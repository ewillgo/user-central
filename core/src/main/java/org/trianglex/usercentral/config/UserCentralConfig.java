package org.trianglex.usercentral.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.trianglex.common.config.WebConfig;
import org.trianglex.common.security.XssRequestFilter;
import org.trianglex.common.security.auth.AuthorizationAspect;
import org.trianglex.usercentral.config.cors.UserCentralWebSecurityConfig;
import org.trianglex.usercentral.security.AppCacheLoader;

@Configuration
@Import({WebConfig.class, UserCentralWebSecurityConfig.class, XssRequestFilter.class})
public class UserCentralConfig {

    @Bean(name = "appCacheLoader")
    public CacheLoader<String, String> cacheLoader() {
        return new AppCacheLoader();
    }

    @Bean
    public AuthorizationAspect authorizationAspect() {
        return new AuthorizationAspect();
    }
}
