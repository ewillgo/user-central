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
import org.trianglex.common.exception.ServiceApiException;
import org.trianglex.common.security.auth.ApiAuthorization;
import org.trianglex.common.util.PasswordUtils;
import org.trianglex.common.util.RegexUtils;
import org.trianglex.common.util.ToolUtils;
import org.trianglex.usercentral.api.domain.User;
import org.trianglex.usercentral.api.domain.UserPrivilege;
import org.trianglex.usercentral.api.dto.*;
import org.trianglex.usercentral.api.core.AccessToken;
import org.trianglex.usercentral.api.core.Ticket;
import org.trianglex.usercentral.api.core.UasSession;
import org.trianglex.usercentral.service.UserPrivilegeService;
import org.trianglex.usercentral.service.UserService;
import org.trianglex.usercentral.util.TicketUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static org.trianglex.usercentral.api.constant.CommonCode.SUCCESS;
import static org.trianglex.usercentral.api.constant.UrlConstant.*;
import static org.trianglex.usercentral.api.constant.UasApiCode.*;
import static org.trianglex.usercentral.api.constant.UasConstant.SESSION_USER;
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
    public Result<UasRegisterResponse> register(
            @Valid @RequestBody UasRegisterRequest uasRegisterRequest, HttpServletRequest request) {

        // 判断用户名是否存在
        if (userService.isUsernameExists(uasRegisterRequest.getUsername())) {
            throw new ServiceApiException(USER_NAME_EXISTS);
        }

        // 判断昵称是否存在
        if (userService.isNicknameExists(uasRegisterRequest.getNickname())) {
            throw new ServiceApiException(USER_NICKNAME_EXISTS);
        }

        // 判断手机号是否存在
        if (!StringUtils.isEmpty(uasRegisterRequest.getPhone())
                && userService.isPhoneExists(uasRegisterRequest.getPhone())) {
            throw new ServiceApiException(USER_PHONE_EXISTS);
        }

        // 有传递邮箱才校验，如果用户名刚好是邮箱，则自动填充邮箱字段
        if (!StringUtils.isEmpty(uasRegisterRequest.getEmail())
                && !RegexUtils.isMatch(uasRegisterRequest.getEmail(), RegexUtils.EMAIL)) {
            throw new ServiceApiException(USER_INCORRECT_EMAIL);
        } else if (StringUtils.isEmpty(uasRegisterRequest.getEmail())
                && RegexUtils.isMatch(uasRegisterRequest.getUsername(), RegexUtils.EMAIL)) {
            uasRegisterRequest.setEmail(uasRegisterRequest.getUsername());
        }

        // 如果身份证长度合法，则自动计算性别和出生日期，并填充到相应字段
        if (!StringUtils.isEmpty(uasRegisterRequest.getIdCard())) {
            uasRegisterRequest.setGender(ToolUtils.extractGender(uasRegisterRequest.getIdCard()));
            uasRegisterRequest.setBirth(ToolUtils.extractBirth(uasRegisterRequest.getIdCard()));
        }

        User user = uasRegisterRequest.toPO(new User());
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
            throw new ServiceApiException(USER_REPEAT);
        }

        HttpSession session = request.getSession();
        processSession(user, session);

        UasRegisterResponse uasRegisterResponse = new UasRegisterResponse();
        uasRegisterResponse.setUserId(user.getUserId());
        uasRegisterResponse.setTicketString(generateTicket(
                uasRegisterRequest.getAppKey(), TicketUtils.getAppSecret(request), user.getUserId(), session.getId()));

        return Result.of(ret ? SUCCESS : USER_REGISTER_FAIL, uasRegisterResponse);
    }

    @ApiAuthorization(message = "登录" + SIGN_ERROR)
    @PostMapping(value = M_API_USER_POST_LOGIN)
    public Result<UasLoginResponse> login(@Valid @RequestBody UasLoginRequest uasLoginRequest, HttpServletRequest request) {

        User user = userService.getUserByUsername(uasLoginRequest.getUsername(), "salt");
        if (user == null) {
            throw new ServiceApiException(USER_NOT_EXISTS);
        }

        user = userService.getUserByUsernameAndPassword(uasLoginRequest.getUsername(),
                PasswordUtils.password(uasLoginRequest.getPassword(), user.getSalt()), "user_id");

        if (user == null) {
            throw new ServiceApiException(USER_NOT_EXISTS);
        }

        HttpSession session = request.getSession();
        processSession(user.getUserId(), session);

        UasLoginResponse uasLoginResponse = new UasLoginResponse();
        uasLoginResponse.setUserId(user.getUserId());
        uasLoginResponse.setTicketString(generateTicket(
                uasLoginRequest.getAppKey(), TicketUtils.getAppSecret(request), user.getUserId(), session.getId()));

        return Result.of(SUCCESS, uasLoginResponse);
    }

    @ApiAuthorization(message = "登出" + SIGN_ERROR)
    @PostMapping(M_API_USER_POST_LOGOUT)
    public Result logout(@Valid @RequestBody UasLogoutRequest uasLogoutRequest) {
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
            throw new ServiceApiException(ACCESS_TOKEN_INVALID);
        }

        Session session = repository.findById(accessToken.getSessionId());
        if (session == null) {
            throw new ServiceApiException(GLOBAL_SESSION_TIMEOUT);
        }

        String accessTokenString = TicketUtils.generateAccessToken(accessToken, appSecret);

        if (StringUtils.isEmpty(accessTokenString)) {
            throw new ServiceApiException(ACCESS_TOKEN_GENERATE_ERROR);
        }

        User user = getUserByUserId(accessToken.getUserId());
        if (user == null) {
            throw new ServiceApiException(USER_NOT_EXISTS);
        }

        List<UserPrivilege> userPrivilegeList = userPrivilegeService.getUserPrivileges(user.getUserId());

        UasSession uasSession = new UasSession(user, userPrivilegeList);
        session.setAttribute(SESSION_USER, uasSession);
        repository.save(session);

        return Result.of(SUCCESS, new RemoteSessionResponse(uasSession, accessTokenString, AccessToken.interval()));
    }

    private String generateTicket(String appKey, String appSecret, String userId, String sessionId) {

        AccessToken accessToken = new AccessToken(userId, sessionId);
        String accessTokenString = TicketUtils.generateAccessToken(accessToken, appSecret);

        if (StringUtils.isEmpty(accessTokenString)) {
            throw new ServiceApiException(ACCESS_TOKEN_GENERATE_ERROR);
        }

        Ticket ticket = new Ticket(appKey, accessTokenString);

        String ticketString = TicketUtils.generateTicket(ticket, appSecret);
        if (StringUtils.isEmpty(ticketString)) {
            throw new ServiceApiException(TICKET_GENERATE_ERROR);
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
            throw new ServiceApiException(USER_NOT_EXISTS);
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
