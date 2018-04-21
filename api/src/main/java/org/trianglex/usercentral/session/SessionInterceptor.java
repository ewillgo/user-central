package org.trianglex.usercentral.session;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    private static final String DEFAULT_LOGIN_PAGE = "/login";

    private String loginPage = DEFAULT_LOGIN_PAGE;
    private RemoteRequest<Object> remoteRequest;

    public SessionInterceptor() {

    }

    public SessionInterceptor(String loginPage) {
        this.loginPage = loginPage;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(loginPage);
            return false;
        }

        remoteRequest.getSession();

        return true;
    }

}
