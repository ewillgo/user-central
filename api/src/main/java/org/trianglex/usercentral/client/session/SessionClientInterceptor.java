package org.trianglex.usercentral.client.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Autowired
    private RemoteRequest remoteRequest;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie ucToken = WebUtils.getCookie(request, COOKIE_NAME);
        String accessTokenString = ucToken != null ? ucToken.getValue() : request.getParameter("accessToken");
        if (accessTokenString == null || accessTokenString.length() == 0) {
            returnResult(ACCESS_TOCKEN_INVALIDATE, response);
            return false;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {

            UserCentralSession userCentralSession = remoteRequest.getRemoteSession(accessTokenString);
            if (userCentralSession == null) {
                returnResult(ACCESS_TOCKEN_TIMEOUT, response);
                return false;
            }

            session = request.getSession();
            session.setAttribute(SESSION_KEY, userCentralSession);
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
