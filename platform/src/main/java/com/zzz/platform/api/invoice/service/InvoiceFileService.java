package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;

import java.util.List;

public interface InvoiceFileService {
    List<InvoiceEntity> searchFile(String fileName, Long userId);

    ResponseDTO<byte[]> download(Long invoiceId, String fileType);

    String searchFileNameById(Long invoiceId, String fileType);

    void saveInvoiceContentInDB(Long invoiceId, byte[] content, FileType filetype);
}
