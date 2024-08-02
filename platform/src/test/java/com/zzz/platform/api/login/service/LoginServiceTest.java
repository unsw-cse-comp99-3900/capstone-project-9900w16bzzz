package com.zzz.platform.api.login.service;

import com.zzz.platform.api.login.domain.LoginForm;
import com.zzz.platform.api.login.domain.LoginResultVO;
import com.zzz.platform.api.login.domain.RequestUser;
import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.api.login.entity.LoginFailEntity;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.UserTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private CaptchaService captchaService;

    @Mock
    private ProtectedPasswordService protectedPasswordService;

    @Mock
    private ProtectLoginService protectLoginService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCaptcha() {
        CaptchaVO captchaVO = new CaptchaVO();
        when(captchaService.generateCaptcha()).thenReturn(captchaVO);

        ResponseDTO<CaptchaVO> response = loginService.getCaptcha();

        assertTrue(response.getOk());
        assertEquals(captchaVO, response.getData());
    }

    @Test
    void testLoginSuccess() {
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginName("testUser");
        loginForm.setLoginPwd("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(BigInteger.valueOf(1));
        userEntity.setLoginName("testUser");
        userEntity.setLoginPwd("password");
        userEntity.setUserName("Test User");
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        when(userService.getByLoginName("testUser")).thenReturn(userEntity);

        LoginFailEntity loginFailEntity = mock(LoginFailEntity.class);
        when(protectLoginService.checkLogin(userEntity.getUserId(), UserTypeEnum.NORMAL)).thenReturn(ResponseDTO.ok(loginFailEntity));

        ResponseDTO<LoginResultVO> response = loginService.login(loginForm, "127.0.0.1", "Mozilla/5.0");

        assertTrue(response.getOk());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getToken());
    }

    @Test
    void testLoginFailureWrongPassword() {
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginName("testUser");
        loginForm.setLoginPwd("wrongPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(BigInteger.valueOf(1));
        userEntity.setLoginName("testUser");
        userEntity.setLoginPwd("correctPassword");
        userEntity.setUserName("Test User");
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        when(userService.getByLoginName("testUser")).thenReturn(userEntity);

        LoginFailEntity loginFailEntity = mock(LoginFailEntity.class);
        when(protectLoginService.checkLogin(userEntity.getUserId(), UserTypeEnum.NORMAL)).thenReturn(ResponseDTO.ok(loginFailEntity));

        ResponseDTO<LoginResultVO> response = loginService.login(loginForm, "127.0.0.1", "Mozilla/5.0");

        assertFalse(response.getOk());
        assertEquals("Login name or password error！", response.getMsg());
    }

    @Test
    void testGetLoginUser() {
        String loginId = "1,01";
        BigInteger userId = new BigInteger("1");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setLoginName("testUser");
        userEntity.setUserName("Test User");
        userEntity.setLoginPwd("password");
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        when(userService.getById(userId)).thenReturn(userEntity);

        // Mock the getHeaderNames method
        Enumeration<String> headerNames = Collections.enumeration(Collections.singletonList("User-Agent"));
        when(request.getHeaderNames()).thenReturn(headerNames);

        // Use doAnswer to return different values based on the argument
        doAnswer(invocation -> {
            String arg = invocation.getArgument(0, String.class);
            if ("User-Agent".equals(arg)) {
                return "Mozilla/5.0";
            } else {
                return null;
            }
        }).when(request).getHeader(anyString());

        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        RequestUser requestUser = loginService.getLoginUser(loginId, request);

        assertNotNull(requestUser);
        assertEquals("testUser", requestUser.getLoginName());
        assertEquals("127.0.0.1", requestUser.getIp());
        assertEquals("Mozilla/5.0", requestUser.getUserAgent());
    }

    @Test
    void testLogout() {
        RequestUser requestUser = new RequestUser();
        requestUser.setUserId(BigInteger.valueOf(1));
        requestUser.setLoginName("testUser");

        String token = "someToken";

        ResponseDTO<String> response = loginService.logout(token, requestUser);

        assertTrue(response.getOk());
    }
}
