package org.trianglex.usercentral.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trianglex.common.util.AES256Utils;
import org.trianglex.common.util.DESUtils;
import org.trianglex.common.util.JsonUtils;
import org.trianglex.usercentral.session.AccessToken;
import org.trianglex.usercentral.session.Ticket;

import java.io.UnsupportedEncodingException;

public abstract class TicketUtils {

    private static final Logger logger = LoggerFactory.getLogger(TicketUtils.class);

    private TicketUtils() {

    }

    public static String generateAccessToken(AccessToken accessToken, String secretKey) {
        try {
            return DESUtils.encodeToUrlSafeString(
                    DESUtils.des3EncodeECB(JsonUtils.toJsonString(accessToken).getBytes(), secretKey));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static AccessToken parseAccessToken(String accessTokenString, String secretKey) {
        try {
            byte[] bytes = DESUtils.des3DecodeECB(
                    DESUtils.decodeFromUrlSafeString(accessTokenString), secretKey);
            return JsonUtils.parse(new String(bytes, "UTF-8"), AccessToken.class);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String generateTicket(Ticket ticket, String secretKey) {
        try {
            return AES256Utils.encodeToUrlSafeString(
                    AES256Utils.encrypt(JsonUtils.toJsonString(ticket).getBytes(), secretKey));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
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
