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
        // Initialize MockMvc object for testing the UserController
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Initialize UserAddForm with test data
        userAddForm = new UserAddForm();
        userAddForm.setLoginName("zzz9900@unsw.edu.au");
        userAddForm.setUserName("Test User");
        userAddForm.setLoginPwd("testPassword");

        // Initialize UserPwdUpdateForm with test data
        userPwdUpdateForm = new UserPwdUpdateForm();
        userPwdUpdateForm.setUserId(new BigInteger("1"));
        userPwdUpdateForm.setOldPassword("oldPassword123");
        userPwdUpdateForm.setNewPassword("newPassword123");

        // Initialize UserEntity with test data
        userEntity = new UserEntity();
        userEntity.setUserId(new BigInteger("1"));
        userEntity.setLoginName("testUser");
        userEntity.setUserName("Test User");
        userEntity.setLoginPwd("password");

        // Initialize ResponseDTO objects with test data
        responseDTOString = ResponseDTO.ok("Success");
        responseDTOUserEntity = ResponseDTO.ok(userEntity);
    }

    @Test
    void testSignup() throws Exception {
        // Mock the userService.addUser() to return a successful response
        when(userService.addUser(any(UserAddForm.class))).thenReturn(responseDTOString);

        // Perform a POST request to /signup and verify the response
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
        // Mock the userService.updatePassword() to return a successful response
        when(userService.updatePassword(any(UserPwdUpdateForm.class))).thenReturn(responseDTOString);

        // Perform a POST request to /user/updateUser and verify the response
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
        // Mock the userService.getByLoginName1() to return a user entity
        when(userService.getByLoginName1(anyString())).thenReturn(responseDTOUserEntity);

        // Perform a GET request to /user/getByLoginName/testUser and verify the response
        mockMvc.perform(get("/user/getByLoginName/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data.loginName").value("testUser"));
    }

    @Test
    void testGetById() throws Exception {
        // Mock the userService.getById1() to return a user entity
        when(userService.getById1(any(BigInteger.class))).thenReturn(responseDTOUserEntity);

        // Perform a GET request to /user/getById/1 and verify the response
        mockMvc.perform(get("/user/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data.userId").value("1"));
    }

    @Test
    void testDeleteById() throws Exception {
        // Mock the userService.deleteById() to return a successful response
        when(userService.deleteById(any(BigInteger.class))).thenReturn(responseDTOString);

        // Perform a POST request to /user/deleteById/1 and verify the response
        mockMvc.perform(post("/user/deleteById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseDTO.OK_CODE))
                .andExpect(jsonPath("$.msg").value(ResponseDTO.OK_MSG))
                .andExpect(jsonPath("$.data").value("Success"));
    }
}
