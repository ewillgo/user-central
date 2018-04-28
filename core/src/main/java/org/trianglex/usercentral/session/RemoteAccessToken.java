package org.trianglex.usercentral.session;

public class RemoteAccessToken {

    private String token;
    private long maxAge;

    public RemoteAccessToken(String token, long maxAge) {
        this.token = token;
        this.maxAge = maxAge;
    }

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
