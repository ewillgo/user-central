package org.trianglex.usercentral.dto;

import javax.validation.constraints.NotBlank;

import static org.trianglex.usercentral.constant.UserConstant.*;

public class LoginDTO {

    @NotBlank(message = USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;

    @NotBlank(message = CAPTCHA_NOT_NULL)
    private String captcha;

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
