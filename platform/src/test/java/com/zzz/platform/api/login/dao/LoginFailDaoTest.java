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
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
        // Set up a LoginFailEntity with test data
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
        // Mock the LoginFailDao to return the LoginFailEntity for specific userId and userType
        when(loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1)).thenReturn(loginFailEntity);

        // Call the selectByUserIdAndUserType method and validate the result
        LoginFailEntity result = loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1);

        // Verify the result is not null and matches the expected values
        assertNotNull(result);
        assertEquals(loginFailEntity.getUserId(), result.getUserId());
        assertEquals(loginFailEntity.getUserType(), result.getUserType());
    }

    @Test
    void testDeleteByUserIdAndUserType() {
        // Mock the deleteByUserIdAndUserType method to do nothing
        doNothing().when(loginFailDao).deleteByUserIdAndUserType(BigInteger.valueOf(1), 1);
        // Mock the selectByUserIdAndUserType method to return null after deletion
        when(loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1)).thenReturn(null);

        // Call the deleteByUserIdAndUserType method
        loginFailDao.deleteByUserIdAndUserType(BigInteger.valueOf(1), 1);

        // Verify the record is deleted by checking the result of selectByUserIdAndUserType
        LoginFailEntity result = loginFailDao.selectByUserIdAndUserType(BigInteger.valueOf(1), 1);
        assertNull(result);
    }

    @Test
    void testQueryPage() {
        // Set up a Page object for pagination
        Page<LoginFailVO> page = new Page<>(1, 10);
        // Set up a LoginFailQueryForm with query criteria
        LoginFailQueryForm queryForm = new LoginFailQueryForm();
        queryForm.setLoginName("testUser");
        queryForm.setLockFlag(false);

        // Create a mock list of LoginFailVO as the expected result
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

        // Mock the queryPage method to return the mock results
        when(loginFailDao.queryPage(any(Page.class), any(LoginFailQueryForm.class))).thenReturn(mockResults);

        // Call the queryPage method and validate the result
        List<LoginFailVO> results = loginFailDao.queryPage(page, queryForm);

        // Verify the results are not null and not empty
        assertNotNull(results);
        assertFalse(results.isEmpty());

        // Validate the first result in the list
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
