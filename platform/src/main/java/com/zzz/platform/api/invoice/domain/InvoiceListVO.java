package com.zzz.platform.api.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
@Data
public class InvoiceListVO {

    @Schema(description = "file uuid")
    private BigInteger invoiceId;

    @Schema(description = "file name")
    private String fileName;

    @Schema(description = "pdf flag")
    private Integer pdfFlag;

    @Schema(description = "json flag")
    private Integer jsonFlag;

    @Schema(description = "xml flag")
    private Integer xmlFlag;

    @Schema(description = "validation flag")
    private Integer validationFlag;

    @Schema(description = "update time")
    private LocalDateTime updateTime;

    @Schema(description = "create time")
    private LocalDateTime createTime;
}
