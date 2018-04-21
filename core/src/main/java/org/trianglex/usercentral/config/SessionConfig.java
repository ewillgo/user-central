package org.trianglex.usercentral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.trianglex.usercentral.session.UserSessionListener;

import java.util.Arrays;

@Configuration
public class SessionConfig {

    @Bean
    public UserSessionListener userSessionListener() {
        return new UserSessionListener();
    }

    @Bean
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter(UserSessionListener userSessionListener) {
        return new SessionEventHttpSessionListenerAdapter(Arrays.asList(userSessionListener));
    }

}
