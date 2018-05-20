package org.trianglex.usercentral.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.trianglex.common.dto.Result;
import org.trianglex.common.exception.ApiCode;
import org.trianglex.common.exception.ApiErrorException;
import org.trianglex.common.security.auth.ApiAuthorization;
import org.trianglex.common.util.PasswordUtils;
import org.trianglex.common.util.RegexUtils;
import org.trianglex.common.util.ToolUtils;
import org.trianglex.usercentral.client.domain.User;
import org.trianglex.usercentral.client.domain.UserPrivilege;
import org.trianglex.usercentral.client.dto.*;
import org.trianglex.usercentral.client.session.UasSession;
import org.trianglex.usercentral.service.UserPrivilegeService;
import org.trianglex.usercentral.service.UserService;
import org.trianglex.usercentral.client.session.AccessToken;
import org.trianglex.usercentral.client.session.Ticket;
import org.trianglex.usercentral.util.TicketUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static org.trianglex.usercentral.client.constant.CommonCode.SUCCESS;
import static org.trianglex.usercentral.client.constant.UrlConstant.*;
import static org.trianglex.usercentral.client.constant.UserApiCode.*;
import static org.trianglex.usercentral.client.constant.UserConstant.SESSION_USER;
import static org.trianglex.usercentral.constant.SystemConstant.SIGN_ERROR;

@RestController
public class UasController {

    private static final Logger logger = LoggerFactory.getLogger(UasController.class);

