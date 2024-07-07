package com.zzz.platform.api.invoice.controller;

import com.zzz.platform.api.invoice.domain.InvoiceDeleteForm;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.domain.InvoiceListVO;
import com.zzz.platform.api.invoice.domain.InvoiceQueryForm;
import com.zzz.platform.api.invoice.service.InvoiceDbService;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.api.invoice.service.InvoiceUploadService;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 作者: zhaoyue zhang
 * 版本: v1.0.0
 * 日期: 2024/6/24
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
    public ResponseDTO<InvoiceJsonVO> uploadFile(@RequestParam BigInteger userId, @RequestParam("file") MultipartFile file) {
        // get user by user_id
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        return invoiceUploadService.upload(userId, file);
    }

    @GetMapping("/invoice/download")
    @Operation(summary = "download a file")
    public void download(@RequestParam BigInteger invoiceId, @RequestParam("fileType") String fileType, HttpServletResponse response) throws IOException {
        ResponseDTO<byte[]> responseDTO = invoiceFileService.download(invoiceId, fileType.toLowerCase());
        if (!responseDTO.getOk()) {
            ResponseUtil.write(response, responseDTO);
            return;
        }
        String fileName = invoiceFileService.searchFileNameById(invoiceId, fileType.toLowerCase());
        // download file content
        byte[] content = responseDTO.getData();
        // set download response header
        ResponseUtil.setDownloadFileHeader(response, fileName, String.valueOf(content.length));
        // download file
        response.getOutputStream().write(content);

    }

    @PostMapping("/invoice/validate")
    @Operation(summary = "validate user files")
    public ResponseDTO<String> validate(@RequestParam BigInteger invoiceId) throws IOException {
        return invoiceFileService.validateInvoice(invoiceId);
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
}
