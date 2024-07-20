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
import java.math.BigInteger;

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
     * update user pwd
     * @param userPwdUpdateForm
     * @return
     */
    @PostMapping("/user/updateUser")
    public ResponseDTO<String> updatePassword(@Valid @RequestBody UserPwdUpdateForm userPwdUpdateForm) {
        return userService.updatePassword(userPwdUpdateForm);
    }

    /**
     * @author Zhaoyue Zhang
     * get by login name
     * @param loginName
     * @return
     */
    @GetMapping("/user/getByLoginName/{loginName}")
    public ResponseDTO<UserEntity> getByLoginName(@PathVariable("loginName") String loginName) {
        return userService.getByLoginName1(loginName);
    }

    /**
     * @author Zhaoyue Zhang
     * search by user id
     * @param userId
     * @return
     */
    @GetMapping("/user/getById/{userId}")
    public ResponseDTO<UserEntity> getById(@PathVariable("userId") BigInteger userId) {
        return userService.getById1(userId);
    }

    /**
     * delete by id
     * @param userId
     * @return
     */
    @PostMapping("/user/deleteById/{userId}")
    public ResponseDTO deleteById(@PathVariable("userId") BigInteger userId) {
        return userService.deleteById(userId);
    }
}
