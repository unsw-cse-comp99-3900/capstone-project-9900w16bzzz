package com.zzz.platform.api.invoice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.platform.api.invoice.converter.InvoiceJsonDtoToVoConverter;
import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.domain.InvoiceApiJsonDTO;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.InvoiceUploadResultVO;
import com.zzz.platform.api.invoice.domain.api.UpbrainExtractorForm;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.InvoiceApiService;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.api.invoice.service.InvoiceUploadService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;
import com.zzz.platform.utils.VerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

@Slf4j
@Service
public class InvoiceUploadServiceImpl implements InvoiceUploadService {

    @Resource
    private InvoiceDao invoiceDao;

    @Resource
    private InvoiceApiService invoiceApiService;

    @Resource
    private InvoiceFileService invoiceFileService;

    @Resource
    private ObjectMapper objectMapper;


    @Override
    public ResponseDTO<InvoiceUploadResultVO> upload(BigInteger userId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        long mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
        BigInteger invoiceId = BigInteger.valueOf(mostSignificantBits);
        if (mostSignificantBits < 0) {
            invoiceId = invoiceId.add(BigInteger.ONE.shiftLeft(64));
        }
        // init invoice entity
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        // generate uuid
        invoiceEntity.setInvoiceId(invoiceId);
        invoiceEntity.setUserId(userId);
        invoiceEntity.setFileName(file.getOriginalFilename());

        InvoiceJsonVO invoiceJsonVO;
        FileType fileType = null;
        if (VerificationUtil.match(fileName, VerificationUtil.PDF_PATTERN)) {
            fileType = FileType.PDF;
            // invoke third api service
            InvoiceApiJsonDTO invoiceApiJsonDTO = convertPdfToJson(file);
            if (ObjectUtils.isEmpty(invoiceApiJsonDTO)) {
                return ResponseDTO.error(InvoiceErrorCode.UPBRAINSAI_API_REQUEST_FAILED);
            } else if(!invoiceApiJsonDTO.getDocumentType().equals("invoice")) {
                return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
            }
            invoiceJsonVO = InvoiceJsonDtoToVoConverter.convert(invoiceApiJsonDTO);
        } else if (VerificationUtil.match(fileName, VerificationUtil.JSON_PATTERN)) {
            fileType = FileType.JSON;
            try {
                invoiceJsonVO = objectMapper.readValue(file.getInputStream(), InvoiceJsonVO.class);
            } catch (IOException e) {
                log.error(e.getMessage());
                return ResponseDTO.error(InvoiceErrorCode.JSON_INVOICE_FORMAT_ERROR);
            }
        } else {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
        }
        invoiceDao.insert(invoiceEntity);
        // after insert new invoice entity
        saveInvoiceContentInDB(invoiceId, invoiceJsonVO);
        saveInvoiceContentInDB(invoiceId, file, fileType);

        InvoiceUploadResultVO invoiceUploadResultVO = new InvoiceUploadResultVO(invoiceId, invoiceJsonVO);
        return ResponseDTO.ok(invoiceUploadResultVO);
    }

    private void saveInvoiceContentInDB(BigInteger invoiceId, MultipartFile file, FileType fileType) {
        byte[] content = null;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            log.error("File get file bytes error, {}", e.getMessage());
        }
        invoiceFileService.saveInvoiceContentInDB(invoiceId, content, fileType);
    }

    private void saveInvoiceContentInDB(BigInteger invoiceId, InvoiceJsonVO invoiceJsonVO) {
        byte[] content = null;
        try {
            String jsonString = objectMapper.writeValueAsString(invoiceJsonVO);
            content = jsonString.getBytes();
        } catch (Exception e) {
            log.error("File get jsonVO bytes error, {}", e.getMessage());
        }
        invoiceFileService.saveInvoiceContentInDB(invoiceId, content, FileType.JSON);
    }

    private InvoiceApiJsonDTO convertPdfToJson(MultipartFile file) {
        UpbrainExtractorForm extractorForm = new UpbrainExtractorForm(file);
        ResponseDTO<InvoiceApiJsonDTO> responseDTO = invoiceApiService.convertPdfToJson(extractorForm);
        if (responseDTO.getOk()) {
            return responseDTO.getData();
        } else {
            return null;
        }
    }



}
