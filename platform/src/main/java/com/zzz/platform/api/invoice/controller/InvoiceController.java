package com.zzz.platform.api.invoice.controller;

import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.DeleteFileService;
import com.zzz.platform.api.invoice.service.InvoiceCreationService;
import com.zzz.platform.api.invoice.service.InvoiceFileService;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.code.UserErrorCode;
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
import java.util.List;

/**
 * 作者: zhaoyue zhang
 * 版本: v1.0.0
 * 日期: 2024/6/24
 */
@RestController
@Tag(name = "invoice")
public class InvoiceController {

    @Autowired
    private InvoiceCreationService invoiceCreationService;

    @Autowired
    private InvoiceFileService invoiceFileService;

    @Autowired
    private DeleteFileService deleteFileService;

    @Autowired
    private UserService userService;

    @PostMapping("/invoice/upload")
    @Operation(summary = "Upload a file")
    public ResponseDTO<InvoiceJsonVO> uploadFile(@RequestParam Long userId, @RequestParam("file") MultipartFile file) {
        // get user by user_id
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        return invoiceCreationService.upload(userId, file);
    }

    @GetMapping("/invoice/download")
    @Operation(summary = "download a file")
    public void download(@RequestParam Long fileId, @RequestParam("fileType") String fileType, HttpServletResponse response) throws IOException {
        ResponseDTO<byte[]> responseDTO = invoiceFileService.download(fileId, fileType.toLowerCase());
        if (!responseDTO.getOk()) {
            ResponseUtil.write(response, responseDTO);
            return;
        }
        String fileName = invoiceFileService.searchFileNameById(fileId, fileType.toLowerCase());
        // download file content
        byte[] content = responseDTO.getData();
        // set download response header
        ResponseUtil.setDownloadFileHeader(response, fileName, String.valueOf(content.length));
        // download file
        response.getOutputStream().write(content);

    }



    @PostMapping("/invoice/search")
    @Operation(summary = "Search user files")
    public List<InvoiceEntity> searchFile(@Valid @RequestBody String fileName, @RequestParam Long userId) {
        return invoiceFileService.searchFile(fileName, userId);
    }

    @PostMapping("/invoice/delete")
    @Operation(summary = "delete a user file")
    public ResponseDTO<String> deleteFile(@Valid @RequestBody Integer fileId, @RequestParam Long userId) {
        return deleteFileService.deleteFile(fileId, userId);
    }
}
