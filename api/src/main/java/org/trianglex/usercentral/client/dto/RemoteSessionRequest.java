package org.trianglex.usercentral.client.dto;

import org.trianglex.common.security.auth.ApiAttributes;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.client.constant.UserConstant.ACCESS_TOKEN_NOT_BLANK;

public class RemoteSessionRequest extends ApiAttributes {

    @NotBlank(message = ACCESS_TOKEN_NOT_BLANK)
    private String accessTokenString;

    public String getAccessTokenString() {
        return accessTokenString;
    }

    public void setAccessTokenString(String accessTokenString) {
        this.accessTokenString = accessTokenString;
    }
}
