package com.zzz.platform.api.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helger.ubl21.UBL21Writer;
import com.zzz.platform.api.invoice.converter.JsonToUblConverter;
import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.dao.InvoiceFileDao;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.entity.InvoiceFileEntity;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;
import com.zzz.platform.utils.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
public class InvoiceFileServiceImpl implements InvoiceFileService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InvoiceFileDao invoiceFileDao;

    @Override
    public List<InvoiceEntity> searchFile(String fileName, BigInteger userId) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setUserId(userId);

        return invoiceDao.searchFile(invoiceEntity);
    }

    @Override
    public ResponseDTO<byte[]> download(BigInteger invoiceId, String fileType) {
        FileType type = EnumUtil.getEnumByName(fileType, FileType.class);
        if (ObjectUtils.isEmpty(type)) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        InvoiceFileEntity invoiceFileEntity = invoiceFileDao.selectOne(
                new QueryWrapper<InvoiceFileEntity>()
                        .eq("invoice_id", invoiceId)
                        .eq("file_type", type.getValue()));
        if (ObjectUtils.isEmpty(invoiceFileEntity)) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        return ResponseDTO.ok(invoiceFileEntity.getContent());
    }

    @Override
    public String searchFileNameById(BigInteger invoiceId, String fileType) {
        InvoiceEntity invoiceEntity = invoiceDao.selectOne(
                new QueryWrapper<InvoiceEntity>().eq("invoice_id", invoiceId));
        if (ObjectUtils.isEmpty(invoiceEntity)) {
            return "";
        }
        String fileName = invoiceEntity.getFileName();

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

    public void saveInvoiceContentInDB(BigInteger invoiceId, byte[] content, FileType filetype) {
        // save file to DB
        InvoiceFileEntity invoiceFileEntity = new InvoiceFileEntity();
        invoiceFileEntity.setFileType(filetype.getValue());
        invoiceFileEntity.setInvoiceId(invoiceId);
        invoiceFileEntity.setContent(content);
        invoiceFileDao.insert(invoiceFileEntity);
    }

    @Override
    public ResponseDTO<String> validateInvoice(BigInteger invoiceId) throws IOException {
        InvoiceFileEntity entity = invoiceFileDao.selectOne(
                new QueryWrapper<InvoiceFileEntity>()
                        .eq("invoice_id", invoiceId)
                        .eq("file_type", FileType.JSON.getValue())
        );
        if (ObjectUtils.isEmpty(entity)) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        InvoiceJsonVO invoiceJsonVO;
        byte[] bytes = entity.getContent();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            invoiceJsonVO = objectMapper.readValue(bytes, InvoiceJsonVO.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseDTO.error(InvoiceErrorCode.JSON_INVOICE_FORMAT_ERROR);
        }

        InvoiceType invoiceType = JsonToUblConverter.convertToUbl(invoiceJsonVO);
        String ublXml = UBL21Writer.invoice().getAsString(invoiceType);
        return ResponseDTO.ok(ublXml);
    };

}
