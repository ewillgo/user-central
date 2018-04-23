package org.trianglex.usercentral.dto;

public class ValidateTicketResponse {

    private String userId;
    private String sessionId;
    private int maxAgeInSeconds;

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

    public int getMaxAgeInSeconds() {
        return maxAgeInSeconds;
    }

    public void setMaxAgeInSeconds(int maxAgeInSeconds) {
        this.maxAgeInSeconds = maxAgeInSeconds;
    }
}
