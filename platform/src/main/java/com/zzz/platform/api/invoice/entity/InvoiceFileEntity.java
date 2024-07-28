package com.zzz.platform.api.invoice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
@Data
@TableName("t_invoice_file")
public class InvoiceFileEntity {

    @TableId(type = IdType.AUTO)
    private BigInteger id;

    private BigInteger invoiceId;

    private int fileType;

    private byte[] content;
}
