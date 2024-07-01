package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.common.domain.ResponseDTO;

import java.util.List;

public interface InvoiceFileService {
    List<InvoiceEntity> searchFile(String fileName, Long userId);

    ResponseDTO<byte[]> download(Long fileId, String fileType);

    String searchFileNameById(Long fileId, String fileType);
}
