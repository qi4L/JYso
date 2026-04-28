package com.qi4l.JYso.gadgets.utils.jre;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TCProxyClassDesc extends TCClassDesc implements SerializedElement {
    private final List<Class<?>> interfaces = new ArrayList<>();

    public void addInterface(Class<?> cls) {
        this.interfaces.add(cls);
    }

    public void doWrite(DataOutputStream out, HandleContainer handles) throws Exception {
        out.writeByte(125);
        out.writeInt(this.interfaces.size());
        for (Class<?> intf : this.interfaces)
            out.writeUTF(intf.getName());
        out.writeByte(120);
    }
}
