package com.zzz.platform.api.invoice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_invoice")
public class InvoiceEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private Long invoiceId;

    private Long userId;

    private String fileName;

    private Integer pdfFlag;

    private Integer jsonFlag;

    private Integer xmlFlag;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
