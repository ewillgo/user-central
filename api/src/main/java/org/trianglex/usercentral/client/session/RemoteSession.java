package org.trianglex.usercentral.client.session;

public class RemoteSession {

    private RemoteAccessToken remoteAccessToken;
    private UserCentralSession userCentralSession;

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
