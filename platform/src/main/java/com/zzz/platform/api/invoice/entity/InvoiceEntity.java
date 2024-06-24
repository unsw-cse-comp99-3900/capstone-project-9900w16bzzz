package com.zzz.platform.api.invoice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: zhaoyue zhang
 * @version: v1.0.0
 * @date: 2024/6/24
 */
@Data
@TableName("t_file")
public class InvoiceEntity {

    @TableId(type = IdType.AUTO)
    private integer fileId;

    private String fileName;

    private Long userId ;

    private String fileContent;

    private Boolean fileValidation;
}
