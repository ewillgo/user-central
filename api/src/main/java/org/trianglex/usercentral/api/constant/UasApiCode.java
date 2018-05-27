package org.trianglex.usercentral.api.constant;

import org.trianglex.common.exception.ApiCode;

public enum UasApiCode implements ApiCode {

    USER_REGISTER_FAIL(600, "账号注册失败"),
    USER_NOT_EXISTS(601, "账号或密码错误"),
    USER_REPEAT(602, "注册信息有重复，请重新注册"),
    USER_NAME_EXISTS(603, "账号已被注册"),
    USER_NICKNAME_EXISTS(604, "昵称已被占用"),
    USER_PHONE_EXISTS(605, "联系电话已存在"),
    USER_LOGOUT_FAIL(606, "会话不存在"),
    USER_INCORRECT_EMAIL(607, "邮件格式不正确"),
    USER_INCORRECT_IDCARD(608, "身份证格式不正确"),
    TICKET_GENERATE_ERROR(609, "票据生成失败"),
    TICKET_INCORRECT(610, "非法票据"),
    TICKET_TIMEOUT(611, "票据已失效"),
    GLOBAL_SESSION_TIMEOUT(612, "全局会话超时"),
    ACCESS_TOKEN_INVALID(613, "令牌无效"),
    APP_SECRET_NOT_FOUND(614, "获取不到应用密钥"),
    ACCESS_TOKEN_GENERATE_ERROR(615, "令牌生成失败");

    private Integer status;
    private String message;

    UasApiCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
