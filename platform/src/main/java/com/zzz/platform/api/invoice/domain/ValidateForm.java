package com.zzz.platform.api.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/22
 */
@Data
public class ValidateForm {

    @Schema(description = "customer")
    @NotBlank(message = "customer name cannot be null")
    private String customer;

    @Schema(description = "authentication rule (multiple rules please use ',' to split)", example = "AUNZ_UBL_1_0_10[,AUNZ_PEPPOL_1_0_10]")
    @NotBlank(message = "rules cannot be null")
    private String rules;

    @Schema(description = "file name")
    @NotBlank(message = "file name cannot be null")
    private String filename;

    @Schema(description = "file content using base64 encode")
    @NotBlank(message = "file content cannot be null")
    private String content;

    @Schema(description = "count md5 for file content ")
    @NotBlank(message = "md5sum cannot be null")
    private String md5sum;
}
