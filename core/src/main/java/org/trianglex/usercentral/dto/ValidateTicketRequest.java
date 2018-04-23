package org.trianglex.usercentral.dto;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.constant.UserConstant.TICKET_NOT_BLANK;

public class ValidateTicketRequest {

    @NotBlank(message = TICKET_NOT_BLANK)
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
