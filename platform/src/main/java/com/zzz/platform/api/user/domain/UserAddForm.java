package com.zzz.platform.api.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
public class UserAddForm {

    @Schema(description = "login name")
    @NotNull(message = "not null")
    @Length(max = 30, message = "maximum length 30")
    private String loginName;

    @Schema(description = "login pwd")
    private String loginPwd;
}
