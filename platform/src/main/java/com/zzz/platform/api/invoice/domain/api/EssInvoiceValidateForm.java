package com.zzz.platform.api.invoice.domain.api;

import com.zzz.platform.utils.RulesUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/1
 */
@Data
public class EssInvoiceValidateForm {

    @Schema(description = "customer name")
    private String customer;

    @Schema(description = "validate rules, using ',' to split")
    private List<RulesUtil.RulesEnum> rules;

    @Schema(description = "file name")
    private String fileName;

    @Schema(description = "file content")
    private String content;

    @Schema(description = "file md5 value")
    private String checksum;

}
