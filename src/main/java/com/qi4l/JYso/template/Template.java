package com.qi4l.JYso.template;

public interface Template {
    void generate();

    byte[] getBytes();

    String getClassName();
}
