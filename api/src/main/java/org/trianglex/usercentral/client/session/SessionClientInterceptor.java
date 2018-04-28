package org.trianglex.usercentral.client.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.trianglex.common.dto.Result;
import org.trianglex.common.support.ConstPair;
import org.trianglex.common.util.JsonUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.trianglex.usercentral.client.constant.ClientConstant.*;

public class SessionClientInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SessionClientInterceptor.class);

    @Autowired
    private RemoteRequest remoteRequest;

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie ucToken = WebUtils.getCookie(request, COOKIE_NAME);
        String accessTokenString = ucToken != null ? ucToken.getValue() : request.getParameter("accessToken");
        if (StringUtils.isEmpty(accessTokenString)) {
            returnResult(ACCESS_TOCKEN_INVALIDATE, response);
            return false;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            try {
                Result<RemoteSession> result = remoteRequest.getRemoteSession(accessTokenString);

                if (result.getData() == null) {
                    returnResult(ConstPair.make(result.getStatus(), result.getMessage()), response);
                    return false;
                }

                RemoteSession remoteSession = result.getData();
                UserCentralSession userCentralSession = remoteSession.getUserCentralSession();

                if (!StringUtils.isEmpty(remoteSession.getRemoteAccessToken().getToken())) {
                    Cookie cookie = new Cookie(COOKIE_NAME, remoteSession.getRemoteAccessToken().getToken());
                    cookie.setMaxAge((int) remoteSession.getRemoteAccessToken().getMaxAge());
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }

                session = request.getSession();
                session.setAttribute(SESSION_KEY, userCentralSession);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                returnResult(ACCESS_TOCKEN_EXCEPTION, response);
                return false;
            }
        }

        return true;
    }

    private void returnResult(ConstPair pair, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            Result result = new Result(pair.getStatus(), pair.getMessage());
            servletOutputStream.write(JsonUtils.toJsonString(result).getBytes());
        }
    }

}
