package com.zzz.platform.api.invoice.service.impl;

import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.SearchFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchFileServiceImpl implements SearchFileService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public List<InvoiceEntity> searchFile(String fileName, Long userId) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setFileName(fileName);
        invoiceEntity.setUserId(userId);

        return invoiceDao.searchFile(invoiceEntity);
    }
}
