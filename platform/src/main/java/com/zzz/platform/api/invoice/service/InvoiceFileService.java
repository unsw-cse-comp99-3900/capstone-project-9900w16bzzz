package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface InvoiceFileService {
    List<InvoiceEntity> searchFile(String fileName, BigInteger userId);

    ResponseDTO<byte[]> download(BigInteger invoiceId, String fileType);

    String searchFileNameById(BigInteger invoiceId, String fileType);

    void saveInvoiceContentInDB(BigInteger invoiceId, byte[] content, FileType filetype);

    ResponseDTO<String> validateInvoice(BigInteger invoiceId) throws IOException;
}
