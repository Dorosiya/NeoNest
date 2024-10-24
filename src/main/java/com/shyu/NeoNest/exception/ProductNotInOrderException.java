package com.shyu.NeoNest.exception;

public class ProductNotInOrderException extends RuntimeException {

    public ProductNotInOrderException() {
    }

    public ProductNotInOrderException(String message) {
        super(message);
    }

    public ProductNotInOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotInOrderException(Throwable cause) {
        super(cause);
    }
}
