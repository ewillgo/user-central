package org.trianglex.usercentral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.trianglex.usercentral.session.AccessTokenProperties;
import org.trianglex.usercentral.session.TicketProperties;
import org.trianglex.usercentral.session.UserSessionListener;

import java.util.Arrays;

@Configuration
@Import({TicketProperties.class, AccessTokenProperties.class})
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
