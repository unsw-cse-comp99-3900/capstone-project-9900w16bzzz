package com.zzz.platform.api.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
@Data
public class InvoiceDeleteForm {

    @Schema(description = "invoice id")
    private BigInteger invoiceId;
}
