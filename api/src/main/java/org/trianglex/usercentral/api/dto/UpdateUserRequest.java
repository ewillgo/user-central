package org.trianglex.usercentral.api.dto;

import org.trianglex.common.validation.IsUUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static org.trianglex.usercentral.api.constant.UserConstant.*;

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
