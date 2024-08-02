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
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidatePassComplexity_EmptyPassword() {
        // Test the password complexity validation with an empty password
        ResponseDTO<String> response = protectedPasswordService.validatePassComplexity("");

        // Verify that the response indicates failure and contains the correct message
        assertFalse(response.getOk());
        assertEquals(ProtectedPasswordService.PASSWORD_FORMAT_MSG, response.getMsg());
    }

    @Test
    void testValidatePassComplexity_ValidPassword() {
        // Test the password complexity validation with a valid password
        ResponseDTO<String> response = protectedPasswordService.validatePassComplexity("Valid123");

        // Verify that the response indicates success
        assertTrue(response.getOk());
    }

    @Test
    void testDecryptPassword() {
        // Prepare encrypted and decrypted password strings
        String encryptedPassword = "encryptedPassword";
        String decryptedPassword = "decryptedPassword";

        // Mock the AesEncryptService to return the decrypted password when the decrypt method is called
        when(aesEncryptService.decrypt(encryptedPassword)).thenReturn(decryptedPassword);

        // Call the decryptPassword method and capture the result
        String result = protectedPasswordService.decryptPassword(encryptedPassword);

        // Verify that the result matches the expected decrypted password
        assertEquals(decryptedPassword, result);

        // Verify that the decrypt method of AesEncryptService was called once with the correct argument
        verify(aesEncryptService, times(1)).decrypt(encryptedPassword);
    }
}
