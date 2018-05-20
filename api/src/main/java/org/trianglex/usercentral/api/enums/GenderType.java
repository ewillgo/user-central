package org.trianglex.usercentral.api.enums;

public enum GenderType {

    SECRET(0, "保密"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private Integer code;
    private String message;

    GenderType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
