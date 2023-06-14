package com.megafair.json;

public class JsonException extends RuntimeException {

    private static final long serialVersionUID = 8952270298894577371L;

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }
}
