package com.apchernykh.todo.atleap.sample.exception;

public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String detailMessage) {
        super(detailMessage);
    }

    public UnauthorizedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnauthorizedException(Throwable throwable) {
        super(throwable);
    }
}
