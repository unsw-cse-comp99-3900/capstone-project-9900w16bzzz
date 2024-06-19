package com.zzz.platform.api.login.controller;

import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.api.login.service.CaptchaService;
import com.zzz.platform.common.domain.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Tag(name = "captcha service")
@RestController
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @Operation(summary = "Get Graphical CAPTCHA")
    @GetMapping("/captcha")
    public ResponseDTO<CaptchaVO> generateCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

}
