package com.zzz.platform.api.login.domain;

import com.zzz.platform.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
public class LoginFailQueryForm extends PageParam {

    @Schema(description = "login name")
    private String loginName;

    @Schema(description = "lock flag")
    private Boolean lockFlag;

    @Schema(description = "login lock begin time")
    private LocalDate loginLockBeginTimeBegin;

    @Schema(description = "login lock end time")
    private LocalDate loginLockBeginTimeEnd;
}
