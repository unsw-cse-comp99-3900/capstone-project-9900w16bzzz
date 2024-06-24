package com.zzz.platform.api.login.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.zzz.platform.api.login.domain.LoginForm;
import com.zzz.platform.api.login.domain.LoginResultVO;
import com.zzz.platform.api.login.domain.RequestUser;
import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.api.login.service.LoginService;
import com.zzz.platform.common.annoation.NoNeedLogin;
import com.zzz.platform.common.constant.RequestHeaderConst;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.utils.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;


/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@RestController
@Tag(name = "system login")
public class LoginController {

    @Resource
    private LoginService loginService;

    @NoNeedLogin
    @PostMapping("/login")
    @Operation(summary = "login")
    public ResponseDTO<LoginResultVO> login(@Valid @RequestBody LoginForm loginForm, HttpServletRequest request) {
        String ip = ServletUtil.getClientIP(request);
        String userAgent = ServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT);
        return loginService.login(loginForm, ip, userAgent);
    }

    @GetMapping("/login/getLoginInfo")
    @Operation(summary = "get login information")
    public ResponseDTO<LoginResultVO> getLoginInfo() {
        LoginResultVO loginResult = new LoginResultVO();
        String tokenValue = StpUtil.getTokenValue();
        loginResult.setToken(tokenValue);
        return ResponseDTO.ok(loginResult);
    }

    @Operation(summary = "logout")
    @GetMapping("/login/logout")
    public ResponseDTO<String> logout(@RequestHeader(value = RequestHeaderConst.TOKEN, required = false) String token) {
        return loginService.logout(token, Objects.requireNonNull(RequestUtil.getRequestUser()));
    }

    @Operation(summary = "get captcha")
    @GetMapping("/login/getCaptcha")
    @NoNeedLogin
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return loginService.getCaptcha();
    }

    @GetMapping("/getLoginName")
    @Operation(summary = "get login name by token")
    public ResponseDTO<RequestUser> getLoginName(HttpServletRequest request) {
        String tokenValue = StpUtil.getTokenValue();
        String loginId = (String) StpUtil.getLoginIdByToken(tokenValue);
        RequestUser requestUser = loginService.getLoginUser(loginId, request);

        return ResponseDTO.ok(requestUser);
    }
}
