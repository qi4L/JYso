package com.qi4l.JYso.gadgets.utils;

import sun.rmi.transport.TransportConstants;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class JRMPUtils {

    public static DataOutputStream handshake(DataInputStream in, Socket s) throws IOException {
        int magic = in.readInt();
        short version = in.readShort();
        if (magic != TransportConstants.Magic || version != TransportConstants.Version) {
            s.close();
            return null;
        }
        OutputStream sockOut = s.getOutputStream();
        BufferedOutputStream bufOut = new BufferedOutputStream(sockOut);
        return new DataOutputStream(bufOut);
    }
}
