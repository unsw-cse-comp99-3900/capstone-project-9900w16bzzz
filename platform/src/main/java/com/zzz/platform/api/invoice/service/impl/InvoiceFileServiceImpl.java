package com.zzz.platform.api.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helger.ubl21.UBL21Writer;
import com.zzz.platform.api.invoice.converter.JsonToUblConverter;
import com.zzz.platform.api.invoice.dao.InvoiceFileDao;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.InvoiceSendForm;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.api.invoice.domain.api.EssInvoiceValidateForm;
import com.zzz.platform.api.invoice.entity.InvoiceFileEntity;
import com.zzz.platform.api.invoice.service.InvoiceApiService;
import com.zzz.platform.api.invoice.service.InvoiceDbService;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;
import com.zzz.platform.component.MailProperties;
import com.zzz.platform.utils.EncodeUtil;
import com.zzz.platform.utils.FileFormaterUtil;
import com.zzz.platform.utils.RulesUtil;
import lombok.extern.slf4j.Slf4j;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
public class InvoiceFileServiceImpl implements InvoiceFileService {

    @Autowired
    private InvoiceDbService invoiceDbServiceImpl;

    @Autowired
    private InvoiceFileDao invoiceFileDao;

    @Autowired
    private InvoiceApiService invoiceApiServiceImpl;

    @Autowired
    private JavaMailSender mailSender;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private MailProperties mailProperties;


    @Override
    public ResponseDTO<byte[]> download(BigInteger invoiceId, FileType type) {
        byte[] content = getFileContent(invoiceId, type);
        if (content == null) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        return ResponseDTO.ok(content);
    }

    @Override
    public String searchFileNameById(BigInteger invoiceId, FileType fileType) {
        String fileName = invoiceDbServiceImpl.getFileNameById(invoiceId);
        return jointFileName(fileName, fileType);
    }

    public void saveInvoiceContentInDB(BigInteger invoiceId, byte[] content, FileType filetype) {
        // save file to DB
        InvoiceFileEntity entity = invoiceFileDao.selectOne(
                new QueryWrapper<InvoiceFileEntity>()
                        .eq(InvoiceFileDbColumn.INVOICE_ID.getVal(), invoiceId)
                        .eq(InvoiceFileDbColumn.FILE_TYPE.getVal(), filetype.getValue())
        );
        if (ObjectUtils.isNotEmpty(entity)) {
            invoiceFileDao.deleteById(entity);
        }
        InvoiceFileEntity invoiceFileEntity = new InvoiceFileEntity();
        invoiceFileEntity.setFileType(filetype.getValue());
        invoiceFileEntity.setInvoiceId(invoiceId);
        // format file content
        byte[] formattedFile = FileFormaterUtil.formatFile(content, filetype);
        invoiceFileEntity.setContent(formattedFile);
        invoiceFileDao.insert(invoiceFileEntity);

        invoiceDbServiceImpl.updateFileFlag(invoiceId, filetype, InvoiceDbService.FileStatusFlag.EXIST);
    }

    @Override
    public ResponseDTO<InvoiceJsonVO> searchInvoiceJsonById(BigInteger invoiceId) {
        byte[] content = getFileContent(invoiceId, FileType.JSON);
        InvoiceJsonVO invoiceJsonVO = null;
        try {
            invoiceJsonVO = objectMapper.readValue(content, InvoiceJsonVO.class);
        } catch (IOException e) {
            log.error("objectMapper read value error", e);
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
        }
        return ResponseDTO.ok(invoiceJsonVO);
    }

    public ResponseDTO<ValidateResultVO> validateInvoice(BigInteger invoiceId, String rules) {
        // search json file from db
        byte[] bytes = getFileContent(invoiceId, FileType.XML);
        String fileName = invoiceDbServiceImpl.getFileNameById(invoiceId);
        ResponseDTO<ValidateResultVO> responseDTO = validate(bytes, fileName, rules, String.valueOf(invoiceId));
        if (responseDTO.getOk()) {
            updateValidateResult(invoiceId, responseDTO.getData());
        }
        return responseDTO;
    }
    private void updateValidateResult(BigInteger invoiceId, ValidateResultVO validateResultVO) {
        InvoiceDbService.ValidationFlag flag;
        if (validateResultVO.isSuccessful()) {
            flag = InvoiceDbService.ValidationFlag.SUCCESS;
        } else {
            flag = InvoiceDbService.ValidationFlag.FAILED;
        }
        invoiceDbServiceImpl.updateValidationFlag(invoiceId, flag);
    }

