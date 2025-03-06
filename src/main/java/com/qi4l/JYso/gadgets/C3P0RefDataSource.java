package com.qi4l.JYso.gadgets;

public class C3P0RefDataSource implements ObjectPayload{
    @Override
    public Object getObject(String command) throws Exception {
//        Object obj = Reflections.createWithoutConstructor(Class.forName("com.mchange.v2.c3p0.JndiRefForwardingDataSource"));
//        // requires ordering
//        Reflections.setFieldValue(obj, "jndiName", command);
//        Reflections.setFieldValue(obj, "loginTimeout", 0);
//        return obj;
        return null;
    }
}
