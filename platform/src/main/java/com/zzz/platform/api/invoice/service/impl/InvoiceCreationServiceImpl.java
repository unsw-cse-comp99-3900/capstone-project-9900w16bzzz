package com.zzz.platform.api.invoice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.platform.api.invoice.dao.InvoiceDao;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.api.UpbrainExtractorForm;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.InvoiceApiService;
import com.zzz.platform.api.invoice.service.InvoiceCreationService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.utils.VerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Service
public class InvoiceCreationServiceImpl implements InvoiceCreationService {

    @Resource
    private InvoiceDao invoiceDao;

    @Resource
    private InvoiceApiService invoiceApiService;


    @Override
    public ResponseDTO<InvoiceJsonVO> upload(Long userId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // init invoice entity
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setUserId(userId);
        invoiceEntity.setFileName(file.getOriginalFilename());
        InvoiceJsonVO invoiceJsonVO;
        if (VerificationUtil.match(fileName, VerificationUtil.PDF_PATTERN)) {
            // add pdf file byte[] to entity
            try {
                invoiceEntity.setPdfContent(file.getBytes());
            } catch (IOException e) {
                log.error("Reading file error, message = {}", e.getMessage());
                return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
            }
            // invoke third api service
            invoiceJsonVO = convertPdfToJson(file);
            if (ObjectUtils.isEmpty(invoiceJsonVO)) {
                return ResponseDTO.error(InvoiceErrorCode.UPBRAINSAI_API_REQUEST_FAILED);
            }
        } else if (VerificationUtil.match(fileName, VerificationUtil.JSON_PATTERN)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                invoiceJsonVO = objectMapper.readValue(file.getInputStream(), InvoiceJsonVO.class);
            } catch (IOException e) {
                log.error(e.getMessage());
                return ResponseDTO.error(InvoiceErrorCode.JSON_INVOICE_FORMAT_ERROR);
            }
        } else {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
        }
        // save invoice in db
        saveInvoiceContentInDB(invoiceEntity, invoiceJsonVO);
        return ResponseDTO.ok(invoiceJsonVO);
    }

    private void saveInvoiceContentInDB(InvoiceEntity invoiceEntity, InvoiceJsonVO invoiceJsonVO) {
        // add json content to entity
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(invoiceJsonVO);
            byte[] jsonBytes = jsonString.getBytes();
            invoiceEntity.setJsonContent(jsonBytes);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        invoiceDao.insert(invoiceEntity);
    }

    private InvoiceJsonVO convertPdfToJson(MultipartFile file) {
        UpbrainExtractorForm extractorForm = new UpbrainExtractorForm(file);
        ResponseDTO<InvoiceJsonVO> responseDTO = invoiceApiService.convertPdfToJson(extractorForm);
        if (responseDTO.getOk()) {
            return responseDTO.getData();
        } else {
            return null;
        }
    }

}
