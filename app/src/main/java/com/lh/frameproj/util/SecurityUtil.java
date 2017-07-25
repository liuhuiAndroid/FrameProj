package com.lh.frameproj.util;


import com.android.frameproj.library.util.log.Logger;
import com.lh.frameproj.Constants;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {


    /**
     * 对 String 进行HMAC-md5运算
     *
     * @param message 要进行HMAC-md5运算的数据 × @param str1 HMAC-md5密钥
     * @param key
     * @return 返回签名后的 byte 数组十六进制签名
     */

    public static String generateDigest(String message, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(
                key,
                "HmacMD5");

        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes(Charset.defaultCharset()));
        return new String(Hex.encodeHex(rawHmac)).toUpperCase();
    }

    /**
     * 对 String 进行HMAC-md5运算
     *
     * @param message 要进行HMAC-md5运算的数据 × @param str1 HMAC-md5密钥
     * @param key
     * @return 返回签名后的 byte 数组十六进制签名
     */

    public static String generateDigest(String message, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        return generateDigest(message, key.getBytes(Charset.defaultCharset()));
    }

    public static String generateDigest16(String message, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        return generateDigest(message, key).substring(8, 24);
    }

    public static String generateDigest16(String message, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        return generateDigest(message, key).substring(8, 24);
    }


    public static String getMd5(Map<String, Object> stringObjectMap, long currentTimeMillis) {
        HashMap<String, Object> stringObjectHashMap = MapSortUtil.sortMap(stringObjectMap);
        Iterator iterator = stringObjectHashMap.entrySet().iterator();
        String result = "";
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            result += key.toString();
            if(val instanceof int[]) {
                result += ((int[])val)[0];
            }else {
                result += val.toString();
            }
        }
        result += Constants.app_value;
        String generateDigest;
        Logger.i("result = " + result);
        Logger.i("currentTimeMillis = " + currentTimeMillis);
        try {
            generateDigest = generateDigest(result, currentTimeMillis + "");
        } catch (Exception e) {
            e.printStackTrace();
            generateDigest = "";
        }
        return generateDigest;
    }

}
