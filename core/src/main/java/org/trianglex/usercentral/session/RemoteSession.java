package org.trianglex.usercentral.session;

public class RemoteSession {

    private UserCentralSession userCentralSession;
    private RemoteAccessToken remoteAccessToken;

    public RemoteAccessToken getRemoteAccessToken() {
        return remoteAccessToken;
    }

    public void setRemoteAccessToken(RemoteAccessToken remoteAccessToken) {
        this.remoteAccessToken = remoteAccessToken;
    }

    public UserCentralSession getUserCentralSession() {
        return userCentralSession;
    }

    public void setUserCentralSession(UserCentralSession userCentralSession) {
        this.userCentralSession = userCentralSession;
    }
}
