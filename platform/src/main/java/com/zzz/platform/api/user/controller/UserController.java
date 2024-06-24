package com.zzz.platform.api.user.controller;

import com.zzz.platform.api.user.domain.UserAddForm;
import com.zzz.platform.api.user.domain.UserPwdUpdateForm;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.annoation.NoNeedLogin;
import com.zzz.platform.common.domain.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/21
 */
@RestController
@Tag(name = "user management")
public class UserController {

    @Resource
    private UserService userService;



    @NoNeedLogin
    @PostMapping("/signup")
    @Operation(summary = "sign up")
    public ResponseDTO<String> signup(@Valid @RequestBody UserAddForm userAddForm, HttpServletRequest request) {

        return userService.addUser(userAddForm);
    }

    /**
     * @author Zhaoyue Zhang
     * 修改用户登录密码
     * @param userPwdUpdateForm
     * @return
     */
    @PostMapping("/user/updateUser")
    public ResponseDTO<String> updatePassword(@Valid @RequestBody UserPwdUpdateForm userPwdUpdateForm) {
        return userService.updatePassword(userPwdUpdateForm);
    }

    /**
     * @author Zhaoyue Zhang
     * 根据登录名查询用户信息
     * @param loginName
     * @return
     */
    @GetMapping("/user/getByLoginName/{loginName}")
    public ResponseDTO<UserEntity> getByLoginName(@PathVariable("loginName") String loginName) {
        return userService.getByLoginName1(loginName);
    }

    /**
     * @author Zhaoyue Zhang
     * 根据userId查询用户信息
     * @param userId
     * @return
     */
    @GetMapping("/user/getById/{userId}")
    public ResponseDTO<UserEntity> getById(@PathVariable("userId") Long userId) {
        return userService.getById1(userId);
    }

    /**
     * 根据用户id删除用户  物理删除
     * @param userId
     * @return
     */
    @PostMapping("/user/deleteById/{userId}")
    public ResponseDTO deleteById(@PathVariable("userId") Long userId) {
        return userService.deleteById(userId);
    }
}
