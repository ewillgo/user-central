package org.trianglex.usercentral.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.trianglex.common.dto.Result;
import org.trianglex.common.support.ConstPair;
import org.trianglex.common.util.PasswordUtils;
import org.trianglex.common.util.RegexUtils;
import org.trianglex.usercentral.domain.Privilege;
import org.trianglex.usercentral.domain.User;
import org.trianglex.usercentral.dto.LoginRequest;
import org.trianglex.usercentral.dto.LoginResponse;
import org.trianglex.usercentral.dto.RegisterRequest;
import org.trianglex.usercentral.dto.RegisterResponse;
import org.trianglex.usercentral.service.UserService;
import org.trianglex.usercentral.session.*;
import org.trianglex.usercentral.util.TicketUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.trianglex.usercentral.constant.UrlConstant.*;
import static org.trianglex.usercentral.constant.UserConstant.*;

@RestController
@RequestMapping(C_USER)
public class UserCentralController {

    private static final Logger logger = LoggerFactory.getLogger(UserCentralController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TicketProperties ticketProperties;

    @Autowired
    private AccessTokenProperties accessTokenProperties;

    @PostMapping(M_USER_POST_REGISTER)
    public Result<RegisterResponse> register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest) {

        Result<RegisterResponse> result = new Result<>();

        if (userService.isUsernameExists(registerRequest.getUsername())) {
            result.setStatus(USER_NAME_EXISTS.getStatus());
            result.setMessage(USER_NAME_EXISTS.getMessage());
            return result;
        }

        if (userService.isNicknameExists(registerRequest.getNickname())) {
            result.setStatus(USER_NICKNAME_EXISTS.getStatus());
            result.setMessage(USER_NICKNAME_EXISTS.getMessage());
            return result;
        }

        User user = registerRequest.toPO(new User());
        user.setUserId(user.getUserId().toLowerCase().replaceAll("-", ""));
        user.setSalt(PasswordUtils.salt256());
        user.setPassword(PasswordUtils.password(user.getPassword(), user.getSalt()));

        if (RegexUtils.isMatch(user.getUsername(), RegexUtils.EMAIL)) {
            user.setEmail(user.getUsername());
        }

        boolean ret;

        try {
            ret = userService.addUser(user);
        } catch (Exception e) {
            if (!(e instanceof DuplicateKeyException)) {
                logger.error(e.getMessage(), e);
            }
            result.setStatus(USER_REPEAT.getStatus());
            result.setMessage(USER_REPEAT.getMessage());
            return result;
        }

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getId());

        if (ret) {
            registerResponse.setTicket(generateTicket(user.getUserId()));
            if (StringUtils.isEmpty(registerResponse.getTicket())) {
                result.setStatus(GENERATE_TICKET_ERROR.getStatus());
                result.setMessage(GENERATE_TICKET_ERROR.getMessage());
                return result;
            }
        }

        ConstPair constPair = ret ? USER_REGISTER_SUCCESS : USER_REGISTER_FAIL;

