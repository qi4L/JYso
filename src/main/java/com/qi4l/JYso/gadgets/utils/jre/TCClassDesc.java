package com.qi4l.JYso.gadgets.utils.jre;

import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TCClassDesc extends ReferencableObject implements SerializedElement {
    private String className;

    private long serialVersionUID;

    private byte classDescFlags;

    private final List<Field> fields = new ArrayList<>();

    protected TCClassDesc() {
    }

    public TCClassDesc(String className) throws Exception {
        this(className, -1L, (byte) 0);
    }

    public TCClassDesc(String className, byte classDescFlags) throws Exception {
        this(className, -1L, classDescFlags);
    }

    public TCClassDesc(String className, long serialVersionUID, byte classDescFlags) throws Exception {
        this.className = className;
        this.serialVersionUID = (serialVersionUID != -1L) ? serialVersionUID : getSerialVersionUID();
        this.classDescFlags = (classDescFlags != 0) ? classDescFlags : getClassDescFlags();
    }

    public int getFieldsCount() {
        return this.fields.size();
    }

    private long getSerialVersionUID() throws Exception {
        Class<?> cls = Class.forName(this.className);
        java.lang.reflect.Field f = cls.getDeclaredField("serialVersionUID");
        f.setAccessible(true);
        return Long.parseLong(f.get((Object) null).toString());
    }

    private byte getClassDescFlags() throws Exception {
        Class<?> cls = Class.forName(this.className);
        byte b = 0;
        if (Serializable.class.isAssignableFrom(cls))
            b = (byte) (b | 0x2);
        try {
            b = (byte) (b | 0x1);
        } catch (Exception ignored) {
        }
        return b;
    }

    public boolean hasWriteObject() {
        return ((this.classDescFlags & 0x1) != 0);
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public void write(DataOutputStream out, HandleContainer handles) throws Exception {
        if (handles.getHandle(this) != -1) {
            TCReference reference = new TCReference(handles.getHandle(this));
            reference.write(out, handles);
            throw new Exception("stop");
        }
        super.write(out, handles);
    }

    public void doWrite(DataOutputStream out, HandleContainer handles) throws Exception {
        out.writeByte(114);
        out.writeUTF(this.className);
        out.writeLong(this.serialVersionUID);
        out.writeByte(this.classDescFlags);
        out.writeShort(this.fields.size());
        handles.putHandle(getHandleObject());
        for (Field field : this.fields)
            field.write(out, handles);
        out.writeByte(120);
    }

    public static class Field implements SerializedElement {

        private final String name;

        private final Class<?> type;

        public Field(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        private byte getTypeByte() {
            Map<Class<?>, Byte> bytes = new HashMap<>();
            bytes.put(byte.class, (byte) 66);
            bytes.put(char.class, (byte) 67);
            bytes.put(double.class, (byte) 68);
            bytes.put(float.class, (byte) 70);
            bytes.put(int.class, (byte) 73);
            bytes.put(long.class, (byte) 74);
            bytes.put(short.class, (byte) 83);
            bytes.put(boolean.class, (byte) 90);
            Byte b = bytes.get(this.type);
            if (b == null)
                b = (byte) 76;
            return b;
        }

        public void write(DataOutputStream out, HandleContainer handles) throws Exception {
            byte b = getTypeByte();
            out.writeByte(b);
            out.writeUTF(this.name);
            if (b == 76) {
                TCString s = TCString.getInstance((char) b + this.type.getName().replace('.', '/') + ";");
                s.write(out, handles);
            }
        }
    }
}
