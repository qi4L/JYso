package com.qi4l.JYso.gadgets.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class jdk17Bypass {
    private static final Logger log = LoggerFactory.getLogger(jdk17Bypass.class);

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>[] params) {
        Method method = null;
        while (clazz != null) {
            try {
                method = clazz.getDeclaredMethod(methodName, params);
                break;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return method;
    }

    private static Unsafe getUnsafe() {
        Unsafe unsafe;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        return unsafe;
    }

    public static void patchModule(Class<?> clazz, Class<?> goalclass) {
        try {
            Class<?> UnsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = UnsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            Object ObjectModule = Class.class.getMethod("getModule").invoke(goalclass);
            long addr = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
            unsafe.getAndSetObject(clazz, addr, ObjectModule);
        } catch (Exception ignored) {
        }
    }

    public void bypassModule(ArrayList<Class<?>> classes) {
        try {
            Unsafe unsafe = getUnsafe();
            Class<?> currentClass = this.getClass();
            try {
                Method getModuleMethod = getMethod(Class.class, "getModule", new Class[0]);
                if (getModuleMethod != null) {
                    for (Class<?> aClass : classes) {
                        Object targetModule = getModuleMethod.invoke(aClass);
                        unsafe.getAndSetObject(currentClass, unsafe.objectFieldOffset(Class.class.getDeclaredField("module")), targetModule);
                    }
                }
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}
