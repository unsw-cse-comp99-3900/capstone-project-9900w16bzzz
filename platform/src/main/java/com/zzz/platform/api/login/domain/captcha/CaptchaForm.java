package com.zzz.platform.api.login.domain.captcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Data
public class CaptchaForm {
    @Schema(description = "captcha")
    @NotBlank(message = "captcha cannot be null")
    private String captchaCode;

    @Schema(description = "uuid")
    @NotBlank(message = "uuid cannot be null")
    private String captchaUuid;
}
