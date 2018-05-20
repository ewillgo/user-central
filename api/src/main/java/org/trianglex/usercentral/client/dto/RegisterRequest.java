package org.trianglex.usercentral.client.dto;

import org.trianglex.common.dto.DtoAttributes;
import org.trianglex.common.security.auth.ApiAttributes;
import org.trianglex.common.validation.IsIdCard;
import org.trianglex.common.validation.IsNickname;
import org.trianglex.common.validation.IsPhone;
import org.trianglex.common.validation.IsUUID;
import org.trianglex.usercentral.client.domain.User;
import org.trianglex.usercentral.client.enums.ThirdType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.trianglex.usercentral.client.constant.UserConstant.*;

public class RegisterRequest extends ApiAttributes implements DtoAttributes<User> {

    private static final long serialVersionUID = 1498144726563297831L;

    @IsUUID(message = UUID_ERROR)
    private String userId;

    @NotBlank(message = USERNAME_NOT_BLANK)
    @Email(message = EMAIL_INCORRECT)
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;

    @NotBlank(message = NICKNAME_NOT_BLANK)
    @IsNickname(message = NICKNAME_INCORRECT)
    private String nickname;

    @NotNull(message = GENDER_NOT_NULL)
    private Integer gender;

    @IsIdCard(message = ID_CARD_INCORRECT)
    private String idCard;

    private String birth;

    @IsPhone(message = PHONE_INCORRECT)
    private String phone;

    private String email;
    private String wechat;
    private String weibo;
    private String province;
    private String city;
    private String address;
    private String thirdId;
    private ThirdType thirdType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public ThirdType getThirdType() {
        return thirdType;
    }

    public void setThirdType(ThirdType thirdType) {
        this.thirdType = thirdType;
    }
}
