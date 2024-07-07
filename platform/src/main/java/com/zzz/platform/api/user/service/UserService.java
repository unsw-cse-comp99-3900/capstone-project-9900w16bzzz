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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private ProtectedPasswordService protectedPasswordService;

    /**
     * add user
     *
     */
    public synchronized ResponseDTO<String> addUser(UserAddForm userAddForm) {
        // Check for duplicate names
        UserEntity userEntity = userDao.selectOne(new QueryWrapper<UserEntity>().eq("login_name", userAddForm.getLoginName()));

        if (null != userEntity) {
            return ResponseDTO.userErrorParam("Duplicate login name");
        }

        UserEntity entity = BeanUtils.copy(userAddForm, UserEntity.class);
        // save data
        userDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * update pwd
     *
     */
    public ResponseDTO<String> updatePassword(UserPwdUpdateForm userPwdUpdateForm) {
        BigInteger userId = userPwdUpdateForm.getUserId();
        UserEntity employeeEntity = userDao.selectById(userId);
        if (employeeEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // check old pwd and new pwd
        if (!Objects.equals(userPwdUpdateForm.getOldPassword(), employeeEntity.getLoginPwd())) {
            return ResponseDTO.userErrorParam("The original password is wrong, please re-enter");
        }

        // old and new pwd are same
        String newPassword = userPwdUpdateForm.getNewPassword();
        if (Objects.equals(userPwdUpdateForm.getOldPassword(), newPassword)) {
            return ResponseDTO.userErrorParam("The new password is the same as the original password, please retype it");
        }

        // validate the pwd complexity
        ResponseDTO<String> validatePassComplexity = protectedPasswordService.validatePassComplexity(newPassword);
        if (!validatePassComplexity.getOk()) {
            return validatePassComplexity;
        }

        // update pwd
        UserEntity updateEntity = new UserEntity();
        updateEntity.setUserId(userId);
        updateEntity.setLoginPwd(newPassword);
        userDao.updateById(updateEntity);

        return ResponseDTO.ok();
    }

    /**
     * get user by login name
     *
     */
    public UserEntity getByLoginName(String loginName) {
        return userDao.getByLoginName(loginName);
    }
    public ResponseDTO<UserEntity> getByLoginName1(String loginName) {
        UserEntity userEntity = userDao.getByLoginName(loginName);
        return ResponseDTO.ok(userEntity);
    }

    /**
     * get user by id
     * @param userId
     * @return
     */
    public UserEntity getById(BigInteger userId) {
        return userDao.selectById(userId);
    }

    public ResponseDTO<UserEntity> getById1(BigInteger userId) {
        UserEntity userEntity = userDao.selectById(userId);
        return ResponseDTO.ok(userEntity);
    }

    /**
     * 删除操作
     * @param userId
     * @return
     */
    public ResponseDTO deleteById(BigInteger userId) {
        userDao.deleteById(userId);
        return ResponseDTO.ok();
    }
}
