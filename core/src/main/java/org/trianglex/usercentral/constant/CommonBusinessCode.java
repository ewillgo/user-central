package org.trianglex.usercentral.constant;

import org.trianglex.common.exception.BusinessCode;

public enum CommonBusinessCode implements BusinessCode {

    SUCCESS(0, "操作成功"),
    FAIL(-1, "操作失败");

    private Integer status;
    private String message;

    CommonBusinessCode(Integer status, String message) {
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
