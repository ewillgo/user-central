package org.trianglex.usercentral.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.trianglex.common.dto.Result;
import org.trianglex.common.exception.BusinessException;
import org.trianglex.common.security.auth.ApiAuthorization;
import org.trianglex.common.support.ConstPair;
import org.trianglex.common.util.PasswordUtils;
import org.trianglex.common.util.RegexUtils;
import org.trianglex.common.util.ToolUtils;
import org.trianglex.usercentral.domain.User;
import org.trianglex.usercentral.domain.UserPrivilege;
import org.trianglex.usercentral.dto.*;
import org.trianglex.usercentral.service.UserPrivilegeService;
import org.trianglex.usercentral.service.UserService;
import org.trianglex.usercentral.session.*;
import org.trianglex.usercentral.util.TicketUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.trianglex.common.util.RegexUtils.UUID;
import static org.trianglex.usercentral.constant.UrlConstant.*;
import static org.trianglex.usercentral.constant.UserConstant.*;

@RestController
@RequestMapping(C_API_USER)
public class UserCentralController {

    private static final Logger logger = LoggerFactory.getLogger(UserCentralController.class);

    @Autowired
    private SessionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Autowired
    private TicketProperties ticketProperties;

    @Autowired
    private AccessTokenProperties accessTokenProperties;

    @ApiAuthorization(message = SIGN_ERROR)
    @GetMapping(value = M_API_USER_GET_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<RegisterResponse> register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest, HttpServletResponse response) {

        Result<RegisterResponse> result = new Result<>();

        if (userService.isUsernameExists(registerRequest.getUsername())) {
            throw new BusinessException(USER_NAME_EXISTS.getStatus(), USER_NAME_EXISTS.getMessage());
        }

        if (userService.isNicknameExists(registerRequest.getNickname())) {
            throw new BusinessException(USER_NICKNAME_EXISTS.getStatus(), USER_NICKNAME_EXISTS.getMessage());
        }

        if (!StringUtils.isEmpty(registerRequest.getPhone()) && userService.isPhoneExists(registerRequest.getPhone())) {
            throw new BusinessException(USER_PHONE_EXISTS.getStatus(), USER_PHONE_EXISTS.getMessage());
        }

        if (!StringUtils.isEmpty(registerRequest.getEmail())
                && !RegexUtils.isMatch(registerRequest.getEmail(), RegexUtils.EMAIL)) {
            throw new BusinessException(USER_INCORRECT_EMAIL.getStatus(), USER_INCORRECT_EMAIL.getMessage());
        } else if (StringUtils.isEmpty(registerRequest.getEmail())
                && RegexUtils.isMatch(registerRequest.getUsername(), RegexUtils.EMAIL)) {
            registerRequest.setEmail(registerRequest.getUsername());
        }

        if (!StringUtils.isEmpty(registerRequest.getIdCard())
                && !RegexUtils.isMatch(registerRequest.getIdCard(), RegexUtils.ID_CARD_18)) {
            throw new BusinessException(USER_INCORRECT_IDCARD.getStatus(), USER_INCORRECT_IDCARD.getMessage());
        } else if (!StringUtils.isEmpty(registerRequest.getIdCard())) {
            registerRequest.setGender(ToolUtils.extractGender(registerRequest.getIdCard()));
            registerRequest.setBirth(ToolUtils.extractBirth(registerRequest.getIdCard()));
        }

        User user = registerRequest.toPO(new User());

        user.setUserId((StringUtils.isEmpty(user.getUserId()) || !RegexUtils.isMatch(user.getUserId(), UUID))
                ? ToolUtils.getUUID() : user.getUserId().toLowerCase().replaceAll("-", ""));

        user.setSalt(PasswordUtils.salt256());
        user.setPassword(PasswordUtils.password(user.getPassword(), user.getSalt()));

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getId());

        registerResponse.setTicket(generateTicket(user.getUserId()));
        if (StringUtils.isEmpty(registerResponse.getTicket())) {
            throw new BusinessException(TICKET_GENERATE_ERROR.getStatus(), TICKET_GENERATE_ERROR.getMessage());
        }

        boolean ret;

