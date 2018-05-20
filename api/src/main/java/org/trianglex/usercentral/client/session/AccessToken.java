package org.trianglex.usercentral.client.session;

import java.time.Duration;

public class AccessToken {

    private String userId;
    private String sessionId;
    private long interval;

    public AccessToken() {

    }

    public AccessToken(String userId, String sessionId, long interval) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.interval = interval;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public static long interval() {
        return System.currentTimeMillis() + Duration.ofMinutes(5).toMillis();
    }
}
