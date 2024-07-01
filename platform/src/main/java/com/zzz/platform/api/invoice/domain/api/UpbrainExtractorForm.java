package com.zzz.platform.api.invoice.domain.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/1
 */
@Data
@AllArgsConstructor
public class UpbrainExtractorForm {

    @Schema(description = "pdf file")
    private MultipartFile file;
}
