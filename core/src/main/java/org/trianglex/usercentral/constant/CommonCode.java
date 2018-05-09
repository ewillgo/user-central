package org.trianglex.usercentral.constant;

import org.trianglex.common.exception.ApiCode;

public enum CommonCode implements ApiCode {

    SUCCESS(0, "操作成功"),
    FAIL(-1, "操作失败");

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
