package org.trianglex.usercentral.dto;

import org.trianglex.common.dto.DtoAttributes;
import org.trianglex.common.validation.IsUUID;
import org.trianglex.usercentral.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.trianglex.usercentral.constant.UserConstant.*;

public class RegisterRequest implements DtoAttributes<User> {

    private static final long serialVersionUID = 1498144726563297831L;

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

    private String idCard;
    private String birth;
    private String phone;
    private String email;
    private String wechat;
    private String weibo;
    private String thirdId;
    private Integer thirdType;

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public Integer getThirdType() {
        return thirdType;
    }

    public void setThirdType(Integer thirdType) {
        this.thirdType = thirdType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

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
