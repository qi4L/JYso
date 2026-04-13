package com.qi4l.JYso.gadgets.utils.jre;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandleContainer {
    private static final Logger log = LogManager.getLogger(HandleContainer.class);
    private static Method lookup;
    private static Method assign;

    static {
        try {
            Class<?> cls = Class.forName("java.io.ObjectOutputStream$HandleTable");
            assign = cls.getDeclaredMethod("assign", Object.class);
            assign.setAccessible(true);
            lookup = cls.getDeclaredMethod("lookup", Object.class);
            lookup.setAccessible(true);
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }

    private final Object handle;

    public HandleContainer(Object handle) {
        this.handle = handle;
    }

    public int getHandle(Object obj) {
        try {
            return (Integer) lookup.invoke(this.handle, new Object[]{obj});
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("e: ", e);
        }
        return -1;
    }

    public void putHandle(Object obj) {
        if (getHandle(obj) == -1)
            try {
                assign.invoke(this.handle, obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("e: ", e);
            }
    }
}
