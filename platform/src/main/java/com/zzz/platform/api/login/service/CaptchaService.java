package com.zzz.platform.api.login.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zzz.platform.api.login.domain.captcha.CaptchaForm;
import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.domain.SystemEnvironment;
import com.zzz.platform.common.exception.BusinessException;
import com.zzz.platform.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Slf4j
@Service
public class CaptchaService {

    /**
     * expire time：65秒
     */
    private static final long EXPIRE_SECOND = 65L;

    @Resource
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private SystemEnvironment systemEnvironment;
    @Resource
    private CacheService cacheService;

    private final static String CAPTCHA_CACHE_KEY_NAME = "captcha_cache";

    /**
     * Generate graphical CAPTCHA
     *
     */
    public CaptchaVO generateCaptcha() {
        String captchaText = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(captchaText);

        String base64Code;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            base64Code = Base64Utils.encodeToString(os.toByteArray());
        } catch (Exception e) {
            log.error("generateCaptcha error:", e);
            throw new BusinessException("Generate CAPTCHA Error");
        }

        /*
         * Returns the CAPTCHA object
         * Image base64 format
         */
        String uuid = UUID.randomUUID().toString().replace("-", "");

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaUuid(uuid);
        captchaVO.setCaptchaBase64Image("data:image/png;base64," + base64Code);
        captchaVO.setExpireSeconds(EXPIRE_SECOND);
        if (!systemEnvironment.isProd()) {
            captchaVO.setCaptchaText(captchaText);
        }
        cacheService.saveKey(CAPTCHA_CACHE_KEY_NAME, uuid, captchaText);
        return captchaVO;
    }

    /**
     * checkCaptcha
     *
     */
    public ResponseDTO<String> checkCaptcha(CaptchaForm captchaForm) {
        if (StringUtils.isBlank(captchaForm.getCaptchaUuid()) || StringUtils.isBlank(captchaForm.getCaptchaCode())) {
            return ResponseDTO.userErrorParam("Please enter the correct code");
        }
        /*
         * 1、check captcha in cahce
         */
        String uuid = captchaForm.getCaptchaUuid();
        String redisCaptchaCode = cacheService.getValue(CAPTCHA_CACHE_KEY_NAME, uuid);
        if (StringUtils.isBlank(redisCaptchaCode)) {
            return ResponseDTO.userErrorParam("Captcha has expired, please refresh and try again.");
        }
        if (!Objects.equals(redisCaptchaCode, captchaForm.getCaptchaCode())) {
            return ResponseDTO.userErrorParam("The verification code is wrong, please enter the correct code.");
        }
        // Delete Used CAPTCHA
        cacheService.removeCache(uuid);
        return ResponseDTO.ok();
    }

}
