package org.trianglex.usercentral.dto;

import org.trianglex.common.validation.IsUUID;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.constant.UserConstant.FIELDS_NOT_BLANK;
import static org.trianglex.usercentral.constant.UserConstant.USER_ID_IS_UUID;
import static org.trianglex.usercentral.constant.UserConstant.USER_ID_NOT_BLANK;

public class UserRequest {

    @IsUUID(message = USER_ID_IS_UUID)
    @NotBlank(message = USER_ID_NOT_BLANK)
    private String userId;

    @NotBlank(message = FIELDS_NOT_BLANK)
    private String fields;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
