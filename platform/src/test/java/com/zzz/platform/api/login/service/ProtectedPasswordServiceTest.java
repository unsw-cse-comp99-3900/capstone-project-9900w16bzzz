package com.zzz.platform.api.login.service;

import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.service.AesEncryptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProtectedPasswordServiceTest {

    @InjectMocks
    private ProtectedPasswordService protectedPasswordService;

    @Mock
    private AesEncryptService aesEncryptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidatePassComplexity_EmptyPassword() {
        ResponseDTO<String> response = protectedPasswordService.validatePassComplexity("");

        assertFalse(response.getOk());
        assertEquals(ProtectedPasswordService.PASSWORD_FORMAT_MSG, response.getMsg());
    }

    @Test
    void testValidatePassComplexity_ValidPassword() {
        ResponseDTO<String> response = protectedPasswordService.validatePassComplexity("Valid123");

        assertTrue(response.getOk());
    }

    @Test
    void testDecryptPassword() {
        String encryptedPassword = "encryptedPassword";
        String decryptedPassword = "decryptedPassword";

        when(aesEncryptService.decrypt(encryptedPassword)).thenReturn(decryptedPassword);

        String result = protectedPasswordService.decryptPassword(encryptedPassword);

        assertEquals(decryptedPassword, result);
        verify(aesEncryptService, times(1)).decrypt(encryptedPassword);
    }
}
