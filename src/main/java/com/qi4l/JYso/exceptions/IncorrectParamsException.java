package com.qi4l.JYso.exceptions;

public class IncorrectParamsException extends RuntimeException {
    public IncorrectParamsException() {
        super();
    }

    public IncorrectParamsException(String message) {
        super(message);
    }
}
