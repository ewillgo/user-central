package org.trianglex.usercentral.client.session;

import org.trianglex.common.dto.Result;

import java.io.IOException;

public interface RemoteRequest {
    Result<UserCentralSession> getRemoteSession(String accessTokenString) throws IOException;
}
