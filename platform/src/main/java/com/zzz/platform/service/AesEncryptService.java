package com.zzz.platform.service;

import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Base64;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Slf4j
@Service
public class AesEncryptService {

    private static final String CHARSET = "UTF-8";

    private static final String AES_KEY = "9900zzz9900zzz99zzz00";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String encrypt(String data) {
        try {
            //  AES encrypt and encoder to base64
            AES aes = new AES(AES_KEY.getBytes(CHARSET));
            return aes.encryptBase64(data);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    public String decrypt(String data) {
        try {
            // first step： Base64 decoder
            byte[] base64Decode = Base64.getDecoder().decode(data);

            // second step： AES decrypt
            AES aes = new AES(AES_KEY.getBytes(CHARSET));
            byte[] decryptedBytes = aes.decrypt(base64Decode);
            return new String(decryptedBytes, CHARSET);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }
}
