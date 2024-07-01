package com.zzz.platform.api.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.BaseEnum;
import com.zzz.platform.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceFileServiceImpl implements InvoiceFileService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public List<InvoiceEntity> searchFile(String fileName, Long userId) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setUserId(userId);

        return invoiceDao.searchFile(invoiceEntity);
    }

    @Override
    public ResponseDTO<byte[]> download(Long fileId, String fileType) {
        String columnName = EnumUtil.getEnumDescByValue(fileType, FileType.class);
        List<Object> objs = invoiceDao.selectObjs(
                new QueryWrapper<InvoiceEntity>().eq("file_id", fileId).select(columnName)
        );
        if (objs.isEmpty()) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        return ResponseDTO.ok((byte[]) objs.get(0));
    }

    @Override
    public String searchFileNameById(Long fileId, String fileType) {
        List<Object> objs = invoiceDao.selectObjs(
                new QueryWrapper<InvoiceEntity>().eq("file_id", fileId).select("file_name")
        );
        if (objs.isEmpty()) {
            return "";
        }
        String fileName = objs.get(0).toString();

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // add file suffix
            fileName += "." + fileType;
        } else {
            // replace file suffix
            fileName = fileName.substring(0, lastDotIndex) + "." + fileType;
        }
        return fileName;
    }

    @AllArgsConstructor
    @Getter
    enum FileType implements BaseEnum{
        JSON("json", "json_content"),
        XML("xml","xml_content"),
        PDF("pdf","pdf_content");
        ;
        final String value;
        // column name in database
        final String desc;
    }
}
