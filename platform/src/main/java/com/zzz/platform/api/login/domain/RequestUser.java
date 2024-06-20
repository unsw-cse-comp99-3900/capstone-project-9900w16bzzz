package com.zzz.platform.api.login.domain;

import com.zzz.platform.common.enumeration.UserTypeEnum;
import com.zzz.platform.common.swagger.SchemaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
public class RequestUser {

    @Schema(description = "user id")
    private Long userId;

    @SchemaEnum(UserTypeEnum.class)
    private UserTypeEnum userType;

    @Schema(description = "login name")
    private String loginName;

    @Schema(description = "request ip")
    private String ip;

    @Schema(description = "request user-agent")
    private String userAgent;
}
