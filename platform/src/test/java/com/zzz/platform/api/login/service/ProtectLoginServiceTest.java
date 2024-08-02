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
        MockitoAnnotations.openMocks(this);
        setField(protectLoginService, "loginMaxFailTimes", loginMaxFailTimes);
        setField(protectLoginService, "loginFailLockedSeconds", loginFailLockedSeconds);
    }

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
        setField(protectLoginService, "loginMaxFailTimes", -1);

        ResponseDTO<LoginFailEntity> response = protectLoginService.checkLogin(BigInteger.ONE, UserTypeEnum.NORMAL);

        assertTrue(response.getOk());
        assertNull(response.getData());
    }

    @Test
    void testCheckLogin_ValidLogin() {
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(2)
                .lockFlag(false) // 设置默认值
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        ResponseDTO<LoginFailEntity> response = protectLoginService.checkLogin(BigInteger.ONE, UserTypeEnum.NORMAL);

        assertTrue(response.getOk());
        assertEquals(loginFailEntity, response.getData());
    }

    @Test
    void testCheckLogin_LockedLogin() {
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(loginMaxFailTimes)
                .loginLockBeginTime(LocalDateTime.now().minusSeconds(loginFailLockedSeconds - 1)) // 刚刚锁定
                .lockFlag(true)
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        ResponseDTO<LoginFailEntity> response = protectLoginService.checkLogin(BigInteger.ONE, UserTypeEnum.NORMAL);

        assertFalse(response.getOk());
        assertEquals(UserErrorCode.LOGIN_FAIL_LOCK.getCode(), response.getCode());
        assertTrue(response.getMsg().contains("You have failed to log in"));
    }

    @Test
    void testRecordLoginFail_FirstFail() {
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(null);

        String result = protectLoginService.recordLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL, "testUser", null);

        assertNull(result);
        verify(loginFailDao, times(1)).insert(any(LoginFailEntity.class));
    }

    @Test
    void testRecordLoginFail_SubsequentFail() {
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(1)
                .lockFlag(false)
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        String result = protectLoginService.recordLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL, "testUser", loginFailEntity);

        assertNull(result);
        verify(loginFailDao, times(1)).updateById(any(LoginFailEntity.class));
    }

    @Test
    void testRecordLoginFail_LockAccount() {
        LoginFailEntity loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.ONE)
                .loginFailCount(4)
                .lockFlag(false) // 设置默认值
                .build();
        when(loginFailDao.selectById(BigInteger.ONE)).thenReturn(loginFailEntity);

        String result = protectLoginService.recordLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL, "testUser", loginFailEntity);

        assertNotNull(result);
        assertTrue(result.contains("locked"));
        verify(loginFailDao, times(1)).updateById(any(LoginFailEntity.class));
    }

    @Test
    void testRemoveLoginFail() {
        protectLoginService.removeLoginFail(BigInteger.ONE, UserTypeEnum.NORMAL);

        verify(loginFailDao, times(1)).deleteById(BigInteger.ONE);
    }

    @Test
    void testBatchDelete() {
        List<Long> idList = Arrays.asList(1L, 2L, 3L);

        ResponseDTO<String> response = protectLoginService.batchDelete(idList);

        assertTrue(response.getOk());
        verify(loginFailDao, times(1)).deleteBatchIds(idList);
    }
}
