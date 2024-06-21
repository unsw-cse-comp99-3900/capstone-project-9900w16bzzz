package com.zzz.platform.api.login.service;

import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.service.AesEncryptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Service
public class ProtectedPasswordService {


    /**
     * Passwords must be 8-20 digits in length and contain uppercase letters, lowercase letters, and numbers.
     */
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$";

    /**
     * Passwords must be 8-20 digits in length and contain uppercase letters, lowercase letters, and numbers.
     */
    public static final String PASSWORD_FORMAT_MSG = "Passwords must be 8-20 digits in length and contain uppercase letters, lowercase letters, and numbers.";


    private static final int PASSWORD_LENGTH = 8;

    @Resource
    private AesEncryptService aesEncryptService;

    /**
     * validate password not empty
     *
     * @return
     */
    public ResponseDTO<String> validatePassComplexity(String password) {
        if (StringUtils.isEmpty(password)) {
            return ResponseDTO.userErrorParam(PASSWORD_FORMAT_MSG);
        }

        return ResponseDTO.ok();
    }

    /**
     * Decrypt AES encrypted passwords
     *
     * @param encryptedPassword
     * @return
     */
    public String decryptPassword(String encryptedPassword) {
        return aesEncryptService.decrypt(encryptedPassword);
    }


}
