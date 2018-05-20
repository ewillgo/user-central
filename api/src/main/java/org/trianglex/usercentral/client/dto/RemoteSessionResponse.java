package org.trianglex.usercentral.client.dto;

import org.trianglex.usercentral.client.session.UasSession;

public class RemoteSessionResponse {

    private UasSession uasSession;
    private String accessTokenString;
    private long accessTokenInterval;

    public RemoteSessionResponse() {

    }

    public RemoteSessionResponse(UasSession uasSession, String accessTokenString, long accessTokenInterval) {
        this.uasSession = uasSession;
        this.accessTokenString = accessTokenString;
        this.accessTokenInterval = accessTokenInterval;
    }

    public UasSession getUasSession() {
        return uasSession;
    }

    public void setUasSession(UasSession uasSession) {
        this.uasSession = uasSession;
    }

    public String getAccessTokenString() {
        return accessTokenString;
    }

    public void setAccessTokenString(String accessTokenString) {
        this.accessTokenString = accessTokenString;
    }

    public long getAccessTokenInterval() {
        return accessTokenInterval;
    }

    public void setAccessTokenInterval(long accessTokenInterval) {
        this.accessTokenInterval = accessTokenInterval;
    }
}
