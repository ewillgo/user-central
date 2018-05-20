package org.trianglex.usercentral.client.dto;

public class LoginResponse {

    private String userId;
    private String ticketString;

    public LoginResponse() {

    }

    public LoginResponse(String userId, String ticketString) {
        this.userId = userId;
        this.ticketString = ticketString;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTicketString() {
        return ticketString;
    }

    public void setTicketString(String ticketString) {
        this.ticketString = ticketString;
    }
}
