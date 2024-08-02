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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCaptcha() {
        // Mock the behavior of DefaultKaptcha
        String captchaText = "testCaptcha";
        when(defaultKaptcha.createText()).thenReturn(captchaText);
        BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        when(defaultKaptcha.createImage(captchaText)).thenReturn(image);

        // Mock SystemEnvironment to return non-production environment
        when(systemEnvironment.isProd()).thenReturn(false);

        // Capture the generated captcha
        CaptchaVO captchaVO = captchaService.generateCaptcha();

        assertNotNull(captchaVO);
        assertNotNull(captchaVO.getCaptchaUuid());
        assertTrue(captchaVO.getCaptchaBase64Image().startsWith("data:image/png;base64,"));
        assertEquals(65L, captchaVO.getExpireSeconds());
        assertEquals(captchaText, captchaVO.getCaptchaText());

        verify(cacheService).saveKey(eq("captcha_cache"), eq(captchaVO.getCaptchaUuid()), eq(captchaText));
    }

    @Test
    void testGenerateCaptchaException() {
        // Mock DefaultKaptcha to throw an exception
        when(defaultKaptcha.createText()).thenReturn("testCaptcha");
        BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        when(defaultKaptcha.createImage(anyString())).thenReturn(image);

        doThrow(new BusinessException("Generate CAPTCHA Error")).when(cacheService).saveKey(anyString(), anyString(), anyString());

        assertThrows(BusinessException.class, () -> captchaService.generateCaptcha());
    }

    @Test
    void testCheckCaptchaSuccess() {
        CaptchaForm form = new CaptchaForm();
        form.setCaptchaUuid(UUID.randomUUID().toString());
        form.setCaptchaCode("testCaptcha");

        // Mock CacheService to return correct captcha code
        when(cacheService.getValue("captcha_cache", form.getCaptchaUuid())).thenReturn("testCaptcha");

        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertTrue(response.getOk());

        verify(cacheService).removeCache(form.getCaptchaUuid());
    }

    @Test
    void testCheckCaptchaExpired() {
        CaptchaForm form = new CaptchaForm();
        form.setCaptchaUuid(UUID.randomUUID().toString());
        form.setCaptchaCode("testCaptcha");

        // Mock CacheService to return null for expired captcha
        when(cacheService.getValue("captcha_cache", form.getCaptchaUuid())).thenReturn(null);

        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertFalse(response.getOk());
        assertEquals("Captcha has expired, please refresh and try again.", response.getMsg());
    }

    @Test
    void testCheckCaptchaWrongCode() {
        CaptchaForm form = new CaptchaForm();
        form.setCaptchaUuid(UUID.randomUUID().toString());
        form.setCaptchaCode("wrongCaptcha");

        // Mock CacheService to return different captcha code
        when(cacheService.getValue("captcha_cache", form.getCaptchaUuid())).thenReturn("testCaptcha");

        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertFalse(response.getOk());
        assertEquals("The verification code is wrong, please enter the correct code.", response.getMsg());
    }

    @Test
    void testCheckCaptchaEmptyFields() {
        CaptchaForm form = new CaptchaForm();

        ResponseDTO<String> response = captchaService.checkCaptcha(form);

        assertNotNull(response);
        assertFalse(response.getOk());
        assertEquals("Please enter the correct code", response.getMsg());
    }
}
