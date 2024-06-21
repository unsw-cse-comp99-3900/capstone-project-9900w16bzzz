package com.zzz.platform.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/21
 */
@Data
public class RequestUrlVO {

    @Schema(description = "comment")
    private String comment;

    @Schema(description = "controller.method")
    private String name;

    @Schema(description = "url")
    private String url;
}
