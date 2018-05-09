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
import org.trianglex.common.exception.ApiCode;
import org.trianglex.common.exception.ApiErrorException;
import org.trianglex.common.security.auth.ApiAuthorization;
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
import static org.trianglex.usercentral.constant.UserBusinessCode.*;
import static org.trianglex.usercentral.constant.UserConstant.SESSION_USER;
import static org.trianglex.usercentral.constant.UserConstant.SIGN_ERROR;

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

        if (userService.isUsernameExists(registerRequest.getUsername())) {
            throw new ApiErrorException(USER_NAME_EXISTS);
        }

        if (userService.isNicknameExists(registerRequest.getNickname())) {
            throw new ApiErrorException(USER_NICKNAME_EXISTS);
        }

        if (!StringUtils.isEmpty(registerRequest.getPhone()) && userService.isPhoneExists(registerRequest.getPhone())) {
            throw new ApiErrorException(USER_PHONE_EXISTS);
        }

        if (!StringUtils.isEmpty(registerRequest.getEmail())
                && !RegexUtils.isMatch(registerRequest.getEmail(), RegexUtils.EMAIL)) {
            throw new ApiErrorException(USER_INCORRECT_EMAIL);
        } else if (StringUtils.isEmpty(registerRequest.getEmail())
                && RegexUtils.isMatch(registerRequest.getUsername(), RegexUtils.EMAIL)) {
            registerRequest.setEmail(registerRequest.getUsername());
        }

        if (!StringUtils.isEmpty(registerRequest.getIdCard())
                && !RegexUtils.isMatch(registerRequest.getIdCard(), RegexUtils.ID_CARD_18)) {
            throw new ApiErrorException(USER_INCORRECT_IDCARD);
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
            throw new ApiErrorException(TICKET_GENERATE_ERROR);
        }

        boolean ret;

        try {
            ret = userService.addUser(user);
        } catch (Exception e) {
            if (!(e instanceof DuplicateKeyException)) {
                logger.error(e.getMessage(), e);
            }
            throw new ApiErrorException(USER_REPEAT);
        }

        ApiCode apiCode = ret ? USER_REGISTER_SUCCESS : USER_REGISTER_FAIL;

        if (ret) {
            redirect(registerResponse.getTicket(), apiCode.getStatus(), apiCode.getMessage(), response);
        }

        return Result.of(apiCode, registerResponse);
    }

    @ApiAuthorization(message = SIGN_ERROR)
    @GetMapping(value = M_API_USER_GET_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<LoginResponse> login(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, HttpServletResponse response) {

        User user = userService.getUserByUsername(loginRequest.getUsername(), "salt");
        if (user == null) {
            throw new ApiErrorException(USER_NOT_EXISTS);
        }

        user = userService.getUserByUsernameAndPassword(loginRequest.getUsername(),
                PasswordUtils.password(loginRequest.getPassword(), user.getSalt()), "user_id");

        if (user == null) {
            throw new ApiErrorException(USER_NOT_EXISTS);
        }

        String ticket = generateTicket(user.getUserId());
        if (StringUtils.isEmpty(ticket)) {
            throw new ApiErrorException(TICKET_GENERATE_ERROR);
        }

        redirect(ticket, USER_LOGIN_SUCCESS.getStatus(), USER_LOGIN_SUCCESS.getMessage(), response);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setTicket(ticket);

        return Result.of(USER_LOGIN_SUCCESS, loginResponse);
    }

    @GetMapping(value = M_API_USER_GET_VALIDATE_TICKET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<ValidateTicketResponse> validateTicket(
            HttpServletRequest request,
            @ModelAttribute("validateTicketRequest") ValidateTicketRequest validateTicketRequest) {

        Result<ValidateTicketResponse> result = new Result<>();
        Ticket ticket = TicketUtils.parseTicket(
                validateTicketRequest.getTicket(), ticketProperties.getTicketEncryptKey());

        if (ticket == null) {
            throw new ApiErrorException(TICKET_INCORRECT);
        }

        long currentTimestamp = System.currentTimeMillis();
        if (ticket.getTimestamp() < currentTimestamp) {
            throw new ApiErrorException(TICKET_TIMEOUT);
        }

        User user = userService.getUserByUserId(ticket.getUserId(),
                "user_id, username, nickname, gender, id_card, birth, " +
                        "phone, email, wechat, weibo, third_id, third_type, avatar, status, create_time");

        if (user == null) {
            throw new ApiErrorException(TICKET_INCORRECT);
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

        AccessToken accessToken =
                TicketUtils.parseAccessToken(accessTokenString, accessTokenProperties.getTokenEncryptKey());

        if (accessToken == null) {
            throw new ApiErrorException(ACCESS_TOKEN_INVALID);
        }

        Session session = repository.findById(accessToken.getSessionId());

        if (session == null) {
            throw new ApiErrorException(GLOBAL_SESSION_TIMEOUT);
        }

        UcSession ucSession = session.getAttribute(SESSION_USER);
        repository.save(session);

        UcSession.SessionAccessToken sessionAccessToken = new UcSession.SessionAccessToken();
        sessionAccessToken.setToken(accessTokenString);
        sessionAccessToken.setMaxAge(accessTokenProperties.getTokenMaxAge().getSeconds());
        ucSession.setSessionAccessToken(sessionAccessToken);

        return Result.of(GLOBAL_SESSION_REFRESH_SUCCESS, ucSession);
    }

    @PostMapping(M_API_USER_GET_LOGOUT)
    public Result logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        ApiCode apiCode = session != null ? USER_LOGOUT_SUCCESS : USER_LOGOUT_FAIL;

        if (session != null) {
            session.invalidate();
        }

        return Result.of(apiCode);
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
