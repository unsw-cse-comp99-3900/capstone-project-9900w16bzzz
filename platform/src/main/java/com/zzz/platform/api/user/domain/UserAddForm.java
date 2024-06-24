package com.zzz.platform.api.user.domain;

import com.zzz.platform.utils.VerificationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
public class UserAddForm {

    @Schema(description = "login name", example = "zzz9900@unsw.edu.au")
    @NotNull(message = "not null")
    @Length(max = 30, message = "maximum length 30")
    @Email(regexp = VerificationUtil.EMAIL, message = "login name should be email address")
    private String loginName;

    @Schema(description = "user name")
    @NotNull(message = "not null")
    @Length(max = 30, message = "maximum length 30")
    private String userName;

    @Schema(description = "login pwd")
    @NotNull(message = "not null")
    private String loginPwd;
}
