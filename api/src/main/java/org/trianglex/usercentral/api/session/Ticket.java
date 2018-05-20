package org.trianglex.usercentral.api.session;

public class Ticket {

    private String appKey;
    private String accessTokenString;

    public Ticket() {

    }

    public Ticket(String appKey, String accessTokenString) {
        this.appKey = appKey;
        this.accessTokenString = accessTokenString;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAccessTokenString() {
        return accessTokenString;
    }

    public void setAccessTokenString(String accessTokenString) {
        this.accessTokenString = accessTokenString;
    }

}
