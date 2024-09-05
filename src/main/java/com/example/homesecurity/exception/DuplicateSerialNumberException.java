package com.example.homesecurity.exception;

public class DuplicateSerialNumberException extends RuntimeException{
    public DuplicateSerialNumberException() {
    }

    public DuplicateSerialNumberException(String message) {
        super(message);
    }

    public DuplicateSerialNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSerialNumberException(Throwable cause) {
        super(cause);
    }

    public DuplicateSerialNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
