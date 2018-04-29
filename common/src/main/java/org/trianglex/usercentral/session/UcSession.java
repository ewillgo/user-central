package org.trianglex.usercentral.session;

import org.trianglex.usercentral.enums.ThirdType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UcSession implements Serializable {

    private static final long serialVersionUID = -1398320661646042419L;
    private SessionUser sessionUser;
    private List<SessionUserPrivilege> sessionUserPrivilegeList;
    private SessionAccessToken sessionAccessToken;

    public SessionUser getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    public List<SessionUserPrivilege> getSessionUserPrivilegeList() {
        return sessionUserPrivilegeList;
    }

    public void setSessionUserPrivilegeList(List<SessionUserPrivilege> sessionUserPrivilegeList) {
        this.sessionUserPrivilegeList = sessionUserPrivilegeList;
    }

    public SessionAccessToken getSessionAccessToken() {
        return sessionAccessToken;
    }

    public void setSessionAccessToken(SessionAccessToken sessionAccessToken) {
        this.sessionAccessToken = sessionAccessToken;
    }

    public static class SessionUser implements Serializable {

        private static final long serialVersionUID = 7899506655873113221L;
        private String userId;
        private String username;
        private String nickname;
        private Integer gender;
        private String idCard;
        private String birth;
        private String phone;
        private String email;
        private String wechat;
        private String weibo;
        private String thirdId;
        private ThirdType thirdType;
        private String avatar;
        private Integer status;
        private LocalDateTime createTime;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }

    public static class SessionUserPrivilege implements Serializable {

        private static final long serialVersionUID = -1103881126541372906L;
        private String privilegeId;
        private Integer status;

        public String getPrivilegeId() {
            return privilegeId;
        }

        public void setPrivilegeId(String privilegeId) {
            this.privilegeId = privilegeId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static class SessionAccessToken implements Serializable {

        private static final long serialVersionUID = 5390239197415170469L;
        private String token;
        private long maxAge;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }
}
