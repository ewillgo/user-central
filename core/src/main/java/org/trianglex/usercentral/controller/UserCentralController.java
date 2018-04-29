package org.trianglex.usercentral.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.trianglex.common.dto.Result;
import org.trianglex.common.support.ConstPair;
import org.trianglex.common.util.PasswordUtils;
import org.trianglex.common.util.RegexUtils;
import org.trianglex.common.util.ToolUtils;
import org.trianglex.usercentral.domain.Privilege;
import org.trianglex.usercentral.domain.User;
import org.trianglex.usercentral.dto.*;
import org.trianglex.usercentral.service.UserService;
import org.trianglex.usercentral.session.*;
import org.trianglex.usercentral.util.TicketUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

import static org.trianglex.usercentral.constant.UrlConstant.*;
import static org.trianglex.usercentral.constant.UserConstant.*;

@RestController
@RequestMapping(C_USER)
public class UserCentralController {

    private static final Logger logger = LoggerFactory.getLogger(UserCentralController.class);

    @Autowired
    private SessionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketProperties ticketProperties;

    @Autowired
    private AccessTokenProperties accessTokenProperties;

    @GetMapping(value = "/sessionTest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result sessionTest(HttpServletRequest request) {

        Session session;
        if (request.getSession(false) == null) {
            request.getSession().setAttribute("name", "adf");
        } else {
            session = repository.findById(request.getSession(false).getId());
            repository.save(session);
        }

        return new Result();
    }

