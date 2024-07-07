package com.zzz.platform.api.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Data
@TableName("t_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private BigInteger userId;

    private String loginName;

    private String userName;

    private String loginPwd;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
