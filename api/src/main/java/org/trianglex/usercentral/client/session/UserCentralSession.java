package org.trianglex.usercentral.client.session;

public class UserCentralSession {

    private SessionUser user = new SessionUser();
    private SessionUserPrivilege privilege = new SessionUserPrivilege();
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public SessionUser getUser() {
        return user;
    }

    public void setUser(SessionUser user) {
        this.user = user;
    }

    public SessionUserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(SessionUserPrivilege privilege) {
        this.privilege = privilege;
    }

}