    @PostMapping(M_USER_POST_REGISTER)
    public Result<RegisterResponse> register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest, HttpServletResponse response) {

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

        if (!StringUtils.isEmpty(registerRequest.getPhone()) && userService.isPhoneExists(registerRequest.getPhone())) {
            result.setStatus(USER_PHONE_EXISTS.getStatus());
            result.setMessage(USER_PHONE_EXISTS.getMessage());
            return result;
        }

        if (!StringUtils.isEmpty(registerRequest.getEmail())
                && !RegexUtils.isMatch(registerRequest.getEmail(), RegexUtils.EMAIL)) {
            result.setStatus(USER_INCORRECT_EMAIL.getStatus());
            result.setMessage(USER_INCORRECT_EMAIL.getMessage());
            return result;
        } else if (StringUtils.isEmpty(registerRequest.getEmail())
                && RegexUtils.isMatch(registerRequest.getUsername(), RegexUtils.EMAIL)) {
            registerRequest.setEmail(registerRequest.getUsername());
        }

        if (!StringUtils.isEmpty(registerRequest.getIdCard())
                && !RegexUtils.isMatch(registerRequest.getIdCard(), RegexUtils.ID_CARD_18)) {
            result.setStatus(USER_INCORRECT_IDCARD.getStatus());
            result.setMessage(USER_INCORRECT_IDCARD.getMessage());
            return result;
        } else if (!StringUtils.isEmpty(registerRequest.getIdCard())) {
            registerRequest.setGender(ToolUtils.extractGender(registerRequest.getIdCard()));
            registerRequest.setBirth(ToolUtils.extractBirth(registerRequest.getIdCard()));
        }

        User user = registerRequest.toPO(new User());
        user.setUserId(user.getUserId().toLowerCase().replaceAll("-", ""));
        user.setSalt(PasswordUtils.salt256());
        user.setPassword(PasswordUtils.password(user.getPassword(), user.getSalt()));

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getId());

        registerResponse.setTicket(generateTicket(user.getUserId()));
        if (StringUtils.isEmpty(registerResponse.getTicket())) {
            result.setStatus(TICKET_GENERATE_ERROR.getStatus());
            result.setMessage(TICKET_GENERATE_ERROR.getMessage());
            return result;
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

        ConstPair constPair = ret ? USER_REGISTER_SUCCESS : USER_REGISTER_FAIL;

        if (ret) {
            redirect(registerResponse.getTicket(), constPair.getStatus(), constPair.getMessage(), response);
        }

        result.setData(registerResponse);
        result.setStatus(constPair.getStatus());
        result.setMessage(constPair.getMessage());
        return result;
    }

    @PostMapping(M_USER_POST_LOGIN)
    public Result<LoginResponse> login(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, HttpServletResponse response) {

        Result<LoginResponse> result = new Result<>();

        User user = userService.getUserByUsername(loginRequest.getUsername(), "salt");
        if (user == null) {
            result.setStatus(USER_NOT_EXISTS.getStatus());
            result.setMessage(USER_NOT_EXISTS.getMessage());
            return result;
        }

        user = userService.getUserByUsernameAndPassword(loginRequest.getUsername(),
                PasswordUtils.password(loginRequest.getPassword(), user.getSalt()), "user_id");

        if (user == null) {
            result.setStatus(USER_NOT_EXISTS.getStatus());
            result.setMessage(USER_NOT_EXISTS.getMessage());
            return result;
        }

        String ticket = generateTicket(user.getUserId());
        if (StringUtils.isEmpty(ticket)) {
            result.setStatus(TICKET_GENERATE_ERROR.getStatus());
            result.setMessage(TICKET_GENERATE_ERROR.getMessage());
            return result;
        }

        redirect(ticket, USER_LOGIN_SUCCESS.getStatus(), USER_LOGIN_SUCCESS.getMessage(), response);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setTicket(ticket);

        result.setData(loginResponse);
        result.setStatus(USER_LOGIN_SUCCESS.getStatus());
        result.setMessage(USER_LOGIN_SUCCESS.getMessage());
        return result;
    }

    @GetMapping(M_USER_GET_VALIDATE_TICKET)
    public Result<ValidateTicketResponse> validateTicket(
            HttpServletRequest request,
            @ModelAttribute("validateTicketRequest") ValidateTicketRequest validateTicketRequest) {

        Result<ValidateTicketResponse> result = new Result<>();
        Ticket ticket = TicketUtils.parseTicket(
                validateTicketRequest.getTicket(), ticketProperties.getTicketEncryptKey());

        if (ticket == null) {
            result.setStatus(TICKET_INCORRECT.getStatus());
            result.setMessage(TICKET_INCORRECT.getMessage());
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
            result.setStatus(TICKET_INCORRECT.getStatus());
            result.setMessage(TICKET_INCORRECT.getMessage());
            return result;
        }

        HttpSession session = request.getSession(false);
        session = session == null ? request.getSession() : session;
        refreshSession(user, session);

        ValidateTicketResponse response = new ValidateTicketResponse();
        response.setAccessToken(generateAccessToken(user.getUserId(), session.getId()));

        result.setData(response);
        result.setStatus(StringUtils.isEmpty(validateTicketRequest.getStatus())
                ? TICKET_VALIDATE_SUCCESS.getStatus() : validateTicketRequest.getStatus());
        result.setMessage(StringUtils.isEmpty(validateTicketRequest.getMessage())
                ? TICKET_VALIDATE_SUCCESS.getMessage() : validateTicketRequest.getMessage());
        return result;
    }

    @SuppressWarnings("unchecked")
    @PostMapping(M_USER_POST_GET_SESSION)
    public Result<RemoteSession> getSession(@RequestParam("accessToken") String accessTokenString) {

        Result<RemoteSession> result = new Result<>();

        AccessToken accessToken =
                TicketUtils.parseAccessToken(accessTokenString, accessTokenProperties.getTokenEncryptKey());

        if (accessToken == null) {
            result.setStatus(ACCESS_TOKEN_INVALID.getStatus());
            result.setMessage(ACCESS_TOKEN_INVALID.getMessage());
            return result;
        }

        Session session = repository.findById(accessToken.getSessionId());

        if (session == null) {
            result.setStatus(GLOBAL_SESSION_TIMEOUT.getStatus());
            result.setMessage(GLOBAL_SESSION_TIMEOUT.getMessage());
            return result;
        }

        UserCentralSession userCentralSession = session.getAttribute(SESSION_USER);
        repository.save(session);

        RemoteSession remoteSession = new RemoteSession();
        remoteSession.setUserCentralSession(userCentralSession);
        remoteSession.setRemoteAccessToken(new RemoteAccessToken(accessTokenString, accessToken.getMaxAge()));

        result.setData(remoteSession);
        result.setStatus(GLOBAL_SESSION_REFRESH_SUCCESS.getStatus());
        result.setMessage(GLOBAL_SESSION_REFRESH_SUCCESS.getMessage());
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

    private void redirect(String ticket, Integer status, String message, HttpServletResponse response) {
        try {
            ;
            response.sendRedirect(String.format("%s%s?ticket=%s&status=%d&message=%s",
                    C_USER, M_USER_GET_VALIDATE_TICKET, ticket, status, ToolUtils.encodeUrl(message)));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private UserCentralSession refreshSession(User user, HttpSession session) {
        return refreshSession(user, null, session);
    }

    private UserCentralSession refreshSession(User user, Privilege privilege, HttpSession session) {
        UserCentralSession userCentralSession = privilege == null
                ? new UserCentralSession(user)
                : new UserCentralSession(user, privilege);
        session.setAttribute(SESSION_USER, userCentralSession);
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
        accessToken.setMaxAge(accessTokenProperties.getTokenMaxAge().getSeconds());
        return TicketUtils.generateAccessToken(accessToken, accessTokenProperties.getTokenEncryptKey());
    }

}
