package org.trianglex.usercentral.session;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "usercentral.session.cookie")
public class SessionCookieProperties {

    private String cookiePath;
    private Duration cookieMaxAge;
    private boolean useHttpOnlyCookie;
    private String cookieName;

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public Duration getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Duration cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public boolean isUseHttpOnlyCookie() {
        return useHttpOnlyCookie;
    }

    public void setUseHttpOnlyCookie(boolean useHttpOnlyCookie) {
        this.useHttpOnlyCookie = useHttpOnlyCookie;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
