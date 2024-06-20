package com.zzz.platform.api.login.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Data
public class LoginResultVO {


    @Schema(description = "token")
    private String token;

    @Schema(description = "上次登录时间")
    private LocalDateTime lastLoginTime;
}
