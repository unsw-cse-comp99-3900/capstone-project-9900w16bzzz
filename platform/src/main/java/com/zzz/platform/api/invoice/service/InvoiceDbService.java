package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceDeleteForm;
import com.zzz.platform.api.invoice.domain.InvoiceListVO;
import com.zzz.platform.api.invoice.domain.InvoiceQueryForm;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
public interface InvoiceDbService {

    ResponseDTO<PageResult<InvoiceListVO>> list(InvoiceQueryForm invoiceQueryForm);

    ResponseDTO<String> delete(InvoiceDeleteForm deleteForm);

    InvoiceEntity findById(BigInteger invoiceId);

    String getFileNameById(BigInteger invoiceId);

    void updateFileFlag(BigInteger invoiceId, FileType fileType, FileStatusFlag flag);

    void updateValidationFlag(BigInteger invoiceId, ValidationFlag flag);

    @Getter
    @AllArgsConstructor
    enum InvoiceDbColumn {
        USER_ID("user_id"),
        INVOICE_ID("invoice_id"),
        FILE_NAME("file_name"),
        CREATE_TIME("create_time"),
        UPDATE_TIME("update_time"),
        FLAG("flag"),
        VALIDATION_FLAG("validation_flag");
        ;
        private final String val;
    }

    @Getter
    @AllArgsConstructor
    enum FileStatusFlag {
        EXIST(1),
        NOT_EXIST(0);
        ;
        private final int val;
    }

    @Getter
    @AllArgsConstructor
    enum ValidationFlag {
        SUCCESS(1, "Validation successful"),
        FAILED(2, "Validation failed"),
        NO_OPERATION(0,"No operation");
        ;
        private final int value;
        private final String desc;
    }
}
