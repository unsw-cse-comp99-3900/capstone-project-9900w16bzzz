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
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCaptcha() {
        // Mock the CaptchaService to return a predefined CaptchaVO object
        CaptchaVO captchaVO = new CaptchaVO();
        when(captchaService.generateCaptcha()).thenReturn(captchaVO);

        // Call the getCaptcha method and validate the response
        ResponseDTO<CaptchaVO> response = loginService.getCaptcha();

        // Verify the response is successful and contains the expected CaptchaVO
        assertTrue(response.getOk());
        assertEquals(captchaVO, response.getData());
    }

    @Test
    void testLoginSuccess() {
        // Prepare a LoginForm with valid credentials
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginName("testUser");
        loginForm.setLoginPwd("password");

        // Prepare a UserEntity object with matching credentials
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(BigInteger.valueOf(1));
        userEntity.setLoginName("testUser");
        userEntity.setLoginPwd("password");
        userEntity.setUserName("Test User");
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        // Mock the UserService to return the UserEntity
        when(userService.getByLoginName("testUser")).thenReturn(userEntity);

        // Mock the ProtectLoginService to allow the login attempt
        LoginFailEntity loginFailEntity = mock(LoginFailEntity.class);
        when(protectLoginService.checkLogin(userEntity.getUserId(), UserTypeEnum.NORMAL)).thenReturn(ResponseDTO.ok(loginFailEntity));

        // Call the login method and validate the response
        ResponseDTO<LoginResultVO> response = loginService.login(loginForm, "127.0.0.1", "Mozilla/5.0");

        // Verify the response is successful and contains a token
        assertTrue(response.getOk());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getToken());
    }

    @Test
    void testLoginFailureWrongPassword() {
        // Prepare a LoginForm with invalid credentials
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginName("testUser");
        loginForm.setLoginPwd("wrongPassword");

        // Prepare a UserEntity object with correct credentials
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(BigInteger.valueOf(1));
        userEntity.setLoginName("testUser");
        userEntity.setLoginPwd("correctPassword");
        userEntity.setUserName("Test User");
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        // Mock the UserService to return the UserEntity
        when(userService.getByLoginName("testUser")).thenReturn(userEntity);

        // Mock the ProtectLoginService to allow the login attempt
        LoginFailEntity loginFailEntity = mock(LoginFailEntity.class);
        when(protectLoginService.checkLogin(userEntity.getUserId(), UserTypeEnum.NORMAL)).thenReturn(ResponseDTO.ok(loginFailEntity));

        // Call the login method and validate the response
        ResponseDTO<LoginResultVO> response = loginService.login(loginForm, "127.0.0.1", "Mozilla/5.0");

        // Verify the response indicates a login failure due to wrong password
        assertFalse(response.getOk());
        assertEquals("Login name or password error！", response.getMsg());
    }

    @Test
    void testGetLoginUser() {
        // Prepare a loginId and userId
        String loginId = "1,01";
        BigInteger userId = new BigInteger("1");

        // Prepare a UserEntity object with test data
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setLoginName("testUser");
        userEntity.setUserName("Test User");
        userEntity.setLoginPwd("password");
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        // Mock the UserService to return the UserEntity
        when(userService.getById(userId)).thenReturn(userEntity);

        // Mock the HttpServletRequest to return specific headers and remote address
        Enumeration<String> headerNames = Collections.enumeration(Collections.singletonList("User-Agent"));
        when(request.getHeaderNames()).thenReturn(headerNames);
        doAnswer(invocation -> {
            String arg = invocation.getArgument(0, String.class);
            if ("User-Agent".equals(arg)) {
                return "Mozilla/5.0";
            } else {
                return null;
            }
        }).when(request).getHeader(anyString());
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // Call the getLoginUser method and validate the response
        RequestUser requestUser = loginService.getLoginUser(loginId, request);

        // Verify the returned RequestUser contains the expected values
        assertNotNull(requestUser);
        assertEquals("testUser", requestUser.getLoginName());
        assertEquals("127.0.0.1", requestUser.getIp());
        assertEquals("Mozilla/5.0", requestUser.getUserAgent());
    }

    @Test
    void testLogout() {
        // Prepare a RequestUser with test data
        RequestUser requestUser = new RequestUser();
        requestUser.setUserId(BigInteger.valueOf(1));
        requestUser.setLoginName("testUser");

        // Prepare a token for logout
        String token = "someToken";

        // Call the logout method and validate the response
        ResponseDTO<String> response = loginService.logout(token, requestUser);

        // Verify the response is successful
        assertTrue(response.getOk());
    }
}
