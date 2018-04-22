package org.trianglex.usercentral.dto;

import org.trianglex.common.dto.DtoAttributes;
import org.trianglex.common.validation.IsUUID;
import org.trianglex.usercentral.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.trianglex.usercentral.constant.UserConstant.*;

public class RegisterRequest implements DtoAttributes<User> {

    private static final long serialVersionUID = 1498144726563297831L;

    @NotBlank(message = USERID_NOT_NULL)
    @IsUUID(message = USERID_NOT_UUID)
    private String userId;

    @NotBlank(message = USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;

    @NotBlank(message = NICKNAME_NOT_BLANK)
    private String nickname;

    @NotNull(message = GENDER_NOT_NULL)
    private Integer gender;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
