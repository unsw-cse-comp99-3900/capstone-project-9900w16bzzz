package com.zzz.platform.api.user.dao;

import com.zzz.platform.api.user.domain.UserVO;
import com.zzz.platform.api.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private UserDao userDao;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Initialize a UserEntity object with test data
        userEntity = new UserEntity();
        userEntity.setUserId(new BigInteger("1"));
        userEntity.setLoginName("testUser");
        userEntity.setUserName("Test User");
        userEntity.setLoginPwd("password");
        userEntity.setUpdateTime(LocalDateTime.now());
        userEntity.setCreateTime(LocalDateTime.now());
    }

    @Test
    void testGetByLoginName() {
        // Mock the userDao.getByLoginName() to return the userEntity
        when(userDao.getByLoginName(anyString())).thenReturn(userEntity);

        // Call the getByLoginName method with a specific login name
        UserEntity found = userDao.getByLoginName("testUser");

        // Verify the result is not null and matches the expected login name and password
        assertThat(found).isNotNull();
        assertThat(found.getLoginName()).isEqualTo("testUser");
        assertThat(found.getLoginPwd()).isEqualTo("password");

        // Verify that the userDao.getByLoginName() method was called once
        verify(userDao, times(1)).getByLoginName("testUser");
    }

    @Test
    void testGetUserById() {
        // Create a UserVO object to be returned by the mock
        UserVO userVO = new UserVO();
        // Mock the userDao.getUserById() to return the userVO
        when(userDao.getUserById(any(BigInteger.class))).thenReturn(userVO);

        // Call the getUserById method with a specific user ID
        UserVO found = userDao.getUserById(new BigInteger("1"));

        // Verify the result is not null
        assertThat(found).isNotNull();
        // Verify that the userDao.getUserById() method was called once
        verify(userDao, times(1)).getUserById(new BigInteger("1"));
    }

    @Test
    void testUpdatePassword() {
        // Mock the userDao.updatePassword() to return 1, indicating success
        when(userDao.updatePassword(anyInt(), anyString())).thenReturn(1);

        // Call the updatePassword method with specific parameters
        Integer result = userDao.updatePassword(1, "newPassword");

        // Verify the result is 1, indicating success
        assertThat(result).isEqualTo(1);
        // Verify that the userDao.updatePassword() method was called once
        verify(userDao, times(1)).updatePassword(1, "newPassword");
    }

    @Test
    void testAddUser() {
        // Mock the userDao.addUser() to return 1, indicating success
        when(userDao.addUser(anyString(), anyString())).thenReturn(1);

        // Call the addUser method with specific parameters
        Integer result = userDao.addUser("newUser", "newPassword");

        // Verify the result is 1, indicating success
        assertThat(result).isEqualTo(1);
        // Verify that the userDao.addUser() method was called once
        verify(userDao, times(1)).addUser("newUser", "newPassword");
    }
}
