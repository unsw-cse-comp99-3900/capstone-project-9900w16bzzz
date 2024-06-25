package com.zzz.platform.api.invoice.service.impl;

import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.DeleteFileService;
import com.zzz.platform.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteFileServiceImpl implements DeleteFileService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public ResponseDTO<String> deleteFile(Integer fileId, Long userId) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setFileId(fileId);
        invoiceEntity.setUserId(userId);

        invoiceDao.deleteFile(invoiceEntity);  // 调用DAO接口的方法

        return ResponseDTO.ok("File deleted successfully");
    }
}
