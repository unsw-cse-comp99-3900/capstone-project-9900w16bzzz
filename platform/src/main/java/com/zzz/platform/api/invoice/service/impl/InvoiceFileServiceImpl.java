package com.zzz.platform.api.invoice.service.impl;

import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helger.ubl21.UBL21Writer;
import com.zzz.platform.api.invoice.converter.JsonToUblConverter;
import com.zzz.platform.api.invoice.dao.InvoiceFileDao;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.api.invoice.domain.api.EssInvoiceValidateForm;
import com.zzz.platform.api.invoice.entity.InvoiceFileEntity;
import com.zzz.platform.api.invoice.service.InvoiceApiService;
import com.zzz.platform.api.invoice.service.InvoiceDbService;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;
import lombok.extern.slf4j.Slf4j;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Service
public class InvoiceFileServiceImpl implements InvoiceFileService {

    @Autowired
    private InvoiceDbService invoiceDbServiceImpl;

    @Autowired
    private InvoiceFileDao invoiceFileDao;

    @Autowired
    private InvoiceApiService invoiceApiServiceImpl;


    @Override
    public ResponseDTO<byte[]> download(BigInteger invoiceId, FileType type) {
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
    public String searchFileNameById(BigInteger invoiceId, FileType fileType) {
        String fileName = invoiceDbServiceImpl.getFileNameById(invoiceId);
        return jointFileName(fileName, fileType);
    }

    public void saveInvoiceContentInDB(BigInteger invoiceId, byte[] content, FileType filetype) {
        // save file to DB
        InvoiceFileEntity invoiceFileEntity = new InvoiceFileEntity();
        invoiceFileEntity.setFileType(filetype.getValue());
        invoiceFileEntity.setInvoiceId(invoiceId);
        invoiceFileEntity.setContent(content);
        invoiceFileDao.insert(invoiceFileEntity);

        invoiceDbServiceImpl.updateFileFlag(invoiceId, FileType.XML, InvoiceDbService.FileStatusFlag.EXIST);
    }

    @Override
    public ResponseDTO<InvoiceJsonVO> searchInvoiceJsonById(BigInteger invoiceId) {
        InvoiceFileEntity entity = invoiceFileDao.selectOne(
                new QueryWrapper<InvoiceFileEntity>()
                        .eq("invoice_id", invoiceId)
                        .eq("file_type", FileType.JSON.getValue())
        );
        if (ObjectUtils.isEmpty(entity)) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        InvoiceJsonVO invoiceJsonVO = null;
        try {
            invoiceJsonVO = objectMapper.readValue(entity.getContent(), InvoiceJsonVO.class);
        } catch (IOException e) {
            log.error("objectMapper read value error", e);
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
        }
        return ResponseDTO.ok(invoiceJsonVO);
    }

    public ResponseDTO<ValidateResultVO> validateInvoice(BigInteger invoiceId, String rules) {
        InvoiceFileEntity entity = invoiceFileDao.selectOne(
                new QueryWrapper<InvoiceFileEntity>()
                        .eq("invoice_id", invoiceId)
                        .eq("file_type", FileType.XML.getValue())
        );
        if (ObjectUtils.isEmpty(entity)) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        // search json file from db
        byte[] bytes = entity.getContent();
        String fileName = invoiceDbServiceImpl.getFileNameById(invoiceId);

        return validate(bytes, fileName, rules);
    }

    @Override
    public ResponseDTO<ValidateResultVO> validateInvoice(BigInteger invoiceId, String rules, InvoiceJsonVO invoiceJsonVO) {
        String fileName = invoiceDbServiceImpl.getFileNameById(invoiceId);
        byte[] ublXml = convertJsonToUbl(invoiceJsonVO);
        // save invoice ubl in db
        saveInvoiceContentInDB(invoiceId, ublXml, FileType.XML);

        return validate(ublXml, fileName, rules);
    }

    private byte[] convertJsonToUbl(InvoiceJsonVO invoiceJsonVO) {
        InvoiceType invoiceType = JsonToUblConverter.convertToUbl(invoiceJsonVO);
        return UBL21Writer.invoice().getAsBytes(invoiceType);
    }

    private ResponseDTO<ValidateResultVO> validate(byte[] ublXml, String fileName, String rules) {
        EssInvoiceValidateForm essInvoiceValidateForm = new EssInvoiceValidateForm();
        essInvoiceValidateForm.setFileName(fileName);
        essInvoiceValidateForm.setContent(Base64.encode(ublXml));
        String md5sum = MD5Encoder.encode(ublXml);
        essInvoiceValidateForm.setChecksum(md5sum);
        essInvoiceValidateForm.setRules(rules);
        return invoiceApiServiceImpl.essValidateInvoice(essInvoiceValidateForm);
    }

    private String jointFileName(String fileName, FileType fileType) {
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

}
