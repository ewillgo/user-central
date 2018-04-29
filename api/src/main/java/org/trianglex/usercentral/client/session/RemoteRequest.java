package org.trianglex.usercentral.client.session;

import org.trianglex.common.dto.Result;
import org.trianglex.usercentral.session.UcSession;

import java.io.IOException;

public interface RemoteRequest {
    Result<UcSession> getRemoteSession(String accessTokenString) throws IOException;
}