        result.setData(registerResponse);
        result.setStatus(constPair.getStatus());
        result.setMessage(constPair.getMessage());
        return result;
    }

    @PostMapping(M_USER_POST_LOGIN)
    public Result<LoginResponse> login(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, HttpServletRequest request) {

        Result<LoginResponse> result = new Result<>();

        User user = userService.getUserByUsername(loginRequest.getUsername(), "salt");
        if (user == null) {
            result.setStatus(USER_NOT_EXISTS.getStatus());
            result.setMessage(USER_NOT_EXISTS.getMessage());
            return result;
        }

        user = userService.getUserByUsernameAndPassword(
                loginRequest.getUsername(), PasswordUtils.password(loginRequest.getPassword(), user.getSalt()), "id");

        if (user == null) {
            result.setStatus(USER_NOT_EXISTS.getStatus());
            result.setMessage(USER_NOT_EXISTS.getMessage());
            return result;
        }

        String ticket = generateTicket(user.getUserId());
        if (StringUtils.isEmpty(ticket)) {
            result.setStatus(GENERATE_TICKET_ERROR.getStatus());
            result.setMessage(GENERATE_TICKET_ERROR.getMessage());
            return result;
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setTicket(ticket);

        result.setData(loginResponse);
        result.setStatus(USER_LOGIN_SUCCESS.getStatus());
        result.setMessage(USER_LOGIN_SUCCESS.getMessage());
        return result;
    }

    @PostMapping(M_USER_POST_VALIDATE_TICKET)
    public Result<UserCentralSession> validateTicket(
            @RequestParam("ticket") String ticketString, HttpServletRequest request) {

        Result<UserCentralSession> result = new Result<>();
        Ticket ticket = TicketUtils.parseTicket(ticketString, ticketProperties.getTicketEncryptKey());

        if (ticket == null) {
            result.setStatus(INCORRECT_TICKET.getStatus());
            result.setMessage(INCORRECT_TICKET.getMessage());
            return result;
        }

        long currentTimestamp = System.currentTimeMillis();
        if (ticket.getTimestamp() < currentTimestamp) {
            result.setStatus(TICKET_TIMEOUT.getStatus());
            result.setMessage(TICKET_TIMEOUT.getMessage());
            return result;
        }

        User user = userService.getUserByUserId(ticket.getUserId(), "*");

        if (user == null) {
            result.setStatus(INCORRECT_TICKET.getStatus());
            result.setMessage(INCORRECT_TICKET.getMessage());
            return result;
        }

        HttpSession session = request.getSession();
        UserCentralSession userCentralSession = refreshSession(user, request.getSession());
        userCentralSession.getUser().setAccessToken(generateAccessToken(user.getUserId(), session.getId()));

        result.setData(userCentralSession);
        result.setStatus(GENERATE_TICKET_SUCCESS.getStatus());
        result.setMessage(GENERATE_TICKET_SUCCESS.getMessage());
        return result;
    }

    @PostMapping(M_USER_POST_REFRESH)
    public Result refreshSession(
            @RequestParam("accessToken") String accessTokenString,
            HttpServletRequest request,
            @SessionAttribute(name = SESSION_KEY, required = false) UserCentralSession session) {

        Result result = new Result();

        AccessToken accessToken = TicketUtils.parseAccessToken(accessTokenString, accessTokenProperties.getTokenEncryptKey());
        if (accessToken == null) {
            result.setStatus(INCORRECT_ACCESS_TOKEN.getStatus());
            result.setMessage(INCORRECT_ACCESS_TOKEN.getMessage());
            return result;
        }

        long currentTimestamp = System.currentTimeMillis();
        if (accessToken.getTimestamp() < currentTimestamp) {
            result.setStatus(ACCESS_TOKEN_TIMEOUT.getStatus());
            result.setMessage(ACCESS_TOKEN_TIMEOUT.getMessage());
            return result;
        }



        return result;
    }

    @PostMapping(M_USER_GET_LOGOUT)
    public Result logout(HttpServletRequest request) {

        Result result = new Result();
        HttpSession session = request.getSession(false);
        ConstPair constPair = session != null ? USER_LOGOUT_SUCCESS : USER_LOGOUT_FAIL;

        if (session != null) {
            session.invalidate();
        }

        result.setStatus(constPair.getStatus());
        result.setMessage(constPair.getMessage());
        return result;
    }

    private UserCentralSession refreshSession(User user, HttpSession session) {
        return refreshSession(user, null, session);
    }

    private UserCentralSession refreshSession(User user, Privilege privilege, HttpSession session) {
        UserCentralSession userCentralSession = privilege == null
                ? new UserCentralSession(user)
                : new UserCentralSession(user, privilege);
        session.setAttribute(SESSION_KEY, userCentralSession);
        return userCentralSession;
    }

    private String generateTicket(String userId) {
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setTimestamp(System.currentTimeMillis() + ticketProperties.getTicketMaxAge().toMillis());
        return TicketUtils.generateTicket(ticket, ticketProperties.getTicketEncryptKey());
    }

    private String generateAccessToken(String userId, String sessionId) {
        AccessToken accessToken = new AccessToken();
        accessToken.setUserId(userId);
        accessToken.setSessionId(sessionId);
        accessToken.setTimestamp(System.currentTimeMillis() + accessTokenProperties.getTokenMaxAge().toMillis());
        return TicketUtils.generateAccessToken(accessToken, accessTokenProperties.getTokenEncryptKey());
    }

}
