package com.megafair.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 3329599034923779551L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
