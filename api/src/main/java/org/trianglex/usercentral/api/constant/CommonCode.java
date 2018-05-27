package org.trianglex.usercentral.api.constant;

import org.trianglex.common.exception.ApiCode;

public enum CommonCode implements ApiCode {

    SUCCESS(0, "操作成功"),
    FAIL(-1, "操作失败"),
    SESSION_INVALID(-9999, "会话失效"),
    SESSION_TIMEOUT(-3, "获取会话超时，请重试");

    private Integer status;
    private String message;

    CommonCode(Integer status, String message) {
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
