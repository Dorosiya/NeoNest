package com.shyu.NeoNest.exception;

public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException() {
    }

    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOrderException(Throwable cause) {
        super(cause);
    }
}
