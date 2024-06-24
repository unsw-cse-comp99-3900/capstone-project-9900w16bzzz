package com.zzz.platform.api.login.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/24
 */
@Data
public class LoginUserVO {

    @Schema(description = "user name")
    private String userName;

    @Schema(description = "login name")
    private String loginName;

    @Schema(description = "uuid")
    private Long loginId;
}
