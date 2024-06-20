package com.zzz.platform.api.login.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
public class LoginFailVO {

    private Long loginFailId;


    @Schema(description = "user id")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "login name")
    private String loginName;

    @Schema(description = "login fail count")
    private Integer loginFailCount;

    @Schema(description = "lock flag: 1 lock，0 unlock")
    private Integer lockFlag;

    @Schema(description = "Continuous Login Failure Lockout Start Time")
    private LocalDateTime loginLockBeginTime;

    @Schema(description = "create time")
    private LocalDateTime createTime;

    @Schema(description = "update time")
    private LocalDateTime updateTime;
}
