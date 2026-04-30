package com.qi4l.JYso.web.config;

public class JYsoWebPasswordProvider {

    private static String password;

    public static void setPassword(String pwd) {
        password = pwd;
    }

    public static String getPassword() {
        return password;
    }
}
