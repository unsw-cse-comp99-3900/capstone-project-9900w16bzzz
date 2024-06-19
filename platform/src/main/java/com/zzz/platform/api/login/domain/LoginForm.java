package com.zzz.platform.api.login.domain;

import com.zzz.platform.api.login.domain.captcha.CaptchaForm;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class LoginForm extends CaptchaForm {


    @Schema(description = "login name")
    @NotBlank(message = "login name cannot be null")
    @Length(max = 30, message = "maximum 30")
    private String loginName;

    @Schema(description = "password")
    @NotBlank(message = "password cannot be null")
    private String password;
}
