package org.trianglex.usercentral.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.trianglex.common.util.JsonUtils;
import org.trianglex.usercentral.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.trianglex.usercentral.constant.UserConstant.SESSION_KEY;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        SessionUser sessionUser;
        HttpSession session = request.getSession(false);

        if (session != null
                && (sessionUser = (SessionUser) session.getAttribute(SESSION_KEY)) != null
                && !StringUtils.isEmpty(sessionUser.getTicket())) {

            try {
                response.setContentType("application/json");
                response.getWriter().write(JsonUtils.toJsonString(sessionUser));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                response.getWriter().close();
            }

            return false;
        }

        return true;
    }

}
