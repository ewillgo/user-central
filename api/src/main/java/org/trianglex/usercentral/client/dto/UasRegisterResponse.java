package org.trianglex.usercentral.client.dto;

public class UasRegisterResponse {

    private String userId;
    private String ticketString;

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
