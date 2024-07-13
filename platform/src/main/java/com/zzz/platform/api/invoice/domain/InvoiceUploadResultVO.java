package com.zzz.platform.api.invoice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/13
 */
@AllArgsConstructor
@Data
public class InvoiceUploadResultVO {

    private BigInteger invoiceId;

    private InvoiceJsonVO invoiceJsonVO;
}
