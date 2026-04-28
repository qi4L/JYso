package com.qi4l.JYso.exceptions;

@SuppressWarnings("unused")
public class UnSupportedActionTypeException extends RuntimeException {
    public UnSupportedActionTypeException() {
        super();
    }

    public UnSupportedActionTypeException(String message) {
        super(message);
    }
}
