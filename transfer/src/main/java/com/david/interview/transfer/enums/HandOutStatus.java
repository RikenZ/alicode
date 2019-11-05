package com.david.interview.transfer.enums;

public enum HandOutStatus {

    PROCESSING(0, "派发中"),
    EXPIRED(1, "超时"),
    COMPLETE(2, "完成");

    private final int code;
    private final String value;

    HandOutStatus(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public String value() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
