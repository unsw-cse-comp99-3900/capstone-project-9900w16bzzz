package com.zzz.platform.api.invoice.controller;

import com.zzz.platform.api.invoice.domain.UploadFileForm;
import com.zzz.platform.api.invoice.entity.InvoiceEntity;
import com.zzz.platform.api.invoice.service.DeleteFileService;
import com.zzz.platform.api.invoice.service.UploadFileService;
import com.zzz.platform.api.invoice.service.SearchFileService;
import com.zzz.platform.common.domain.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 作者: zhaoyue zhang
 * 版本: v1.0.0
 * 日期: 2024/6/24
 */
@RestController
@Tag(name = "invoice")
public class UploadFileController {

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private SearchFileService searchFileService;

    @Autowired
    private DeleteFileService deleteFileService;

    @PostMapping("/invoice/upload")
    @Operation(summary = "Upload a file")
    public ResponseDTO<String> uploadFile(@Valid @RequestBody UploadFileForm uploadFileForm, @RequestParam Long userId) {
        return uploadFileService.uploadFile(uploadFileForm, userId);
    }

    @PostMapping("/invoice/search")
    @Operation(summary = "Search user files")
    public List<InvoiceEntity> searchFile(@Valid @RequestBody String fileName, @RequestParam Long userId) {
        return searchFileService.searchFile(fileName, userId);
    }

    @PostMapping("/invoice/delete")
    @Operation(summary = "delete a user file")
    public ResponseDTO<String> deleteFile(@Valid @RequestBody Integer fileId, @RequestParam Long userId) {
        return deleteFileService.deleteFile(fileId, userId);
    }
}
