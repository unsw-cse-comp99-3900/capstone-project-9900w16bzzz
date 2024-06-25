package com.zzz.platform.api.invoice.service;

import com.zzz.platform.api.invoice.entity.InvoiceEntity;

import java.util.List;

public interface SearchFileService {
    List<InvoiceEntity> searchFile(String fileName, Long userId);
}
