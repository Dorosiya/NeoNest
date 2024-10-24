package com.shyu.NeoNest.exception;

public class DuplicationMemberNameException extends RuntimeException {

    public DuplicationMemberNameException() {
    }

    public DuplicationMemberNameException(String message) {
        super(message);
    }

    public DuplicationMemberNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationMemberNameException(Throwable cause) {
        super(cause);
    }
}
