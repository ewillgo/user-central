package org.trianglex.usercentral.util;

import org.trianglex.common.util.AES256Utils;
import org.trianglex.common.util.JsonUtils;
import org.trianglex.usercentral.dto.Ticket;

public abstract class TicketUtils {

    private TicketUtils() {

    }

    public static String generateTicket(Ticket ticket, String secretKey) {
        return AES256Utils.encodeToUrlSafeString(
                AES256Utils.encrypt(JsonUtils.toJsonString(ticket).getBytes(), secretKey));
    }

}