    @Override
    public ResponseDTO<String> sendInvoice(BigInteger invoiceId, FileType fileType, InvoiceSendForm sendForm) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(sendForm.getTargetEmail());
        mimeMessageHelper.setFrom(mailProperties.getUsername());
        mimeMessageHelper.setSubject(sendForm.getSubject());
        mimeMessageHelper.setText(sendForm.getText());

        byte[] content = getFileContent(invoiceId, fileType);
        if (content == null) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        String originalFilename = invoiceDbServiceImpl.getFileNameById(invoiceId);
        String fileName = jointFileName(originalFilename, fileType);

        ByteArrayResource resource = new ByteArrayResource(content);
        mimeMessageHelper.addAttachment(fileName, resource);

        mailSender.send(mimeMessage);
        return ResponseDTO.ok();
    }

    @Override
    public ResponseDTO<String> convertInvoice(BigInteger invoiceId) throws IOException {
        InvoiceFileEntity entity = invoiceFileDao.selectOne(
                new QueryWrapper<InvoiceFileEntity>()
                        .eq(InvoiceFileDbColumn.INVOICE_ID.getVal(), invoiceId)
                        .eq(InvoiceFileDbColumn.FILE_TYPE.getVal(), FileType.JSON.getValue())
        );
        if (entity == null) {
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_DOES_NOT_EXIST);
        }
        InvoiceJsonVO invoiceJsonVO = objectMapper.readValue(entity.getContent(), InvoiceJsonVO.class);
        byte[] ublXml = convertJsonToUbl(invoiceJsonVO);
        // save invoice ubl in db
        saveInvoiceContentInDB(invoiceId, ublXml, FileType.XML);
        return ResponseDTO.ok();
    }

    @Override
    public ResponseDTO<ValidateResultVO> validateInvoice(BigInteger invoiceId, String rules, InvoiceJsonVO invoiceJsonVO) {
        byte[] ublXml = convertJsonToUbl(invoiceJsonVO);
        // save invoice ubl in db
        saveInvoiceContentInDB(invoiceId, ublXml, FileType.XML);
        String fileName = invoiceDbServiceImpl.getFileNameById(invoiceId);

        ResponseDTO<ValidateResultVO> responseDTO = validate(ublXml, fileName, rules, String.valueOf(invoiceId));
        if (responseDTO.getOk()) {
            updateValidateResult(invoiceId, responseDTO.getData());
        }
        return responseDTO;
    }

    private byte[] getFileContent(BigInteger invoiceId, FileType fileType) {
        InvoiceFileEntity entity = invoiceFileDao.selectOne(new QueryWrapper<InvoiceFileEntity>()
                .eq(InvoiceFileDbColumn.INVOICE_ID.getVal(), invoiceId)
                .eq(InvoiceFileDbColumn.FILE_TYPE.getVal(), fileType.getValue()));
        if (ObjectUtils.isNotEmpty(entity)) {
            return entity.getContent();
        }
        return null;
    }

    private byte[] convertJsonToUbl(InvoiceJsonVO invoiceJsonVO) {
        InvoiceType invoiceType = JsonToUblConverter.convertToUbl(invoiceJsonVO);
        return UBL21Writer.invoice().getAsBytes(invoiceType);
    }

    private ResponseDTO<ValidateResultVO> validate(byte[] ublXml, String fileName, String rules, String customer) {
        fileName = jointFileName(fileName, FileType.XML);

        EssInvoiceValidateForm essInvoiceValidateForm = new EssInvoiceValidateForm();
        essInvoiceValidateForm.setFileName(fileName);
        String encode = EncodeUtil.base64Encode(ublXml);
        essInvoiceValidateForm.setContent(encode);
        String md5sum = EncodeUtil.md5Encode(encode.getBytes());
        essInvoiceValidateForm.setChecksum(md5sum);

        List<RulesUtil.RulesEnum> rulesEnumList = RulesUtil.splitRules(rules);
        if (ObjectUtils.isEmpty(rulesEnumList)) {
            return ResponseDTO.error(InvoiceErrorCode.VALIDATION_RULES_ERROR);
        }
        essInvoiceValidateForm.setRules(rulesEnumList);
        essInvoiceValidateForm.setCustomer(customer);
        return invoiceApiServiceImpl.essValidateInvoice(essInvoiceValidateForm);
    }

    private String jointFileName(String fileName, FileType fileType) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // add file suffix
            fileName += "." + fileType.getDesc();
        } else {
            // replace file suffix
            fileName = fileName.substring(0, lastDotIndex) + "." + fileType;
        }
        return fileName;
    }

}
