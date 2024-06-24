package com.zzz.platform.api.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 作者: zhaoyue zhang
 * 版本: v1.0.0
 * 日期: 2024/6/24
 */
@Data
public class UploadFileForm {

    @Schema(description = "file name", example = "bill invoice")
    @NotNull(message = "not null")
    @Length(max = 50, message = "maximum length 50")
    private String fileName;

    @Schema(description = "file content")
    @NotNull(message = "not null")
    private String fileContent;
}
