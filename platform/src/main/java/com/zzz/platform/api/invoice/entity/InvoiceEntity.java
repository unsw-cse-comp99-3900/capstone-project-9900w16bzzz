package com.zzz.platform.api.invoice.entity;

import lombok.Data;

@Data
public class InvoiceEntity {
    private Integer fileId;
    private String fileName;
    private Long userId;
    private String fileContent;
    private Byte fileValidation;
}
