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
        when(userDao.getByLoginName(anyString())).thenReturn(userEntity);

        UserEntity found = userDao.getByLoginName("testUser");

        assertThat(found).isNotNull();
        assertThat(found.getLoginName()).isEqualTo("testUser");
        assertThat(found.getLoginPwd()).isEqualTo("password");

        verify(userDao, times(1)).getByLoginName("testUser");
    }

    @Test
    void testGetUserById() {
        UserVO userVO = new UserVO();
        when(userDao.getUserById(any(BigInteger.class))).thenReturn(userVO);

        UserVO found = userDao.getUserById(new BigInteger("1"));

        assertThat(found).isNotNull();
        verify(userDao, times(1)).getUserById(new BigInteger("1"));
    }

    @Test
    void testUpdatePassword() {
        when(userDao.updatePassword(anyInt(), anyString())).thenReturn(1);

        Integer result = userDao.updatePassword(1, "newPassword");

        assertThat(result).isEqualTo(1);
        verify(userDao, times(1)).updatePassword(1, "newPassword");
    }

    @Test
    void testAddUser() {
        when(userDao.addUser(anyString(), anyString())).thenReturn(1);

        Integer result = userDao.addUser("newUser", "newPassword");

        assertThat(result).isEqualTo(1);
        verify(userDao, times(1)).addUser("newUser", "newPassword");
    }
}
