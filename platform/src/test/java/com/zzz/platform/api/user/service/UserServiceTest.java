package com.zzz.platform.api.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzz.platform.api.login.service.ProtectedPasswordService;
import com.zzz.platform.api.user.dao.UserDao;
import com.zzz.platform.api.user.domain.UserAddForm;
import com.zzz.platform.api.user.domain.UserPwdUpdateForm;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.utils.BeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private ProtectedPasswordService protectedPasswordService;

    @InjectMocks
    private UserService userService;

    private UserAddForm userAddForm;
    private UserPwdUpdateForm userPwdUpdateForm;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userAddForm = new UserAddForm();
        userAddForm.setLoginName("zzz9900@unsw.edu.au");
        userAddForm.setUserName("Test User");
        userAddForm.setLoginPwd("testPassword");

        userPwdUpdateForm = new UserPwdUpdateForm();
        userPwdUpdateForm.setUserId(new BigInteger("1"));
        userPwdUpdateForm.setOldPassword("oldPassword123");
        userPwdUpdateForm.setNewPassword("newPassword123");

        userEntity = new UserEntity();
        userEntity.setUserId(new BigInteger("1"));
        userEntity.setLoginName("zzz9900@unsw.edu.au");
        userEntity.setUserName("Test User");
        userEntity.setLoginPwd("oldPassword123");
    }

    @Test
    void testAddUserSuccess() {
        when(userDao.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(userDao.insert(any(UserEntity.class))).thenReturn(1);

        ResponseDTO<String> response = userService.addUser(userAddForm);

        assertTrue(response.getOk());
        assertEquals(ResponseDTO.OK_MSG, response.getMsg());
    }

    @Test
    void testAddUserDuplicateLoginName() {
        when(userDao.selectOne(any(QueryWrapper.class))).thenReturn(userEntity);

        ResponseDTO<String> response = userService.addUser(userAddForm);

        assertFalse(response.getOk());
        assertEquals("Duplicate login name", response.getMsg());
    }

    @Test
    void testUpdatePasswordSuccess() {
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);
        when(protectedPasswordService.validatePassComplexity(anyString())).thenReturn(ResponseDTO.ok());

        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        assertTrue(response.getOk());
        assertEquals(ResponseDTO.OK_MSG, response.getMsg());
    }

    @Test
    void testUpdatePasswordUserNotFound() {
        when(userDao.selectById(any(BigInteger.class))).thenReturn(null);

        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        assertFalse(response.getOk());
        assertEquals("user not exist", response.getMsg()); // Adjust according to the actual error message in your code
    }

    @Test
    void testUpdatePasswordWrongOldPassword() {
        userPwdUpdateForm.setOldPassword("wrongPassword");
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        assertFalse(response.getOk());
        assertEquals("The original password is wrong, please re-enter", response.getMsg());
    }

    @Test
    void testUpdatePasswordSameOldAndNewPassword() {
        userPwdUpdateForm.setNewPassword("oldPassword123");
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        assertFalse(response.getOk());
        assertEquals("The new password is the same as the original password, please retype it", response.getMsg());
    }

    @Test
    void testGetByLoginName() {
        when(userDao.getByLoginName(anyString())).thenReturn(userEntity);

        UserEntity result = userService.getByLoginName("zzz9900@unsw.edu.au");

        assertNotNull(result);
        assertEquals("zzz9900@unsw.edu.au", result.getLoginName());
    }

    @Test
    void testGetByLoginName1() {
        when(userDao.getByLoginName(anyString())).thenReturn(userEntity);

        ResponseDTO<UserEntity> response = userService.getByLoginName1("zzz9900@unsw.edu.au");

        assertTrue(response.getOk());
        assertEquals(userEntity, response.getData());
    }

    @Test
    void testGetById() {
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        UserEntity result = userService.getById(new BigInteger("1"));

        assertNotNull(result);
        assertEquals(new BigInteger("1"), result.getUserId());
    }

    @Test
    void testGetById1() {
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        ResponseDTO<UserEntity> response = userService.getById1(new BigInteger("1"));

        assertTrue(response.getOk());
        assertEquals(userEntity, response.getData());
    }

    @Test
    void testDeleteById() {
        when(userDao.deleteById(any(BigInteger.class))).thenReturn(1);

        ResponseDTO response = userService.deleteById(new BigInteger("1"));

        assertTrue(response.getOk());
        assertEquals(ResponseDTO.OK_MSG, response.getMsg());
    }
}
