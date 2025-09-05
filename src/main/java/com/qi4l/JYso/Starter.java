package com.qi4l.JYso;

import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.ObjectPayload;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import static com.qi4l.JYso.controllers.ysoserial.ysoserial;
import static com.qi4l.JYso.gadgets.Config.Config.logo;

public class Starter {

    // 用于存储所有的ObjectPayload类
    public static CaseInsensitiveMap<String,Class<? extends ObjectPayload>> caseInsensitiveObjectPayloadMap = new CaseInsensitiveMap();
    static {
        for (Class<? extends ObjectPayload> clazz : ObjectPayload.Utils.getPayloadClasses()) {
            caseInsensitiveObjectPayloadMap.put(clazz.getName(), clazz);
        }
    }

    public static boolean JYsoMode = false;

    public static void main(String[] args) throws Exception {
        // 如果参数中包含-j，则启动LDAP、HTTP、RMI服务
        if (args.length > 0 && args[0].equals("-j")) {
            logo();
            Config.applyCmdArgs(args);
            LdapServer.start();
            HTTPServer.start();
            if (Config.TLSProxy) {
                TLSProxy.start();
            } else {
                //RMIServer.start();
            }
        }

        // 如果参数中包含-y，则启动 ysuserial
        if (args.length > 0 && args[0].equals("-y")) {
            JYsoMode = true;
            ysoserial(args);
        }
    }
}
