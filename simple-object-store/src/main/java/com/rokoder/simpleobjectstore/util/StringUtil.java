package com.rokoder.simpleobjectstore.util;

public class StringUtil {

    private StringUtil() {
        // Util class no constructor
    }

    /**
     * Converts bytes array to hex string representation.
     * <p/>
     * <pre>
     *     Example: In = 0,2,4,8,16,32,64,255
     *              Out = 00 02 04 08 10 20 40 ff
     * </pre>
     *
     * @param bytes
     * @return
     */
    public static String byteArrayToHexString(byte[] bytes) {

        if (bytes == null) return null;

        if (bytes.length == 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            sb.append(String.format("%02x", b));

            if (i < bytes.length - 1) sb.append(" ");
        }

        return sb.toString();
    }
}