        try {
            ret = userService.addUser(user);
        } catch (Exception e) {
            if (!(e instanceof DuplicateKeyException)) {
                logger.error(e.getMessage(), e);
            }
            throw new BusinessException(USER_REPEAT.getStatus(), USER_REPEAT.getMessage());
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

    @ApiAuthorization(message = SIGN_ERROR)
    @GetMapping(value = M_API_USER_GET_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<LoginResponse> login(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, HttpServletResponse response) {

        Result<LoginResponse> result = new Result<>();

        User user = userService.getUserByUsername(loginRequest.getUsername(), "salt");
        if (user == null) {
            throw new BusinessException(USER_NOT_EXISTS.getStatus(), USER_NOT_EXISTS.getMessage());
        }

        user = userService.getUserByUsernameAndPassword(loginRequest.getUsername(),
                PasswordUtils.password(loginRequest.getPassword(), user.getSalt()), "user_id");

        if (user == null) {
            throw new BusinessException(USER_NOT_EXISTS.getStatus(), USER_NOT_EXISTS.getMessage());
        }

        String ticket = generateTicket(user.getUserId());
        if (StringUtils.isEmpty(ticket)) {
            throw new BusinessException(TICKET_GENERATE_ERROR.getStatus(), TICKET_GENERATE_ERROR.getMessage());
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

    @GetMapping(value = M_API_USER_GET_VALIDATE_TICKET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<ValidateTicketResponse> validateTicket(
            HttpServletRequest request,
            @ModelAttribute("validateTicketRequest") ValidateTicketRequest validateTicketRequest) {

        Result<ValidateTicketResponse> result = new Result<>();
        Ticket ticket = TicketUtils.parseTicket(
                validateTicketRequest.getTicket(), ticketProperties.getTicketEncryptKey());

        if (ticket == null) {
            throw new BusinessException(TICKET_INCORRECT.getStatus(), TICKET_INCORRECT.getMessage());
        }

        long currentTimestamp = System.currentTimeMillis();
        if (ticket.getTimestamp() < currentTimestamp) {
            throw new BusinessException(TICKET_TIMEOUT.getStatus(), TICKET_TIMEOUT.getMessage());
        }

        User user = userService.getUserByUserId(ticket.getUserId(),
                "user_id, username, nickname, gender, id_card, birth, " +
                        "phone, email, wechat, weibo, third_id, third_type, avatar, status, create_time");

        if (user == null) {
            throw new BusinessException(TICKET_INCORRECT.getStatus(), TICKET_INCORRECT.getMessage());
        }

        List<UserPrivilege> userPrivilegeList = userPrivilegeService.getUserPrivileges(user.getUserId());

        HttpSession session = request.getSession(false);
        session = session == null ? request.getSession() : session;
        refreshSession(user, userPrivilegeList, session);

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
    @PostMapping(M_API_USER_POST_GET_SESSION)
    public Result<UcSession> getSession(@RequestParam("accessToken") String accessTokenString) {

        Result<UcSession> result = new Result<>();

        AccessToken accessToken =
                TicketUtils.parseAccessToken(accessTokenString, accessTokenProperties.getTokenEncryptKey());

        if (accessToken == null) {
            throw new BusinessException(ACCESS_TOKEN_INVALID.getStatus(), ACCESS_TOKEN_INVALID.getMessage());
        }

        Session session = repository.findById(accessToken.getSessionId());

        if (session == null) {
            throw new BusinessException(GLOBAL_SESSION_TIMEOUT.getStatus(), GLOBAL_SESSION_TIMEOUT.getMessage());
        }

        UcSession ucSession = session.getAttribute(SESSION_USER);
        repository.save(session);

        UcSession.SessionAccessToken sessionAccessToken = new UcSession.SessionAccessToken();
        sessionAccessToken.setToken(accessTokenString);
        sessionAccessToken.setMaxAge(accessTokenProperties.getTokenMaxAge().getSeconds());
        ucSession.setSessionAccessToken(sessionAccessToken);

        result.setData(ucSession);
        result.setStatus(GLOBAL_SESSION_REFRESH_SUCCESS.getStatus());
        result.setMessage(GLOBAL_SESSION_REFRESH_SUCCESS.getMessage());
        return result;
    }

    @PostMapping(M_API_USER_GET_LOGOUT)
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
                    C_API_USER, M_API_USER_GET_VALIDATE_TICKET, ticket, status, ToolUtils.encodeUrl(message)));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private UcSession refreshSession(User user, List<UserPrivilege> userPrivilegeList, HttpSession session) {

        UcSession ucSession = new UcSession();

        UcSession.SessionUser sessionUser = new UcSession.SessionUser();
        BeanUtils.copyProperties(user, sessionUser);
        ucSession.setSessionUser(sessionUser);

        if (!CollectionUtils.isEmpty(userPrivilegeList)) {
            List<UcSession.SessionUserPrivilege> sessionUserPrivilegeList = new ArrayList<>();
            BeanUtils.copyProperties(userPrivilegeList, sessionUserPrivilegeList);
            ucSession.setSessionUserPrivilegeList(sessionUserPrivilegeList);
        }

        session.setAttribute(SESSION_USER, ucSession);
        return ucSession;
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
