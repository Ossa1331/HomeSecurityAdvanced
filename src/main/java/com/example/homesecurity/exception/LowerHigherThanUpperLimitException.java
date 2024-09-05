package com.example.homesecurity.exception;

public class LowerHigherThanUpperLimitException extends RuntimeException{
    public LowerHigherThanUpperLimitException() {
    }

    public LowerHigherThanUpperLimitException(String message) {
        super(message);
    }

    public LowerHigherThanUpperLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LowerHigherThanUpperLimitException(Throwable cause) {
        super(cause);
    }

    public LowerHigherThanUpperLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
