package org.trianglex.usercentral.client.constant;

import org.trianglex.common.support.ConstPair;

public interface ClientConstant {

    ConstPair ACCESS_TOCKEN_INVALIDATE = ConstPair.make(10000, "令牌无效");
    ConstPair ACCESS_TOCKEN_TIMEOUT = ConstPair.make(10001, "令牌超时");

}
