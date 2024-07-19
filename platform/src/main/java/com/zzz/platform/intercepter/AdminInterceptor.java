package com.zzz.platform.intercepter;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import com.zzz.platform.api.login.domain.RequestUser;
import com.zzz.platform.api.login.service.LoginService;
import com.zzz.platform.common.annoation.NoNeedLogin;
import com.zzz.platform.common.code.SystemErrorCode;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.common.domain.SystemEnvironment;
import com.zzz.platform.common.enumeration.SystemEnvironmentEnum;
import com.zzz.platform.utils.RequestUtil;
import com.zzz.platform.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Resource
    private SystemEnvironment systemEnvironment;

    @Value("${sa-token.active-timeout}")
    private long tokenActiveTimeout;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // if http method is OPTION then return false
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }

        boolean isHandler = handler instanceof HandlerMethod;
        if (!isHandler) {
            return true;
        }

        try {
            /* First step: get user by token value */
            String tokenValue = StpUtil.getTokenValue();
            String loginId = (String) StpUtil.getLoginIdByToken(tokenValue);
            RequestUser requestUser = loginService.getLoginUser(loginId, request);

            /* Second step: validate login */
            Method method = ((HandlerMethod) handler).getMethod();
            NoNeedLogin noNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class);
            if (noNeedLogin != null) {
                checkActiveTimeout(requestUser);
                return true;
            }

            if (requestUser == null) {
                ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
                return false;
            }

            // Detecting token activity frequency
            checkActiveTimeout(requestUser);


            /* Third step: validate authentication */
            RequestUtil.setRequestUser(requestUser);
            if (SaStrategy.instance.isAnnotationPresent.apply(method, SaIgnore.class)) {
                return true;
            }
            SaStrategy.instance.checkMethodAnnotation.accept(method);
        } catch (SaTokenException e) {
            /*
             * sa-token error code
             */
            int code = e.getCode();
            if (code == 11041 || code == 11051) {
                ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.NO_PERMISSION));
            } else if (code == 11016) {
                ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_ACTIVE_TIMEOUT));
            } else if (code >= 11011 && code <= 11015) {
                ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
            } else {
                ResponseUtil.write(response, ResponseDTO.error(UserErrorCode.PARAM_ERROR));
            }
            return false;
        } catch (Throwable e) {
            ResponseUtil.write(response, ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR));
            log.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // clean context
        RequestUtil.remove();
        // Development environment, disable temporary user switching for sa token
        if (systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.DEV) {
            StpUtil.endSwitch();
        }
    }

    /**
     * Detection: the minimum active frequency of the token (in seconds),
     * if the token does not access the system for more than this time it will be frozen
     */
    private void checkActiveTimeout(RequestUser requestUser) {
        // Users are not online and do not need to be detected
        if (requestUser == null) {
            return;
        }

        // Less than 1 and no test is required
        if (tokenActiveTimeout < 1) {
            return;
        }
        StpUtil.checkActiveTimeout();
        StpUtil.updateLastActiveToNow();
    }
}


