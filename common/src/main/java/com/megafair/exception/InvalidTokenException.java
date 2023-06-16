package com.megafair.exception;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 3329599032324779551L;

    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
