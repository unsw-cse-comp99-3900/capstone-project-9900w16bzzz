package com.zzz.platform.api.login.domain;

import com.zzz.platform.api.login.domain.captcha.CaptchaForm;
import com.zzz.platform.utils.VerificationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Data
public class LoginForm extends CaptchaForm {


    @Schema(description = "login name", example = "zzz9900@unsw.edu.au")
    @NotBlank(message = "login name cannot be null")
    @Length(max = 30, message = "maximum 30")
    @Email(regexp = VerificationUtil.EMAIL, message = "login name should be email address")
    private String loginName;

    @Schema(description = "login password")
    @NotBlank(message = "password cannot be null")
    private String loginPwd;
}
