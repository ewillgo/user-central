package org.trianglex.usercentral.api.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "uas")
public class UasProperties {

    private String appKey;
    private String appSecret;
    private String cookieName;
    private String cookiePath;
    private String cookieDomain;
    private Duration cookieMaxAge;
    private boolean useHttpOnlyCookie;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
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
}
