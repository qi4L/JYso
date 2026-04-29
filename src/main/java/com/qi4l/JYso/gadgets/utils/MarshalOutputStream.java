package com.qi4l.JYso.gadgets.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class MarshalOutputStream extends ObjectOutputStream {

    private final URL sendUrl;

    public MarshalOutputStream(OutputStream out, URL u) throws IOException {
        super(out);
        this.sendUrl = u;
    }

    public MarshalOutputStream(OutputStream out) throws IOException {
        super(out);
        this.sendUrl = null;
    }

    @Override
    protected void annotateClass(Class<?> cl) throws IOException {
        if (this.sendUrl != null) {
            writeObject(this.sendUrl.toString());
        } else if (!(cl.getClassLoader() instanceof URLClassLoader)) {
            writeObject(null);
        } else {
            URL[] us = ((URLClassLoader) cl.getClassLoader()).getURLs();
            StringBuilder cb = new StringBuilder();
            for (URL u : us) {
                cb.append(u.toString());
            }
            writeObject(cb.toString());
        }
    }

    @Override
    protected void annotateProxyClass(Class<?> cl) throws IOException {
        annotateClass(cl);
    }
}
