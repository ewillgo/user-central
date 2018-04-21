package org.trianglex.usercentral.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserSessionListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(UserSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("Session id : {} created.", se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session id : {} destroyed.", se.getSession().getId());
    }
}
