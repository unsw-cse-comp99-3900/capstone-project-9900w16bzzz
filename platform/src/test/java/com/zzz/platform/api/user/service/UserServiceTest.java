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
        // Mock the userDao.selectOne() to return null, indicating the user doesn't already exist
        when(userDao.selectOne(any(QueryWrapper.class))).thenReturn(null);
        // Mock the userDao.insert() to return 1, indicating the user was successfully inserted
        when(userDao.insert(any(UserEntity.class))).thenReturn(1);

        // Call the addUser method with userAddForm
        ResponseDTO<String> response = userService.addUser(userAddForm);

        // Verify the response is successful
        assertTrue(response.getOk());
        assertEquals(ResponseDTO.OK_MSG, response.getMsg());
    }

    @Test
    void testAddUserDuplicateLoginName() {
        // Mock the userDao.selectOne() to return a userEntity, indicating a duplicate login name
        when(userDao.selectOne(any(QueryWrapper.class))).thenReturn(userEntity);

        // Call the addUser method with userAddForm
        ResponseDTO<String> response = userService.addUser(userAddForm);

        // Verify the response indicates failure due to duplicate login name
        assertFalse(response.getOk());
        assertEquals("Duplicate login name", response.getMsg());
    }

    @Test
    void testUpdatePasswordSuccess() {
        // Mock the userDao.selectById() to return userEntity, indicating the user exists
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);
        // Mock the protectedPasswordService.validatePassComplexity() to return a successful response
        when(protectedPasswordService.validatePassComplexity(anyString())).thenReturn(ResponseDTO.ok());

        // Call the updatePassword method with userPwdUpdateForm
        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        // Verify the response is successful
        assertTrue(response.getOk());
        assertEquals(ResponseDTO.OK_MSG, response.getMsg());
    }

    @Test
    void testUpdatePasswordUserNotFound() {
        // Mock the userDao.selectById() to return null, indicating the user does not exist
        when(userDao.selectById(any(BigInteger.class))).thenReturn(null);

        // Call the updatePassword method with userPwdUpdateForm
        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        // Verify the response indicates failure due to user not existing
        assertFalse(response.getOk());
        assertEquals("user not exist", response.getMsg()); // Adjust according to the actual error message in your code
    }

    @Test
    void testUpdatePasswordWrongOldPassword() {
        userPwdUpdateForm.setOldPassword("wrongPassword");
        // Mock the userDao.selectById() to return userEntity, indicating the user exists
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        // Call the updatePassword method with userPwdUpdateForm
        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        // Verify the response indicates failure due to wrong old password
        assertFalse(response.getOk());
        assertEquals("The original password is wrong, please re-enter", response.getMsg());
    }

    @Test
    void testUpdatePasswordSameOldAndNewPassword() {
        userPwdUpdateForm.setNewPassword("oldPassword123");
        // Mock the userDao.selectById() to return userEntity, indicating the user exists
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        // Call the updatePassword method with userPwdUpdateForm
        ResponseDTO<String> response = userService.updatePassword(userPwdUpdateForm);

        // Verify the response indicates failure due to new password being the same as the old password
        assertFalse(response.getOk());
        assertEquals("The new password is the same as the original password, please retype it", response.getMsg());
    }

    @Test
    void testGetByLoginName() {
        // Mock the userDao.getByLoginName() to return userEntity
        when(userDao.getByLoginName(anyString())).thenReturn(userEntity);

        // Call the getByLoginName method with a specific login name
        UserEntity result = userService.getByLoginName("zzz9900@unsw.edu.au");

        // Verify the result is not null and matches the expected login name
        assertNotNull(result);
        assertEquals("zzz9900@unsw.edu.au", result.getLoginName());
    }

    @Test
    void testGetByLoginName1() {
        // Mock the userDao.getByLoginName() to return userEntity
        when(userDao.getByLoginName(anyString())).thenReturn(userEntity);

        // Call the getByLoginName1 method with a specific login name
        ResponseDTO<UserEntity> response = userService.getByLoginName1("zzz9900@unsw.edu.au");

        // Verify the response is successful and contains the expected user entity
        assertTrue(response.getOk());
        assertEquals(userEntity, response.getData());
    }

    @Test
    void testGetById() {
        // Mock the userDao.selectById() to return userEntity
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        // Call the getById method with a specific user ID
        UserEntity result = userService.getById(new BigInteger("1"));

        // Verify the result is not null and matches the expected user ID
        assertNotNull(result);
        assertEquals(new BigInteger("1"), result.getUserId());
    }

    @Test
    void testGetById1() {
        // Mock the userDao.selectById() to return userEntity
        when(userDao.selectById(any(BigInteger.class))).thenReturn(userEntity);

        // Call the getById1 method with a specific user ID
        ResponseDTO<UserEntity> response = userService.getById1(new BigInteger("1"));

        // Verify the response is successful and contains the expected user entity
        assertTrue(response.getOk());
        assertEquals(userEntity, response.getData());
    }

    @Test
    void testDeleteById() {
        // Mock the userDao.deleteById() to return 1, indicating successful deletion
        when(userDao.deleteById(any(BigInteger.class))).thenReturn(1);

        // Call the deleteById method with a specific user ID
        ResponseDTO response = userService.deleteById(new BigInteger("1"));

        // Verify the response is successful
        assertTrue(response.getOk());
        assertEquals(ResponseDTO.OK_MSG, response.getMsg());
    }
}
