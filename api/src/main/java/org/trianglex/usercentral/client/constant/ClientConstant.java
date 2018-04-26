package org.trianglex.usercentral.client.constant;

import org.trianglex.common.support.ConstPair;

import java.time.Duration;

public interface ClientConstant {

    String SESSION_KEY = "user:session";
    String COOKIE_NAME = "uctoken";
    Duration COOKIE_DURATION = Duration.ofHours(2);

    ConstPair ACCESS_TOCKEN_INVALIDATE = ConstPair.make(100000, "令牌无效");
    ConstPair ACCESS_TOCKEN_EXCEPTION = ConstPair.make(100001, "获取令牌异常");

}
