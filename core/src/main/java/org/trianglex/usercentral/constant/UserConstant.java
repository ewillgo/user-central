package org.trianglex.usercentral.constant;

import org.trianglex.common.support.ConstPair;

public interface UserConstant {

    String SESSION_KEY = "user:session";

    String USERNAME_NOT_BLANK = "1000#账号不能为空";
    String PASSWORD_NOT_BLANK = "1001#密码不能为空";
    String NICKNAME_NOT_BLANK = "1002#昵称不能为空";
    String GENDER_NOT_NULL = "1003#性别不能为空";
    String CAPTCHA_NOT_NULL = "1004#验证码不能为空";

    ConstPair USER_REGISTER_SUCCESS = ConstPair.make(0, "注册用户成功");
    ConstPair USER_REGISTER_FAIL = ConstPair.make(100, "注册用户失败");

    ConstPair USER_LOGIN_SUCCESS = ConstPair.make(0, "用户登录成功");
    ConstPair USER_NOT_EXISTS = ConstPair.make(101, "账号或密码错误");
}
