package com.david.interview.transfer.enums;

public enum TransferStatus {

    PROCESSING(0, "处理中"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    private final int code;
    private final String value;

    TransferStatus(int code, String value) {
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
