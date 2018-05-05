package org.trianglex.usercentral.dto;

import org.trianglex.common.validation.IsUUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Map;

import static org.trianglex.usercentral.constant.UserConstant.UPDATE_DATA_NOT_NULL;
import static org.trianglex.usercentral.constant.UserConstant.USER_ID_IS_UUID;
import static org.trianglex.usercentral.constant.UserConstant.USER_ID_NOT_BLANK;

public class UpdateUserRequest {

    @IsUUID(message = USER_ID_IS_UUID)
    @NotBlank(message = USER_ID_NOT_BLANK)
    private String userId;

    @NotNull(message = UPDATE_DATA_NOT_NULL)
    private Map<String, Object> data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
