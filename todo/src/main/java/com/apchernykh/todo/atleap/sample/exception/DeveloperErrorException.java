package com.apchernykh.todo.atleap.sample.exception;

public class DeveloperErrorException extends ServerErrorException {

    @SuppressWarnings("unused")
    public DeveloperErrorException(String errorCode, String errorDetails) {
        super(errorCode, errorDetails);
    }
}
