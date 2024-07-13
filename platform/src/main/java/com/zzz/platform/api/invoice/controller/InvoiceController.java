package com.zzz.platform.api.invoice.controller;

import com.zzz.platform.api.invoice.domain.*;
import com.zzz.platform.api.invoice.service.InvoiceDbService;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.api.invoice.service.InvoiceUploadService;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.FileType;
import com.zzz.platform.utils.EnumUtil;
import com.zzz.platform.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/13
 */
@RestController
@Tag(name = "invoice")
public class InvoiceController {

    @Autowired
    private InvoiceUploadService invoiceUploadService;

    @Autowired
    private InvoiceFileService invoiceFileService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvoiceDbService invoiceDbService;

    @PostMapping("/invoice/upload")
    @Operation(summary = "Upload a file")
    public ResponseDTO<InvoiceUploadResultVO> uploadFile(@RequestParam BigInteger userId, @RequestParam("file") MultipartFile file) {
        // get user by user_id
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        return invoiceUploadService.upload(userId, file);
    }

    @GetMapping("/invoice/json")
    @Operation(summary = "search json result by invoiceId")
    public ResponseDTO<InvoiceJsonVO> searchJson(@RequestParam BigInteger invoiceId) {
        return invoiceFileService.searchInvoiceJsonById(invoiceId);
    }

    @GetMapping("/invoice/download")
    @Operation(summary = "download a file")
    public void download(@NotNull @RequestParam BigInteger invoiceId, @NotNull @RequestParam("fileType") String fileType, HttpServletResponse response) throws IOException {

        ResponseDTO<byte[]> responseDTO = invoiceFileService.download(invoiceId, EnumUtil.getEnumByValue(fileType.toLowerCase(), FileType.class));
        if (!responseDTO.getOk()) {
            ResponseUtil.write(response, responseDTO);
            return;
        }
        String fileName = invoiceFileService.searchFileNameById(invoiceId, EnumUtil.getEnumByValue(fileType.toLowerCase(), FileType.class));
        // download file content
        byte[] content = responseDTO.getData();
        // set download response header
        ResponseUtil.setDownloadFileHeader(response, fileName, String.valueOf(content.length));
        // download file
        response.getOutputStream().write(content);
    }

    @PostMapping("/invoice/validate")
    @Operation(summary = "validate user files")
    public ResponseDTO<ValidateResultVO> validate(@RequestParam BigInteger invoiceId, @RequestParam String rules, @RequestBody InvoiceJsonVO invoiceJsonVO) {
        return invoiceFileService.validateInvoice(invoiceId, rules, invoiceJsonVO);
    }

    @PostMapping("/invoice/validateExist")
    @Operation(summary = "validate user exist files")
    public ResponseDTO<ValidateResultVO> validate(@RequestParam BigInteger invoiceId, @RequestParam String rules) {
        return invoiceFileService.validateInvoice(invoiceId, rules);
    }

    @PostMapping("/invoice/list")
    @Operation(summary = "list user files")
    public ResponseDTO<PageResult<InvoiceListVO>> list(@Valid @RequestBody InvoiceQueryForm queryForm) {
        return invoiceDbService.list(queryForm);
    }

    @PostMapping("/invoice/delete")
    @Operation(summary = "delete a user file")
    public ResponseDTO<String> deleteFile(@RequestBody InvoiceDeleteForm deleteForm) {
        return invoiceDbService.delete(deleteForm);
    }

    @PostMapping("/invoice/send")
    @Operation(summary = "send invoice to target email")
    public ResponseDTO<String> sendEmail(@NotNull @RequestParam BigInteger invoiceId, @NotNull @RequestParam("fileType") String fileType, @Valid @RequestBody InvoiceSendForm sendForm) throws MessagingException {
        FileType type = EnumUtil.getEnumByValue(fileType, FileType.class);
        return invoiceFileService.sendInvoice(invoiceId, type, sendForm);
    }


}
