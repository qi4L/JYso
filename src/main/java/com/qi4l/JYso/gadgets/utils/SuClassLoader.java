package com.qi4l.JYso.gadgets.utils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SuClassLoader extends ClassLoader {

    private final Set<String> skipParentClasses;

    public SuClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
        this.skipParentClasses = Collections.emptySet();
    }

    public SuClassLoader(ClassLoader parent) {
        super(parent);
        this.skipParentClasses = Collections.emptySet();
    }

    public SuClassLoader(ClassLoader parent, String skipParentClass) {
        super(parent);
        this.skipParentClasses = ConcurrentHashMap.newKeySet();
        this.skipParentClasses.add(skipParentClass);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (skipParentClasses.contains(name)) {
            Class<?> c = findLoadedClass(name);
            if (c != null) return c;
            c = findClass(name);
            if (resolve) resolveClass(c);
            return c;
        }
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
}
