package com.qi4l.JYso.exceptions;

public class UnSupportedPayloadTypeException extends RuntimeException {
    public UnSupportedPayloadTypeException() {
        super();
    }

    public UnSupportedPayloadTypeException(String message) {
        super(message);
    }
}
