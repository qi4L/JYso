package com.qi4l.JYso;

import com.qi4l.JYso.gadgets.Config.ysoserial;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.ObjectPayload;
import com.qi4l.JYso.web.JYsoWebApplication;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import static com.qi4l.JYso.gadgets.Config.Config.logo;

public class Starter {

    public static CaseInsensitiveMap<String, Class<? extends ObjectPayload<?>>> caseInsensitiveObjectPayloadMap = new CaseInsensitiveMap<>();
    public static boolean JYsoMode = false;

    static {
        for (Class<? extends ObjectPayload<?>> clazz : ObjectPayload.Utils.getPayloadClasses()) {
            caseInsensitiveObjectPayloadMap.put(clazz.getName(), clazz);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("-w")) {
            JYsoWebApplication.start(args);
            return;
        }

        if (args[0].equals("-j")) {
            logo();
            Config.applyCmdArgs(args);
            LdapServer.start();
            HTTPServer.start();
            if (Config.TLSProxy) {
                LdapsServer.start();
            }
            RMIServer.start();
        }

        if (args[0].equals("-y")) {
            JYsoMode = true;
            try {
                ysoserial.run(args);
            } catch (Exception e) {
                System.err.println("[!] " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
