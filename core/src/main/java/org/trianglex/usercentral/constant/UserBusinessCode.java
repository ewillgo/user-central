package org.trianglex.usercentral.constant;

import org.trianglex.common.exception.ApiCode;

import static org.trianglex.usercentral.constant.CommonCode.SUCCESS;

public enum UserBusinessCode implements ApiCode {

    USER_REGISTER_SUCCESS(SUCCESS.getStatus(), "账号注册成功"),
    USER_REGISTER_FAIL(100, "账号注册失败"),
    USER_LOGIN_SUCCESS(SUCCESS.getStatus(), "账号登录成功"),
    USER_NOT_EXISTS(101, "账号或密码错误"),
    USER_REPEAT(102, "注册信息有重复，请重新注册"),
    USER_NAME_EXISTS(103, "账号已被注册"),
    USER_NICKNAME_EXISTS(104, "昵称已被占用"),
    USER_PHONE_EXISTS(105, "联系电话已存在"),
    USER_LOGOUT_SUCCESS(SUCCESS.getStatus(), "注销会话成功"),
    USER_LOGOUT_FAIL(106, "会话不存在"),
    USER_INCORRECT_EMAIL(107, "邮件格式不正确"),
    USER_INCORRECT_IDCARD(108, "身份证格式不正确"),

    TICKET_VALIDATE_SUCCESS(SUCCESS.getStatus(), "票据校验成功"),
    TICKET_GENERATE_ERROR(109, "票据生成失败"),
    TICKET_INCORRECT(110, "非法票据"),
    TICKET_TIMEOUT(111, "票据已失效"),

    GLOBAL_SESSION_REFRESH_SUCCESS(SUCCESS.getStatus(), "全局会话刷新成功"),
    GLOBAL_SESSION_TIMEOUT(112, "全局会话超时"),
    ACCESS_TOKEN_INVALID(113, "令牌无效");

    private Integer status;
    private String message;

    UserBusinessCode(Integer status, String message) {
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
