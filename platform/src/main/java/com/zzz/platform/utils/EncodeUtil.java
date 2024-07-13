package com.zzz.platform.utils;

import cn.hutool.core.codec.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/13
 */
public class EncodeUtil {


    public static String md5Encode(String origin) {
        return md5Encode(origin.getBytes());
    }

    public static String md5Encode(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(bytes);

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.encode(bytes);
    }

    public static BigInteger truncateBigInteger(BigInteger bigInt) {
        String bigIntStr = bigInt.toString();

        if (bigIntStr.length() <= 5) {
            return BigInteger.ZERO;
        }
        // sub 5
        String truncatedStr = bigIntStr.substring(5);
        return new BigInteger(truncatedStr);
    }
}
