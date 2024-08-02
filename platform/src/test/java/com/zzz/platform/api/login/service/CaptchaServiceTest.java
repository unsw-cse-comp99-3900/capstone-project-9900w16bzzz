package com.zzz.platform.api.login.service;

import com.zzz.platform.api.login.domain.captcha.CaptchaForm;
import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.domain.SystemEnvironment;
import com.zzz.platform.common.exception.BusinessException;
import com.zzz.platform.service.CacheService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CaptchaServiceTest {

    @Mock
    private DefaultKaptcha defaultKaptcha;

    @Mock
    private SystemEnvironment systemEnvironment;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private CaptchaService captchaService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCaptcha() {
        // Mock the behavior of DefaultKaptcha to return a predefined captcha text
        String captchaText = "testCaptcha";
        when(defaultKaptcha.createText()).thenReturn(captchaText);

        // Mock the behavior of DefaultKaptcha to create an image from the captcha text
        BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        when(defaultKaptcha.createImage(captchaText)).thenReturn(image);

        // Mock SystemEnvironment to return non-production environment
        when(systemEnvironment.isProd()).thenReturn(false);

        // Capture the generated captcha
        CaptchaVO captchaVO = captchaService.generateCaptcha();

        // Validate the generated captcha
        assertNotNull(captchaVO);
        assertNotNull(captchaVO.getCaptchaUuid());
        assertTrue(captchaVO.getCaptchaBase64Image().startsWith("data:image/png;base64,"));
        assertEquals(65L, captchaVO.getExpireSeconds());
        assertEquals(captchaText, captchaVO.getCaptchaText());

        // Verify that the captcha text is saved in the cache
        verify(cacheService).saveKey(eq("captcha_cache"), eq(captchaVO.getCaptchaUuid()), eq(captchaText));
    }

    @Test
    void testGenerateCaptchaException() {
        // Mock DefaultKaptcha to return a predefined captcha text
        when(defaultKaptcha.createText()).thenReturn("testCaptcha");

        // Mock the behavior of DefaultKaptcha to create an image from the captcha text
        BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        when(defaultKaptcha.createImage(anyString())).thenReturn(image);

        // Mock CacheService to throw an exception when saving the captcha text
        doThrow(new BusinessException("Generate CAPTCHA Error")).when(cacheService).saveKey(anyString(), anyString(), anyString());

        // Verify that the exception is thrown when generating the captcha
        assertThrows(BusinessException.class, () -> captchaService.generateCaptcha());
    }

    @Test
    void testCheckCaptchaSuccess() {
        // Create a CaptchaForm with predefined UUID and code
        CaptchaForm form = new CaptchaForm();
        form.setCaptchaUuid(UUID.randomUUID().toString());
        form.setCaptchaCode("testCaptcha");

        // Mock CacheService to return the correct captcha code
        when(cacheService.getValue("captcha_cache", form.getCaptchaUuid())).thenReturn("testCaptcha");

        // Call the checkCaptcha method and validate the response
        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertTrue(response.getOk());

        // Verify that the captcha is removed from the cache after successful validation
        verify(cacheService).removeCache(form.getCaptchaUuid());
    }

    @Test
    void testCheckCaptchaExpired() {
        // Create a CaptchaForm with predefined UUID and code
        CaptchaForm form = new CaptchaForm();
        form.setCaptchaUuid(UUID.randomUUID().toString());
        form.setCaptchaCode("testCaptcha");

        // Mock CacheService to return null for expired captcha
        when(cacheService.getValue("captcha_cache", form.getCaptchaUuid())).thenReturn(null);

        // Call the checkCaptcha method and validate the response
        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertFalse(response.getOk());
        assertEquals("Captcha has expired, please refresh and try again.", response.getMsg());
    }

    @Test
    void testCheckCaptchaWrongCode() {
        // Create a CaptchaForm with predefined UUID and wrong code
        CaptchaForm form = new CaptchaForm();
        form.setCaptchaUuid(UUID.randomUUID().toString());
        form.setCaptchaCode("wrongCaptcha");

        // Mock CacheService to return a different captcha code
        when(cacheService.getValue("captcha_cache", form.getCaptchaUuid())).thenReturn("testCaptcha");

        // Call the checkCaptcha method and validate the response
        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertFalse(response.getOk());
        assertEquals("The verification code is wrong, please enter the correct code.", response.getMsg());
    }

    @Test
    void testCheckCaptchaEmptyFields() {
        // Create an empty CaptchaForm
        CaptchaForm form = new CaptchaForm();

        // Call the checkCaptcha method and validate the response
        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertFalse(response.getOk());
        assertEquals("Please enter the correct code", response.getMsg());
    }
}
