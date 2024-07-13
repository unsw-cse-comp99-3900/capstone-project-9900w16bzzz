package com.zzz.platform.api.invoice.domain;

import com.zzz.platform.utils.VerificationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/13
 */
@Data
public class InvoiceSendForm {

    @Schema(description = "target email")
    @NotNull
    @Email(regexp = VerificationUtil.EMAIL, message = "login name should be email address")
    private String targetEmail;

    @Schema(description = "email subject")
    @NotNull
    private String subject;

    @Schema(description = "email body text")
    @NotNull
    private String text;
}
