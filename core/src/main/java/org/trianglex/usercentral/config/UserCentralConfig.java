package org.trianglex.usercentral.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.trianglex.common.config.WebConfig;
import org.trianglex.common.security.XssRequestFilter;
import org.trianglex.usercentral.config.cors.UserCentralWebSecurityConfig;

@Configuration
@Import({WebConfig.class, UserCentralWebSecurityConfig.class, XssRequestFilter.class})
public class UserCentralConfig {

}
