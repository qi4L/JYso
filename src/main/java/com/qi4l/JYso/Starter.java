package com.qi4l.JYso;

import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.gadgets.ObjectPayload;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import static com.qi4l.JYso.controllers.ysoserial.ysoserial;

public class Starter {

    public static CaseInsensitiveMap<String,Class<? extends ObjectPayload>> caseInsensitiveObjectPayloadMap = new CaseInsensitiveMap();
    static {
        for (Class<? extends ObjectPayload> clazz : ObjectPayload.Utils.getPayloadClasses()) {
            caseInsensitiveObjectPayloadMap.put(clazz.getName(), clazz);
        }
    }

    public static boolean JYsoMode = false;

    public static void main(String[] args) throws Exception {
        String logo = "" +
                " ┏┳┓┏    \n" +
                "  ┃┗┫┏┏┓ \n" +
                " ┗┛┗┛┛┗┛ version: 1.33";
        System.out.println(logo);
        if (args.length > 0 && args[0].equals("-j")) {
            Config.applyCmdArgs(args);
            if (Config.TLSProxy) {
                TLSProxy.start();
            }
            LdapServer.start();
            HTTPServer.start();
            RMIServer.start();
        }
        if (args.length > 0 && args[0].equals("-y")) {
            JYsoMode = true;
            ysoserial(args);
        }
    }
}
