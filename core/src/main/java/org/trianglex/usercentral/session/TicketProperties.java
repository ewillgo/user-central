package org.trianglex.usercentral.session;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "usercentral.ticket")
public class TicketProperties {

    private Duration ticketMaxAge;
    private String ticketEncryptKey;

    public Duration getTicketMaxAge() {
        return ticketMaxAge;
    }

    public void setTicketMaxAge(Duration ticketMaxAge) {
        this.ticketMaxAge = ticketMaxAge;
    }

    public String getTicketEncryptKey() {
        return ticketEncryptKey;
    }

    public void setTicketEncryptKey(String ticketEncryptKey) {
        this.ticketEncryptKey = ticketEncryptKey;
    }
}
