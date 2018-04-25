package org.trianglex.usercentral.client.constant;

import org.trianglex.common.support.ConstPair;

public interface ClientConstant {

    String SESSION_KEY = "user:session";
    String COOKIE_NAME = "uctoken";

    ConstPair ACCESS_TOCKEN_INVALIDATE = ConstPair.make(100000, "令牌无效");
    ConstPair ACCESS_TOCKEN_TIMEOUT = ConstPair.make(100001, "令牌超时");

}
