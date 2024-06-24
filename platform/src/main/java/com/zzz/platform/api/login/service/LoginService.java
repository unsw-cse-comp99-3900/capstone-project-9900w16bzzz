package com.zzz.platform.api.login.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.zzz.platform.api.login.domain.LoginForm;
import com.zzz.platform.api.login.domain.LoginResultVO;
import com.zzz.platform.api.login.domain.RequestUser;
import com.zzz.platform.api.login.domain.captcha.CaptchaVO;
import com.zzz.platform.api.login.entity.LoginFailEntity;
import com.zzz.platform.api.user.entity.UserEntity;
import com.zzz.platform.api.user.service.UserService;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.constant.RequestHeaderConst;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.enumeration.UserTypeEnum;
import com.zzz.platform.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Service
@Slf4j
public class LoginService {

    /**
     * Maximum online cache size
     */
    private static final long CACHE_MAX_ONLINE_PERSON_COUNT = 1000L;

    /**
     * Secondary Cache for Login Information
     */
    private final ConcurrentMap<Long, RequestUser> loginUserCache = new ConcurrentLinkedHashMap.Builder<Long, RequestUser>().maximumWeightedCapacity(CACHE_MAX_ONLINE_PERSON_COUNT).build();

    @Resource
    private CaptchaService captchaService;

    @Resource
    private ProtectedPasswordService protectedPasswordService;

    @Resource
    private ProtectLoginService protectLoginService;

    @Resource
    private UserService userService;

    /**
     * get captcha
     * @return captcha
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

    public ResponseDTO<LoginResultVO> login(LoginForm loginForm, String ip, String userAgent) {
        // check captcha image
        ResponseDTO<String> checkCaptcha = captchaService.checkCaptcha(loginForm);
        if (!checkCaptcha.getOk()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, checkCaptcha.getMsg());
        }

        // verify login name
        UserEntity userEntity = userService.getByLoginName(loginForm.getLoginName());
        if (null == userEntity) {
            return ResponseDTO.userErrorParam("Login name does not exist! ");
        }
        // Decrypting front-end encrypted passwords
        String requestPassword = loginForm.getLoginPwd();
        // String requestPassword = protectedPasswordService.decryptPassword(loginForm.getLoginPwd());

        // Log-in failures were verified in accordance with the requirements for isochronous logins
        ResponseDTO<LoginFailEntity> loginFailEntityResponseDTO = protectLoginService.checkLogin(userEntity.getUserId(), UserTypeEnum.NORMAL);
        if (!loginFailEntityResponseDTO.getOk()) {
            return ResponseDTO.error(loginFailEntityResponseDTO);
        }

        // pwd incorrect
        if (!userEntity.getLoginPwd().equals(requestPassword)) {
            // Record Login Failure
            log.info("User {} login password error", userEntity.getLoginName());
            // Number of times level of protection was recorded
            String msg = protectLoginService.recordLoginFail(userEntity.getUserId(), UserTypeEnum.NORMAL, userEntity.getLoginName(), loginFailEntityResponseDTO.getData());
            return msg == null ? ResponseDTO.userErrorParam("Login name or password incorrect！") : ResponseDTO.error(UserErrorCode.LOGIN_FAIL_WILL_LOCK, msg);
        }

        String tokenLoginId = UserTypeEnum.NORMAL.getValue() + "," + userEntity.getUserId();
        // try login
        StpUtil.login(tokenLoginId);

        RequestUser requestUser = BeanUtils.copy(userEntity, RequestUser.class);
        requestUser.setIp(ip);
        requestUser.setUserAgent(userAgent);
        // save in cache
        loginUserCache.put(userEntity.getUserId(), requestUser);

        // remove failed login
        protectLoginService.removeLoginFail(userEntity.getUserId(), UserTypeEnum.NORMAL);

        // get login result
        LoginResultVO loginResultVO = new LoginResultVO();

        //record login success
        log.info("User: {} login success.", loginForm.getLoginName());

        // set token
        loginResultVO.setToken(StpUtil.getTokenValue());

        return ResponseDTO.ok(loginResultVO);
    }

    /**
     * Obtain information about the requesting worker based on the login token
     */
    public RequestUser getLoginUser(String loginId, HttpServletRequest request) {
        if (loginId == null) {
            return null;
        }

        Long requestEmployeeId = getUserIdByLoginId(loginId);
        if (requestEmployeeId == null) {
            return null;
        }

        RequestUser requestUser = loginUserCache.get(requestEmployeeId);
        if (requestUser == null) {
            // user entity
            UserEntity userEntity = userService.getById(requestEmployeeId);
            if (userEntity == null) {
                return null;
            }

            requestUser = BeanUtils.copy(userEntity, RequestUser.class);
            loginUserCache.put(requestEmployeeId, requestUser);
        }

        // update request ip and user agent
        requestUser.setUserAgent(ServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT));
        requestUser.setIp(ServletUtil.getClientIP(request));

        return requestUser;
    }

    /**
     * according loginId get user id
     */
    Long getUserIdByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }
        try {
            String userId = loginId.substring(2);
            return Long.parseLong(userId);
        } catch (Exception e) {
            log.error("loginId parse error , loginId : {}", loginId, e);
            return null;
        }
    }


    /**
     * logout
     */
    public ResponseDTO<String> logout(String token, RequestUser requestUser) {

        // sa token logout
        StpUtil.logoutByTokenValue(token);

        // clear login cache
        loginUserCache.remove(requestUser.getUserId());

        log.info("User: {} logout.", requestUser.getUserId());

        return ResponseDTO.ok();
    }

}
