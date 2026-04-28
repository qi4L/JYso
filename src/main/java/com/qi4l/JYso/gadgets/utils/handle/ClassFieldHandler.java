package com.qi4l.JYso.gadgets.utils.handle;

import com.qi4l.JYso.gadgets.utils.InjShell;
import javassist.CtClass;
import javassist.CtField;

public class ClassFieldHandler {
    public static void insertField(CtClass ctClass, String fieldName, String fieldCode) throws Exception {
        InjShell.insertField(ctClass, fieldName, fieldCode);
    }


    /**
     * 将 Field String 转一层，实际用处不大
     *
     * @param target Field String 值
     * @return 替换后的 String
     */
    public static String converString(String target) {
        return InjShell.converString(target);
    }

    /**
     * 只有在原 Class 已经有此 Field 的情况下才加入
     *
     * @param ctClass   CtClass
     * @param fieldName field 名称
     * @param fieldCode field 代码
     * @throws Exception 抛出异常
     */
    public static void insertFieldIfExists(CtClass ctClass, String fieldName, String fieldCode) throws Exception {
        ctClass.defrost();
        try {
            CtField field = ctClass.getDeclaredField(fieldName);
            ctClass.removeField(field);
            ctClass.addField(CtField.make(fieldCode, ctClass));
        } catch (javassist.NotFoundException ignored) {
        }
    }
}
