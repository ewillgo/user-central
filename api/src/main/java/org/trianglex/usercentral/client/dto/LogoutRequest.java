package org.trianglex.usercentral.client.dto;

import org.trianglex.common.security.auth.ApiAttributes;
import org.trianglex.common.validation.IsUUID;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.client.constant.UserConstant.*;

public class LogoutRequest extends ApiAttributes {

    @NotBlank(message = USER_ID_NOT_BLANK)
    @IsUUID(message = UUID_ERROR)
    private String userId;

    @NotBlank(message = SESSION_ID_NOT_BLANK)
    private String sessionId;

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
}
