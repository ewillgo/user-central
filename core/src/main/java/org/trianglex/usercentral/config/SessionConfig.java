package org.trianglex.usercentral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.trianglex.usercentral.session.SessionCookieProperties;
import org.trianglex.usercentral.session.UserSessionListener;

import java.util.Arrays;

@Configuration
@Import(SessionCookieProperties.class)
public class SessionConfig {

    @Bean
    public UserSessionListener userSessionListener() {
        return new UserSessionListener();
    }

    @Bean
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter(UserSessionListener userSessionListener) {
        return new SessionEventHttpSessionListenerAdapter(Arrays.asList(userSessionListener));
    }

    @Bean
    public DefaultCookieSerializer defaultCookieSerializer(SessionCookieProperties properties) {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookiePath(properties.getCookiePath());
        serializer.setCookieMaxAge((int) properties.getCookieMaxAge().getSeconds());
        serializer.setUseHttpOnlyCookie(properties.isUseHttpOnlyCookie());
        serializer.setCookieName(properties.getCookieName());
        return serializer;
    }

}
