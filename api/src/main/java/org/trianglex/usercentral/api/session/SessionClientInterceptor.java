package org.trianglex.usercentral.api.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.trianglex.common.dto.Result;
import org.trianglex.common.exception.ClientApiException;
import org.trianglex.common.security.auth.SignUtils;
import org.trianglex.usercentral.api.UasClient;
import org.trianglex.usercentral.api.constant.CommonCode;
import org.trianglex.usercentral.api.dto.RemoteSessionRequest;
import org.trianglex.usercentral.api.dto.RemoteSessionResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.trianglex.usercentral.api.constant.CommonCode.SESSION_INVALID;
import static org.trianglex.usercentral.api.constant.CommonCode.SESSION_TIMEOUT;
import static org.trianglex.usercentral.api.constant.UasConstant.SESSION_USER;

@Import(UasProperties.class)
public class SessionClientInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SessionClientInterceptor.class);
    private static final String ACCESS_TOKEN_INTERVAL_NAME = "_%s_itv_";

    @Autowired
    private UasClient uasClient;

    @Autowired
    private UasProperties uasProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 优先从Cookie里面获取令牌数据，否则从Request里面获取
        // 从Request里面获取主要用于不同域名之间传递令牌
        Cookie accessTokenCookie = WebUtils.getCookie(request, uasProperties.getCookieName());
        String accessTokenString = Optional.of(accessTokenCookie)
                .map(Cookie::getValue).orElseGet(() -> request.getParameter("accessToken"));

        // 没有发现令牌，会话失效
        if (StringUtils.isEmpty(accessTokenString)) {
            throw new ClientApiException(SESSION_INVALID);
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SESSION_USER) == null
                || autoRefreshRemoteSessionIfNecessary(request)) {

            RemoteSessionResponse remoteSessionResponse = getRemoteSession(accessTokenString);
            if (!StringUtils.isEmpty(remoteSessionResponse.getAccessTokenString())) {
                accessTokenCookie = new Cookie(uasProperties.getCookieName(), remoteSessionResponse.getAccessTokenString());
                accessTokenCookie.setMaxAge((int) uasProperties.getCookieMaxAge().getSeconds());
                accessTokenCookie.setHttpOnly(uasProperties.isUseHttpOnlyCookie());
                accessTokenCookie.setPath(uasProperties.getCookiePath());
                accessTokenCookie.setDomain(uasProperties.getCookieDomain());
                response.addCookie(accessTokenCookie);
            }

            Cookie accessTokenIntervalCookie = new Cookie(
                    String.format(ACCESS_TOKEN_INTERVAL_NAME, uasProperties.getCookieName()),
                    String.valueOf(remoteSessionResponse.getAccessTokenInterval()));
            accessTokenIntervalCookie.setMaxAge((int) uasProperties.getCookieMaxAge().getSeconds());
            accessTokenIntervalCookie.setHttpOnly(uasProperties.isUseHttpOnlyCookie());
            accessTokenIntervalCookie.setPath(uasProperties.getCookiePath());
            accessTokenIntervalCookie.setDomain(uasProperties.getCookieDomain());
            response.addCookie(accessTokenIntervalCookie);

            session = request.getSession();
            session.setAttribute(SESSION_USER, remoteSessionResponse.getUasSession());

            return true;
        }

        return true;
    }

    /**
     * 判断是否需要刷新远程会话信息
     */
    private boolean autoRefreshRemoteSessionIfNecessary(HttpServletRequest request) {

        // 获取令牌更新时间戳
        Cookie accessTokenIntervalCookie = WebUtils.getCookie(
                request, String.format(ACCESS_TOKEN_INTERVAL_NAME, uasProperties.getCookieName()));

        long accessTokenInterval;

        try {

            if (accessTokenIntervalCookie != null && !StringUtils.isEmpty(accessTokenIntervalCookie.getValue())) {
                accessTokenInterval = Long.valueOf(accessTokenIntervalCookie.getValue());
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return System.currentTimeMillis() >= accessTokenInterval;
    }

    /**
     * 获取远程会话信息
     */
    private RemoteSessionResponse getRemoteSession(String accessTokenString) {

        RemoteSessionRequest remoteSessionRequest = new RemoteSessionRequest();
        remoteSessionRequest.setAccessTokenString(accessTokenString);
        remoteSessionRequest.setAppKey(uasProperties.getAppKey());
        remoteSessionRequest.setSign(SignUtils.sign(remoteSessionRequest, uasProperties.getAppSecret()));

        Result<RemoteSessionResponse> result;
        try {
            result = uasClient.getRemoteSession(remoteSessionRequest);
        } catch (Exception e) {
            // 虽然获取远程会话超时，但是不等于远程会话已经失效，所以提示用户刷新页面，以便重新获取
            throw new ClientApiException(SESSION_TIMEOUT, e);
        }

        // 远程会话失效
        if (result.getStatus() != CommonCode.SUCCESS.getStatus().intValue()) {
            throw new ClientApiException(SESSION_INVALID);
        }

        return result.getData();
    }

}
