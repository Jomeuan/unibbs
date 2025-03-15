package com.jomeuan.unibbs.exception;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = -4574798572974879027L;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }
}
