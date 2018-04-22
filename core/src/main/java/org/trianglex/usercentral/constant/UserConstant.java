package org.trianglex.usercentral.constant;

import org.trianglex.common.support.ConstPair;

public interface UserConstant {

    int SUCCESS = 0;
    String SESSION_KEY = "user:session";

    String USERNAME_NOT_BLANK = "1000#账号不能为空";
    String PASSWORD_NOT_BLANK = "1001#密码不能为空";
    String NICKNAME_NOT_BLANK = "1002#昵称不能为空";
    String GENDER_NOT_NULL = "1003#性别不能为空";
    String USERID_NOT_NULL = "1004#用户ID不能为空";
    String USERID_NOT_UUID = "1005#用户ID不是UUID/GUID";

    ConstPair USER_REGISTER_SUCCESS = ConstPair.make(SUCCESS, "注册用户成功");
    ConstPair USER_REGISTER_FAIL = ConstPair.make(100, "注册用户失败");

    ConstPair USER_LOGIN_SUCCESS = ConstPair.make(SUCCESS, "用户登录成功");
    ConstPair USER_NOT_EXISTS = ConstPair.make(101, "账号或密码错误");
    ConstPair USER_REPEAT = ConstPair.make(102, "注册信息有重复，请重新注册");
    ConstPair USER_NAME_EXISTS = ConstPair.make(103, "账号已被注册");
    ConstPair USER_NICKNAME_EXISTS = ConstPair.make(104, "昵称已被占用");

    ConstPair GENERATE_TICKET_SUCCESS = ConstPair.make(SUCCESS, "生成票据成功");
    ConstPair GENERATE_TICKET_ERROR = ConstPair.make(105, "生成票据失败");
    ConstPair INCORRECT_TICKET = ConstPair.make(106, "非法票据");
    ConstPair TICKET_TIMEOUT = ConstPair.make(107, "票据已超时");

    ConstPair USER_LOGOUT_SUCCESS = ConstPair.make(SUCCESS, "注销会话成功");
    ConstPair USER_LOGOUT_FAIL = ConstPair.make(108, "会话不存在");

    ConstPair INCORRECT_ACCESS_TOKEN = ConstPair.make(109, "非法令牌");
    ConstPair ACCESS_TOKEN_TIMEOUT = ConstPair.make(110, "令牌已超时");
}
