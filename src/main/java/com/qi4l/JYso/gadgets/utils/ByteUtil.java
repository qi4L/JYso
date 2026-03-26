package com.qi4l.JYso.gadgets.utils;

public class ByteUtil {
    public static int getSubarrayIndex(byte[] haystack, byte[] needle) {
        outer:
        for (int i = 0; i <= haystack.length - needle.length; ++i) {
            for (int j = 0; j < needle.length; ++j) {
                if (haystack[i + j] != needle[j]) {
                    continue outer;
                }
            }
            return i;
        }

        return -1;
    }

    public static byte[] deleteAt(byte[] bs, int index) {
        int length = bs.length - 1;
        byte[] ret = new byte[length];

        if (index == bs.length - 1) {
            System.arraycopy(bs, 0, ret, 0, length);
        } else if (index < bs.length - 1) {
            for (int i = index; i < length; i++) {
                bs[i] = bs[i + 1];
            }

            System.arraycopy(bs, 0, ret, 0, length);
        }

        return ret;
    }

}
