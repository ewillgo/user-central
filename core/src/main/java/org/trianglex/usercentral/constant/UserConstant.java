package org.trianglex.usercentral.constant;

import org.trianglex.common.support.ConstPair;

public interface UserConstant {

    int SUCCESS = 0;
    String SESSION_USER = "SESSION:USER";

    String USERNAME_NOT_BLANK = "1000#账号不能为空";
    String PASSWORD_NOT_BLANK = "1001#密码不能为空";
    String NICKNAME_NOT_BLANK = "1002#昵称不能为空";
    String GENDER_NOT_NULL = "1003#性别不能为空";
    String TICKET_NOT_BLANK = "1004#票据不能为空";

    String PAGE_NO_NOT_NULL = "1005#当前页码不能为空";
    String PAGE_SIZE_NOT_NULL = "1006#每页大小不能为空";
    String POSITIVE = "1007#数字必须大于等于1";
    String FIELDS_NOT_BLANK = "1008#请填写需要查询的字段";
    String USER_ID_NOT_BLANK = "1009#用户ID不能为空";
    String USER_ID_IS_UUID = "1010#用户ID必须是UUID";
    String UPDATE_DATA_NOT_NULL = "1011#请填写需要更新的字段";
    String EMAIL_INCORRECT = "1012#电子邮箱格式不正确";
    String ID_CARD_INCORRECT = "1013#身份证号格式不正确";
    String PHONE_INCORRECT = "1013#手机号码格式不正确";
    String NICKNAME_INCORRECT = "1014#昵称由中文、字母和数字组成";


    ConstPair USER_REGISTER_SUCCESS = ConstPair.make(SUCCESS, "账号注册成功");
    ConstPair USER_REGISTER_FAIL = ConstPair.make(100, "账号注册失败");
    ConstPair USER_LOGIN_SUCCESS = ConstPair.make(SUCCESS, "账号登录成功");
    ConstPair USER_NOT_EXISTS = ConstPair.make(101, "账号或密码错误");
    ConstPair USER_REPEAT = ConstPair.make(102, "注册信息有重复，请重新注册");
    ConstPair USER_NAME_EXISTS = ConstPair.make(103, "账号已被注册");
    ConstPair USER_NICKNAME_EXISTS = ConstPair.make(104, "昵称已被占用");
    ConstPair USER_PHONE_EXISTS = ConstPair.make(105, "联系电话已存在");
    ConstPair USER_LOGOUT_SUCCESS = ConstPair.make(SUCCESS, "注销会话成功");
    ConstPair USER_LOGOUT_FAIL = ConstPair.make(106, "会话不存在");
    ConstPair USER_INCORRECT_EMAIL = ConstPair.make(107, "邮件格式不正确");
    ConstPair USER_INCORRECT_IDCARD = ConstPair.make(108, "身份证格式不正确");

    ConstPair TICKET_VALIDATE_SUCCESS = ConstPair.make(SUCCESS, "票据校验成功");
    ConstPair TICKET_GENERATE_ERROR = ConstPair.make(109, "票据生成失败");
    ConstPair TICKET_INCORRECT = ConstPair.make(110, "非法票据");
    ConstPair TICKET_TIMEOUT = ConstPair.make(111, "票据已失效");

    ConstPair GLOBAL_SESSION_REFRESH_SUCCESS = ConstPair.make(SUCCESS, "全局会话刷新成功");
    ConstPair GLOBAL_SESSION_TIMEOUT = ConstPair.make(112, "全局会话超时");

    ConstPair ACCESS_TOKEN_INVALID = ConstPair.make(113, "令牌无效");

}
