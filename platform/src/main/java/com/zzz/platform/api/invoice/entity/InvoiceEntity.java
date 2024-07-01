package com.zzz.platform.api.invoice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_invoice")
public class InvoiceEntity {

    @TableId(type = IdType.AUTO)
    private Long fileId;

    private Long userId;

    private String fileName;

    private byte[] pdfContent;

    private byte[] jsonContent;

    private byte[] xmlContent;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
