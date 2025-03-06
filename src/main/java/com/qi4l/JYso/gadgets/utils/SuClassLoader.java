package com.qi4l.JYso.gadgets.utils;

public class SuClassLoader extends ClassLoader {

    public SuClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }
}
