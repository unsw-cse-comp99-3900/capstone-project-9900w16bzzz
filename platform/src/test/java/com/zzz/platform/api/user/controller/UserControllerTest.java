package com.zzz.platform.api.user.controller;

import com.zzz.platform.api.user.domain.UserAddForm;
import com.zzz.platform.api.user.domain.UserPwdUpdateForm;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.domain.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserAddForm userAddForm;
    private UserPwdUpdateForm userPwdUpdateForm;
    private UserEntity userEntity;
    private ResponseDTO<String> responseDTOString;
    private ResponseDTO<UserEntity> responseDTOUserEntity;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

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
        userEntity.setLoginName("testUser");
        userEntity.setUserName("Test User");
        userEntity.setLoginPwd("password");

        responseDTOString = ResponseDTO.ok("Success");
        responseDTOUserEntity = ResponseDTO.ok(userEntity);
    }

    @Test
    void testSignup() throws Exception {
        when(userService.addUser(any(UserAddForm.class))).thenReturn(responseDTOString);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userAddForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data").value("Success"));
    }

    @Test
    void testUpdatePassword() throws Exception {
        when(userService.updatePassword(any(UserPwdUpdateForm.class))).thenReturn(responseDTOString);

        mockMvc.perform(post("/user/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userPwdUpdateForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data").value("Success"));
    }

    @Test
    void testGetByLoginName() throws Exception {
        when(userService.getByLoginName1(anyString())).thenReturn(responseDTOUserEntity);

        mockMvc.perform(get("/user/getByLoginName/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data.loginName").value("testUser"));
    }

    @Test
    void testGetById() throws Exception {
        when(userService.getById1(any(BigInteger.class))).thenReturn(responseDTOUserEntity);

        mockMvc.perform(get("/user/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data.userId").value("1"));
    }

    @Test
    void testDeleteById() throws Exception {
        when(userService.deleteById(any(BigInteger.class))).thenReturn(responseDTOString);

        mockMvc.perform(post("/user/deleteById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data").value("Success"));
    }
}
