package org.trianglex.usercentral.client.session;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.trianglex.common.dto.Result;
import org.trianglex.common.support.ConstPair;
import org.trianglex.common.util.JsonUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.trianglex.usercentral.client.constant.ClientConstant.ACCESS_TOCKEN_INVALIDATE;
import static org.trianglex.usercentral.client.constant.ClientConstant.ACCESS_TOCKEN_TIMEOUT;

public class SessionClientInterceptor extends HandlerInterceptorAdapter {

    private RemoteRequest remoteRequest;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String accessTokenString = request.getParameter("ticket");
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
