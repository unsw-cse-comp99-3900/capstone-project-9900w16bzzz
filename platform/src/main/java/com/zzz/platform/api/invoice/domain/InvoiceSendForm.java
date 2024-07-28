package com.zzz.platform.api.invoice.domain;

import com.zzz.platform.utils.VerificationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceSendForm {

    @Schema(description = "target email")
    @NotNull
    @Email(regexp = VerificationUtil.EMAIL, message = "This field should be an email address")
    private String targetEmail;

    @Schema(description = "email subject")
    @Builder.Default
    private String subject = "[Eazy Invoice] Sending invoice file from Eazy Invoice";

    @Schema(description = "email body text")
    @Builder.Default
    private String text = "Find invoice file from attachment";
}
