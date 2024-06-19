package com.zzz.platform.api.login.domain.captcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Data
public class CaptchaVO {

    @Schema(description = "uuid")
    private String captchaUuid;

    @Schema(description = "CAPTCHA image content - invalid for production environment")
    private String captchaText;

    @Schema(description = "CAPTCHA Base64 image")
    private String captchaBase64Image;

    @Schema(description = "expire time (second)")
    private Long expireSeconds;
}
