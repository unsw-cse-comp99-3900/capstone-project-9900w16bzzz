package com.zzz.platform.api.invoice.service.impl;

import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.domain.UploadFileForm;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.UploadFileService;
import com.zzz.platform.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public ResponseDTO<String> uploadFile(UploadFileForm uploadFileForm, Long userId) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setFileName(uploadFileForm.getFileName());
        invoiceEntity.setUserId(userId);
        invoiceEntity.setFileContent(uploadFileForm.getFileContent());

        invoiceDao.addFile(invoiceEntity);  // 调用DAO接口的方法

        return ResponseDTO.ok("File uploaded successfully");
    }
}
