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

    @Getter
    @AllArgsConstructor
    enum InvoiceDbColumn {
        USER_ID("user_id"),
        INVOICE_ID("invoice_id"),
        FILE_NAME("file_name"),
        CREATE_TIME("create_time"),
        UPDATE_TIME("update_time"),
        FLAG("flag");
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
}
