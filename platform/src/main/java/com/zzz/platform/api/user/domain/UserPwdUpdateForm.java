package com.zzz.platform.api.user.domain;

import com.zzz.platform.utils.VerificationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
public class UserPwdUpdateForm {

    @Schema(hidden = true)
    private Long userId;

    @Schema(description = "old password")
    @NotBlank(message = "not null")
    @Pattern(regexp = VerificationUtil.PWD_REGEXP, message = "6-15 digits (include numbers, upper and lower case letters or decimal points)")
    private String oldPassword;

    @Schema(description = "new password")
    @NotBlank(message = "not null")
    @Pattern(regexp = VerificationUtil.PWD_REGEXP, message = "6-15 digits (include numbers, upper and lower case letters or decimal points)")
    private String newPassword;
}
