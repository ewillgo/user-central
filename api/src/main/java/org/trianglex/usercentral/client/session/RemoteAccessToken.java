package org.trianglex.usercentral.client.session;

public class RemoteAccessToken {

    private String token;
    private long maxAge;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }
}
