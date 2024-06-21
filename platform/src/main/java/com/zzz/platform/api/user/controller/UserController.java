package com.zzz.platform.api.user.controller;

import com.zzz.platform.api.user.domain.UserAddForm;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.annoation.NoNeedLogin;
import com.zzz.platform.common.domain.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
