package org.trianglex.usercentral.session;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "usercentral.access-token")
public class AccessTokenProperties {

    private Duration tokenMaxAge;
    private String tokenEncryptKey;

    public Duration getTokenMaxAge() {
        return tokenMaxAge;
    }

    public void setTokenMaxAge(Duration tokenMaxAge) {
        this.tokenMaxAge = tokenMaxAge;
    }

    public String getTokenEncryptKey() {
        return tokenEncryptKey;
    }

    public void setTokenEncryptKey(String tokenEncryptKey) {
        this.tokenEncryptKey = tokenEncryptKey;
    }
}
