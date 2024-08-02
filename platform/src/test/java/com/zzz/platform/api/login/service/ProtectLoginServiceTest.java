package com.zzz.platform.api.login.service;

import com.zzz.platform.api.login.dao.LoginFailDao;
import com.zzz.platform.api.login.entity.LoginFailEntity;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.UserTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProtectLoginServiceTest {

    @InjectMocks
    private ProtectLoginService protectLoginService;

    @Mock
    private LoginFailDao loginFailDao;

    @Value("${classified-protect.login-max-fail-times}")
    private Integer loginMaxFailTimes;

    @Value("${classified-protect.login-fail-locked-seconds}")
    private Integer loginFailLockedSeconds;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
        // Set the protected fields loginMaxFailTimes and loginFailLockedSeconds
        setField(protectLoginService, "loginMaxFailTimes", loginMaxFailTimes);
        setField(protectLoginService, "loginFailLockedSeconds", loginFailLockedSeconds);
    }

    // Helper method to set the value of a protected field using reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCheckLogin_NoValidation() {
        // Disable login validation by setting loginMaxFailTimes to -1
        setField(protectLoginService, "loginMaxFailTimes", -1);

        // Check login for a user without validation
        ResponseDTO<LoginFailEntity> response = protectLoginService.checkLogin(BigInteger.ONE, UserTypeEnum.NORMAL);

        // Verify the response is successful and contains no data
        assertTrue(response.getOk());
        assertNull(response.getData());
    }

    @Test
    void testCheckLogin_ValidLogin() {
        // Prepare a LoginFailEntity with valid login details
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(2)
                .lockFlag(false) // Set default value
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        // Check login for a user with valid login details
        ResponseDTO<LoginFailEntity> response = protectLoginService.checkLogin(BigInteger.ONE, UserTypeEnum.NORMAL);

        // Verify the response is successful and contains the LoginFailEntity
        assertTrue(response.getOk());
        assertEquals(loginFailEntity, response.getData());
    }

    @Test
    void testCheckLogin_LockedLogin() {
        // Prepare a LoginFailEntity with locked login details
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(loginMaxFailTimes)
                .loginLockBeginTime(LocalDateTime.now().minusSeconds(loginFailLockedSeconds - 1)) // Just locked
                .lockFlag(true)
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        // Check login for a user with locked login details
        ResponseDTO<LoginFailEntity> response = protectLoginService.checkLogin(BigInteger.ONE, UserTypeEnum.NORMAL);

        // Verify the response indicates a login failure due to account lock
        assertFalse(response.getOk());
        assertEquals(UserErrorCode.LOGIN_FAIL_LOCK.getCode(), response.getCode());
        assertTrue(response.getMsg().contains("You have failed to log in"));
    }

    @Test
    void testRecordLoginFail_FirstFail() {
        // Mock the LoginFailDao to return null, indicating no previous login fail record
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(null);

        // Record the first login failure for a user
        String result = protectLoginService.recordLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL, "testUser", null);

        // Verify that no lock message is returned and a new LoginFailEntity is inserted
        assertNull(result);
        verify(loginFailDao, times(1)).insert(any(LoginFailEntity.class));
    }

    @Test
    void testRecordLoginFail_SubsequentFail() {
        // Prepare a LoginFailEntity with previous login fail records
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(1)
                .lockFlag(false)
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        // Record a subsequent login failure for the user
        String result = protectLoginService.recordLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL, "testUser", loginFailEntity);

        // Verify that no lock message is returned and the existing LoginFailEntity is updated
        assertNull(result);
        verify(loginFailDao, times(1)).updateById(any(LoginFailEntity.class));
    }

    @Test
    void testRecordLoginFail_LockAccount() {
        // Prepare a LoginFailEntity with previous login fail records nearing the lock threshold
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(4)
                .lockFlag(false) // Set default value
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        // Record a login failure that triggers account lock
        String result = protectLoginService.recordLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL, "testUser", loginFailEntity);

        // Verify that a lock message is returned and the existing LoginFailEntity is updated
        assertNotNull(result);
        assertTrue(result.contains("locked"));
        verify(loginFailDao, times(1)).updateById(any(LoginFailEntity.class));
    }

    @Test
    void testRemoveLoginFail() {
        // Remove the login fail record for a user
        protectLoginService.removeLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL);

        // Verify that the LoginFailEntity is deleted from the database
        verify(loginFailDao, times(1)).deleteById(BigInteger.ONE);
    }

    @Test
    void testBatchDelete() {
        // Prepare a list of IDs to be deleted
        List<Long> idList = Arrays.asList(1L, 2L, 3L);

        // Batch delete login fail records for the provided IDs
        ResponseDTO<String> response = protectLoginService.batchDelete(idList);

        // Verify the response is successful and the login fail records are deleted
        assertTrue(response.getOk());
        verify(loginFailDao, times(1)).deleteBatchIds(idList);
    }
}
