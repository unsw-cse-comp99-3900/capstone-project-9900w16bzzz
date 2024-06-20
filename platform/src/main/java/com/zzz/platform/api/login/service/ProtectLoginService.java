package com.zzz.platform.api.login.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzz.platform.api.login.dao.LoginFailDao;
import com.zzz.platform.api.login.domain.LoginFailQueryForm;
import com.zzz.platform.api.login.domain.LoginFailVO;
import com.zzz.platform.api.login.entity.LoginFailEntity;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.domain.PageResult;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.UserTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Service
public class ProtectLoginService {

    private static final String LOGIN_LOCK_MSG = "You have failed to log in %s times in a row, the account is locked for %s minutes, the unlock time is: %s, please be patient!";

    private static final String LOGIN_FAIL_MSG = "Wrong login name or password! Login failed %s times in a row and the account will be locked for %s minutes! You can try again %s times!";

    /**
     * The number of consecutive failed login attempts will lock you out, -1 means there is no restriction
     * and you can log in all the time.
     */
    @Value("${classified-protect.login-max-fail-times}")
    private Integer loginMaxFailTimes;

    /**
     * Continuous Login Failure Lockout Time (unit: seconds), -1 means no lockout
     */
    @Value("${classified-protect.login-fail-locked-seconds}")
    private Integer loginFailLockedSeconds;

    @Resource
    private LoginFailDao loginFailDao;


    /**
     * Check if u can log in
     *
     * @param userId
     * @param userType
     * @return
     */
    public ResponseDTO<LoginFailEntity> checkLogin(Long userId, UserTypeEnum userType) {

        // no need validate
        if (loginMaxFailTimes < 1) {
            return ResponseDTO.ok();
        }


        LoginFailEntity loginFailEntity = loginFailDao.selectByUserIdAndUserType(userId, userType.getValue());
        if (loginFailEntity == null) {
            return ResponseDTO.ok();
        }

        // fail count
        if (loginFailEntity.getLoginFailCount() < loginMaxFailTimes) {
            return ResponseDTO.ok(loginFailEntity);
        }

        // validate lock time
        if(loginFailEntity.getLoginLockBeginTime().plusSeconds(loginFailLockedSeconds).isBefore(LocalDateTime.now())){
            // pass lock
            return ResponseDTO.ok(loginFailEntity);
        }

        LocalDateTime unlockTime = loginFailEntity.getLoginLockBeginTime().plusSeconds(loginFailLockedSeconds);
        return ResponseDTO.error(UserErrorCode.LOGIN_FAIL_LOCK, String.format(LOGIN_LOCK_MSG, loginFailEntity.getLoginFailCount(), loginFailLockedSeconds / 60, LocalDateTimeUtil.formatNormal(unlockTime)));
    }

    /**
     * record after login failed
     *
     * @param userId
     * @param userType
     * @param loginFailEntity
     */
    public String recordLoginFail(Long userId, UserTypeEnum userType, String loginName, LoginFailEntity loginFailEntity) {

        // no need validate
        if (loginMaxFailTimes < 1) {
            return null;
        }

        if (loginFailEntity == null) {
            loginFailEntity = LoginFailEntity.builder()
                    .userId(userId)
                    .userType(userType.getValue())
                    .loginName(loginName)
                    .loginFailCount(1)
                    .lockFlag(false)
                    .loginLockBeginTime(null).build();
            loginFailDao.insert(loginFailEntity);
        } else {

            // Recalculate if it is an already locked state
            if(loginFailEntity.getLockFlag()){
                loginFailEntity.setLockFlag(false);
                loginFailEntity.setLoginFailCount(1);
                loginFailEntity.setLoginLockBeginTime(null);
            }else{
                loginFailEntity.setLoginLockBeginTime(LocalDateTime.now());
                loginFailEntity.setLoginFailCount(loginFailEntity.getLoginFailCount() + 1);
                loginFailEntity.setLockFlag(loginFailEntity.getLoginFailCount() >= loginMaxFailTimes);
            }

            loginFailEntity.setLoginName(loginName);
            loginFailDao.updateById(loginFailEntity);
        }

        // alert info
        if (loginFailEntity.getLoginFailCount() >= loginMaxFailTimes) {
            LocalDateTime unlockTime = loginFailEntity.getLoginLockBeginTime().plusSeconds(loginFailLockedSeconds);
            return String.format(LOGIN_LOCK_MSG, loginFailEntity.getLoginFailCount(), loginFailLockedSeconds / 60, LocalDateTimeUtil.formatNormal(unlockTime));
        } else {
            return String.format(LOGIN_FAIL_MSG, loginMaxFailTimes, loginFailLockedSeconds / 60, loginMaxFailTimes - loginFailEntity.getLoginFailCount());
        }
    }

    /**
     * remove login fail record
     *
     * @param userId
     * @param userType
     */
    public void removeLoginFail(Long userId, UserTypeEnum userType) {
        // no need validate
        if (loginMaxFailTimes < 1) {
            return;
        }

        loginFailDao.deleteByUserIdAndUserType(userId, userType.getValue());
    }

    /**
     * query page
     *
     * @param queryForm
     * @return
     */
    public PageResult<LoginFailVO> queryPage(LoginFailQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<LoginFailVO> list = loginFailDao.queryPage(page, queryForm);
        PageResult<LoginFailVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    /**
     * batch delete
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        loginFailDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }
}
