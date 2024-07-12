package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;

import java.math.BigInteger;

public interface InvoiceFileService {

    ResponseDTO<byte[]> download(BigInteger invoiceId, FileType fileType);

    String searchFileNameById(BigInteger invoiceId, FileType fileType);

    void saveInvoiceContentInDB(BigInteger invoiceId, byte[] content, FileType filetype);

    ResponseDTO<InvoiceJsonVO> searchInvoiceJsonById(BigInteger invoiceId);

    ResponseDTO<ValidateResultVO> validateInvoice(BigInteger invoiceId, String rules, InvoiceJsonVO invoiceJsonVO);

    ResponseDTO<ValidateResultVO> validateInvoice(BigInteger invoiceId, String rules);
}
