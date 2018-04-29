package org.trianglex.usercentral.dto;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.constant.UserConstant.TICKET_NOT_BLANK;

public class ValidateTicketRequest {

    @NotBlank(message = TICKET_NOT_BLANK)
    private String ticket;
    private Integer status;
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
