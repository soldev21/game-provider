package com.megafair.exception;

public class InvalidSessionTokenException extends RuntimeException {

    private static final long serialVersionUID = 332939232324779551L;

    public InvalidSessionTokenException() {
        super();
    }

    public InvalidSessionTokenException(String message) {
        super(message);
    }

    public InvalidSessionTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
