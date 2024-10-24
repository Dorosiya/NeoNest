package com.shyu.NeoNest.exception;

public class DuplicateReviewException extends RuntimeException {

    public DuplicateReviewException() {
    }

    public DuplicateReviewException(String message) {
        super(message);
    }

    public DuplicateReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateReviewException(Throwable cause) {
        super(cause);
    }
}