    @Autowired
    private SessionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @ApiAuthorization(message = "注册" + SIGN_ERROR)
    @PostMapping(M_API_USER_POST_REGISTER)
    public Result<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest request) {

        // 判断用户名是否存在
        if (userService.isUsernameExists(registerRequest.getUsername())) {
            throw new ApiErrorException(USER_NAME_EXISTS);
        }

        // 判断昵称是否存在
        if (userService.isNicknameExists(registerRequest.getNickname())) {
            throw new ApiErrorException(USER_NICKNAME_EXISTS);
        }

        // 判断手机号是否存在
        if (!StringUtils.isEmpty(registerRequest.getPhone())
                && userService.isPhoneExists(registerRequest.getPhone())) {
            throw new ApiErrorException(USER_PHONE_EXISTS);
        }

        // 有传递邮箱才校验，如果用户名刚好是邮箱，则自动填充邮箱字段
        if (!StringUtils.isEmpty(registerRequest.getEmail())
                && !RegexUtils.isMatch(registerRequest.getEmail(), RegexUtils.EMAIL)) {
            throw new ApiErrorException(USER_INCORRECT_EMAIL);
        } else if (StringUtils.isEmpty(registerRequest.getEmail())
                && RegexUtils.isMatch(registerRequest.getUsername(), RegexUtils.EMAIL)) {
            registerRequest.setEmail(registerRequest.getUsername());
        }

        // 如果身份证长度合法，则自动计算性别和出生日期，并填充到相应字段
        if (!StringUtils.isEmpty(registerRequest.getIdCard())) {
            registerRequest.setGender(ToolUtils.extractGender(registerRequest.getIdCard()));
            registerRequest.setBirth(ToolUtils.extractBirth(registerRequest.getIdCard()));
        }

        User user = registerRequest.toPO(new User());
        user.setUserId(StringUtils.isEmpty(user.getUserId())
                ? ToolUtils.getUUID()
                : user.getUserId().toLowerCase().replaceAll("-", ""));
        user.setSalt(PasswordUtils.salt256());
        user.setPassword(PasswordUtils.password(user.getPassword(), user.getSalt()));

        boolean ret;

        try {
            ret = userService.addUser(user);
        } catch (Exception e) {
            if (!(e instanceof DuplicateKeyException)) {
                logger.error(e.getMessage(), e);
            }
            throw new ApiErrorException(USER_REPEAT);
        }

        HttpSession session = request.getSession();
        processSession(user, session);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setTicketString(generateTicket(
                registerRequest.getAppKey(), TicketUtils.getAppSecret(request), user.getUserId(), session.getId()));

        return Result.of(ret ? SUCCESS : USER_REGISTER_FAIL, registerResponse);
    }

    @ApiAuthorization(message = "登录" + SIGN_ERROR)
    @PostMapping(value = M_API_USER_POST_LOGIN)
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        User user = userService.getUserByUsername(loginRequest.getUsername(), "salt");
        if (user == null) {
            throw new ApiErrorException(USER_NOT_EXISTS);
        }

        user = userService.getUserByUsernameAndPassword(loginRequest.getUsername(),
                PasswordUtils.password(loginRequest.getPassword(), user.getSalt()), "user_id");

        if (user == null) {
            throw new ApiErrorException(USER_NOT_EXISTS);
        }

        HttpSession session = request.getSession();
        processSession(user.getUserId(), session);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getUserId());
        loginResponse.setTicketString(generateTicket(
                loginRequest.getAppKey(), TicketUtils.getAppSecret(request), user.getUserId(), session.getId()));

        return Result.of(SUCCESS, loginResponse);
    }

    @ApiAuthorization(message = "登出" + SIGN_ERROR)
    @PostMapping(M_API_USER_POST_LOGOUT)
    public Result logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        HttpSession session = null;
        ApiCode apiCode = session != null ? SUCCESS : USER_LOGOUT_FAIL;
        return Result.of(apiCode);
    }

    @ApiAuthorization(message = "获取远程会话" + SIGN_ERROR)
    @PostMapping(M_API_USER_POST_GET_REMOTE_SESSION)
    public Result<RemoteSessionResponse> getRemoteSession(
            @Valid @RequestBody RemoteSessionRequest remoteSessionRequest, HttpServletRequest request) {

        String appSecret = TicketUtils.getAppSecret(request);
        AccessToken accessToken = TicketUtils.parseAccessToken(
                remoteSessionRequest.getAccessTokenString(), appSecret);

        if (accessToken == null) {
            throw new ApiErrorException(ACCESS_TOKEN_INVALID);
        }

        Session session = repository.findById(accessToken.getSessionId());
        if (session == null) {
            throw new ApiErrorException(GLOBAL_SESSION_TIMEOUT);
        }

        accessToken.setInterval(AccessToken.interval());
        String accessTokenString = TicketUtils.generateAccessToken(accessToken, appSecret);

        if (StringUtils.isEmpty(accessTokenString)) {
            throw new ApiErrorException(ACCESS_TOKEN_GENERATE_ERROR);
        }

        User user = getUserByUserId(accessToken.getUserId());
        if (user == null) {
            throw new ApiErrorException(USER_NOT_EXISTS);
        }

        List<UserPrivilege> userPrivilegeList = userPrivilegeService.getUserPrivileges(user.getUserId());

        UasSession uasSession = new UasSession(user, userPrivilegeList);
        session.setAttribute(SESSION_USER, uasSession);
        repository.save(session);

        return Result.of(SUCCESS, new RemoteSessionResponse(uasSession, accessTokenString, AccessToken.interval()));
    }

    private String generateTicket(String appKey, String appSecret, String userId, String sessionId) {

        AccessToken accessToken = new AccessToken(userId, sessionId, AccessToken.interval());
        String accessTokenString = TicketUtils.generateAccessToken(accessToken, appSecret);

        if (StringUtils.isEmpty(accessTokenString)) {
            throw new ApiErrorException(ACCESS_TOKEN_GENERATE_ERROR);
        }

        Ticket ticket = new Ticket(appKey, accessTokenString);

        String ticketString = TicketUtils.generateTicket(ticket, appSecret);
        if (StringUtils.isEmpty(ticketString)) {
            throw new ApiErrorException(TICKET_GENERATE_ERROR);
        }

        return ticketString;
    }

    private void processSession(String userId, HttpSession session) {
        User user = new User();
        user.setUserId(userId);
        processSession(user, session);
    }

    private void processSession(User user, HttpSession session) {

        if (user.getUsername() == null) {
            user = getUserByUserId(user.getUserId());
        }

        if (user == null) {
            throw new ApiErrorException(USER_NOT_EXISTS);
        }

        List<UserPrivilege> userPrivilegeList = userPrivilegeService.getUserPrivileges(user.getUserId());

        session.setAttribute(SESSION_USER, new UasSession(user, userPrivilegeList));
    }

    private User getUserByUserId(String userId) {
        return userService.getUserByUserId(userId,
                "user_id, username, nickname, gender, id_card, birth, " +
                        "phone, email, wechat, weibo, third_id, third_type, avatar, status, create_time");
    }
}
