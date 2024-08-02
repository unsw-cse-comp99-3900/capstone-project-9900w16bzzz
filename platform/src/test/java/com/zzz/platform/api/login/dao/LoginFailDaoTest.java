package com.zzz.platform.api.login.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzz.platform.api.login.domain.LoginFailQueryForm;
import com.zzz.platform.api.login.domain.LoginFailVO;
import com.zzz.platform.api.login.entity.LoginFailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoginFailDaoTest {

    @Mock
    private LoginFailDao loginFailDao;

    private LoginFailEntity loginFailEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginFailEntity = LoginFailEntity.builder()
                .userId(BigInteger.valueOf(1))
                .userType(1)
                .loginName("testUser")
                .lockFlag(false)
                .loginFailCount(0)
                .loginLockBeginTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
    }

    @Test
    void testSelectByUserIdAndUserType() {
        when(loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1)).thenReturn(loginFailEntity);

        LoginFailEntity result = loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1);

        assertNotNull(result);
        assertEquals(loginFailEntity.getUserId(), result.getUserId());
        assertEquals(loginFailEntity.getUserType(), result.getUserType());
    }

    @Test
    void testDeleteByUserIdAndUserType() {
        doNothing().when(loginFailDao).deleteByUserIdAndUserType(BigInteger.valueOf(1), 1);
        when(loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1)).thenReturn(null);

        loginFailDao.deleteByUserIdAndUserType(BigInteger.valueOf(1), 1);

        LoginFailEntity result = loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1);
        assertNull(result);
    }

    @Test
    void testQueryPage() {
        Page<LoginFailVO> page = new Page<>(1, 10);
        LoginFailQueryForm queryForm = new LoginFailQueryForm();
        queryForm.setLoginName("testUser");
        queryForm.setLockFlag(false);

        List<LoginFailVO> mockResults = new ArrayList<>();
        LoginFailVO loginFailVO = new LoginFailVO();
        loginFailVO.setUserId(loginFailEntity.getUserId());
        loginFailVO.setUserType(loginFailEntity.getUserType());
        loginFailVO.setLoginName(loginFailEntity.getLoginName());
        loginFailVO.setLoginFailCount(loginFailEntity.getLoginFailCount());
        loginFailVO.setLockFlag(loginFailEntity.getLockFlag() ? 1 : 0);
        loginFailVO.setLoginLockBeginTime(loginFailEntity.getLoginLockBeginTime());
        loginFailVO.setCreateTime(loginFailEntity.getCreateTime());
        loginFailVO.setUpdateTime(loginFailEntity.getUpdateTime());
        mockResults.add(loginFailVO);

        when(loginFailDao.queryPage(any(Page.class), any(LoginFailQueryForm.class))).thenReturn(mockResults);

        List<LoginFailVO> results = loginFailDao.queryPage(page, queryForm);

        assertNotNull(results);
        assertFalse(results.isEmpty());

        LoginFailVO resultVO = results.get(0);
        assertEquals(loginFailEntity.getUserId(), resultVO.getUserId());
        assertEquals(loginFailEntity.getUserType(), resultVO.getUserType());
        assertEquals(loginFailEntity.getLoginName(), resultVO.getLoginName());
        assertEquals(loginFailEntity.getLoginFailCount(), resultVO.getLoginFailCount());
        assertEquals(loginFailEntity.getLockFlag() ? 1 : 0, resultVO.getLockFlag());
        assertEquals(loginFailEntity.getLoginLockBeginTime(), resultVO.getLoginLockBeginTime());
        assertEquals(loginFailEntity.getCreateTime(), resultVO.getCreateTime());
        assertEquals(loginFailEntity.getUpdateTime(), resultVO.getUpdateTime());
    }
}
