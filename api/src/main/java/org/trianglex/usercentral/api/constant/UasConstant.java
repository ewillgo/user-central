package org.trianglex.usercentral.api.constant;

public interface UasConstant {

    String SESSION_USER = "session:user";

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
    String UUID_ERROR = "1015#用户ID不是合法的UUID/GUID";
    String SESSION_ID_NOT_BLANK = "1016#会话ID不能为空";
    String ACCESS_TOKEN_NOT_BLANK = "1017#令牌不能为空";

}
