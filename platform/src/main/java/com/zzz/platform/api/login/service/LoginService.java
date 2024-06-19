package com.zzz.platform.api.login.service;

import com.zzz.platform.api.login.domain.LoginForm;
import com.zzz.platform.api.login.domain.LoginResultVO;
import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.common.domain.ResponseDTO;

import javax.annotation.Resource;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class LoginService {

    @Resource
    private CaptchaService captchaService;

    /**
     * get captcha
     * @return captcha
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

    public ResponseDTO<LoginResultVO> login(LoginForm loginForm, String ip, String userAgent) {

    }
}
