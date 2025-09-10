package com.example.demo.demos.web;

import java.io.Serializable;

/* loaded from: demo.jar:com/example/demo/demos/web/secCig.class */
public class secCig implements Serializable {
    private static final long serialVersionUID = 1;
    public String message;
    public Object secObject;

    public String toString() {
        return "secCig{message='" + this.message + "'}";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSecObject(Object secObject) {
        this.secObject = secObject;
    }
}