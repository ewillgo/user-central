package org.trianglex.usercentral.client.session;

public interface RemoteRequest {
    UserCentralSession getRemoteSession(String accessTokenString);
}
