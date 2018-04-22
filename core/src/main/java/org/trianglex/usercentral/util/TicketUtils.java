package org.trianglex.usercentral.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trianglex.common.util.AES256Utils;
import org.trianglex.common.util.JsonUtils;
import org.trianglex.usercentral.dto.Ticket;

import java.io.UnsupportedEncodingException;

public abstract class TicketUtils {

    private static final Logger logger = LoggerFactory.getLogger(TicketUtils.class);

    private TicketUtils() {

    }

    public static String generateTicket(Ticket ticket, String secretKey) {
        return AES256Utils.encodeToUrlSafeString(
                AES256Utils.encrypt(JsonUtils.toJsonString(ticket).getBytes(), secretKey));
    }

    public static Ticket parseTicket(String ticketString, String secretKey) {
        try {
            byte[] bytes = AES256Utils.decrypt(AES256Utils.decodeFromUrlSafeString(ticketString), secretKey);
            return JsonUtils.parse(new String(bytes, "UTF-8"), Ticket.class);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
