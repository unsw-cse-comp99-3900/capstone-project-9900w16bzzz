package com.zzz.platform.api.invoice.domain.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/1
 */
@Data
public class EssInvoiceValidateForm {

    @Schema(description = "customer name")
    private String customer;

    @Schema(description = "validate rules, using ',' to split")
    private String rules;

    @Schema(description = "file name")
    private String fileName;

    @Schema(description = "file content")
    private String content;

    @Schema(description = "file md5 value")
    private String checksum;

}
