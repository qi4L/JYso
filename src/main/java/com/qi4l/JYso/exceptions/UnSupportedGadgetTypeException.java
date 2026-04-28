package com.qi4l.JYso.exceptions;

public class UnSupportedGadgetTypeException extends RuntimeException {
    @SuppressWarnings("unused")
    public UnSupportedGadgetTypeException() {
        super();
    }

    public UnSupportedGadgetTypeException(String message) {
        super(message);
    }
}
