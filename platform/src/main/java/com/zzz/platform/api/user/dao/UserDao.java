package com.zzz.platform.api.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzz.platform.api.user.domain.UserVO;
import com.zzz.platform.api.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Mapper
@Component
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * query by login name
     */
    UserEntity getByLoginName(@Param("loginName") String loginName);

    /**
     * query by user id
     */
    UserVO getUserById(@Param("userId") Long userId);

    /**
     * update pwd
     */
    Integer updatePassword(@Param("userId") Integer userId, @Param("loginPwd") String loginPwd);

    Integer addUser(@Param("loginName") String loginName, @Param("loginPwd") String loginPwd);

}
