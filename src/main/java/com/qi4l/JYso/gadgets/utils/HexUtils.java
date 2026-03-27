package com.qi4l.JYso.gadgets.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class HexUtils {

    public static byte[] toByteArray(InputStream in) throws IOException {
        byte[] classBytes;
        classBytes = new byte[in.available()];
        int bytesRead = in.read(classBytes);
        if (bytesRead == -1) {
            throw new EOFException("流已结束，未读取到数据");
        }
        in.close();
        return classBytes;
    }

    public static String bytesToHexString(byte[] bArray, int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
