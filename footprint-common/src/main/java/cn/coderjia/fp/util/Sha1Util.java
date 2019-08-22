package cn.coderjia.fp.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author CoderJiA
 * @Description Sha1
 * @Date 7/8/2019 6:01 PM
 **/
public class Sha1Util {

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String hex(String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(srcStr.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }

}
