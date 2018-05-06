package org.trianglex.usercentral.dto;

import org.trianglex.common.security.auth.ApiAttributes;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.constant.UserConstant.*;

public class LoginRequest extends ApiAttributes {

    @NotBlank(message = USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
