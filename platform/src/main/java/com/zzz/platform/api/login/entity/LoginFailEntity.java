package com.zzz.platform.api.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */

@Data
@Builder
@TableName("t_login_fail")
public class LoginFailEntity {



    @TableId(type = IdType.AUTO)
    private BigInteger loginFailId;

    private BigInteger userId;

    private Integer userType;

    private String loginName;

    /**
     * lock flag
     */
    private Boolean lockFlag;

    /**
     * login fail count
     */
    private Integer loginFailCount;

    /**
     * Continuous Login Failure Lockout Start Time
     */
    private LocalDateTime loginLockBeginTime;


    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
