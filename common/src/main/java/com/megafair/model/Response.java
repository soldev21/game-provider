package com.megafair.model;

public enum Response {
    SUCCESS(0, ""),
    ERROR(9999, "INTERNAL ERROR"),
    UNAUTHORIZED(1000, "UNAUTHORIZED"),
    INSUFFICIENT_FUNDS(1001, "INSUFFICIENT_FUNDS"),
    INVALID_AMOUNT(1001, "INVALID AMOUNT");

    Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;
}
