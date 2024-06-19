package com.zzz.platform.handler;

import com.zzz.platform.common.domain.SystemEnvironment;
import com.zzz.platform.common.code.SystemErrorCode;
import com.zzz.platform.common.code.UserErrorCode;
import com.zzz.platform.common.enumeration.SystemEnvironmentEnum;
import com.zzz.platform.common.exception.BusinessException;
import com.zzz.platform.common.exception.NotPermissionException;
import com.zzz.platform.common.domain.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler
 *
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @Resource
    private SystemEnvironment systemEnvironment;

    /**
     * json format error
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseDTO<?> jsonFormatExceptionHandler(Exception e) {
        if (!systemEnvironment.isProd()) {
            log.error("Global JSON format error exception,URL:{}", getCurrentRequestUrl(), e);
        }
        return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "Parameter JSON format error");
    }

    /**
     * json Format error. Missing request body
     */
    @ResponseBody
    @ExceptionHandler({TypeMismatchException.class, BindException.class})
    public ResponseDTO<?> paramExceptionHandler(Exception e) {
        if (!systemEnvironment.isProd()) {
            log.error("global parameter exception,URL:{}", getCurrentRequestUrl(), e);
        }

        if (e instanceof BindException) {
            if (e instanceof MethodArgumentNotValidException) {
                List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
                List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, String.join(",", msgList));
            }

            List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            List<String> error = fieldErrors.stream().map(field -> field.getField() + ":" + field.getRejectedValue()).collect(Collectors.toList());
            String errorMsg = UserErrorCode.PARAM_ERROR.getMsg() + ":" + error;
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, errorMsg);
        }

        return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
    }

    /**
     *  tackle login permission
     *
     * @param e permission error
     * @return error result
     */
    @ResponseBody
    @ExceptionHandler(NotPermissionException.class)
    public ResponseDTO<String> permissionException(NotPermissionException e) {
        // prod environment
        if (SystemEnvironmentEnum.PROD != systemEnvironment.getCurrentEnvironment()) {
            return ResponseDTO.error(UserErrorCode.NO_PERMISSION, e.getMessage());
        }
        return ResponseDTO.error(UserErrorCode.NO_PERMISSION);
    }


    /**
     * business exception
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseDTO<?> businessExceptionHandler(BusinessException e) {
        if (!systemEnvironment.isProd()) {
            log.error("Global business error,URL:{}", getCurrentRequestUrl(), e);
        }
        return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    /**
     * all error
     *
     * @param e global exception
     * @return exception result
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseDTO<?> errorHandler(Throwable e) {
        log.error("catch global exception,URL:{}", getCurrentRequestUrl(), e);
        return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, systemEnvironment.isProd() ? null : e.toString());
    }

    /**
     * get current request url
     */
    private String getCurrentRequestUrl() {
        RequestAttributes request = RequestContextHolder.getRequestAttributes();
        if (null == request) {
            return null;
        }
        ServletRequestAttributes servletRequest = (ServletRequestAttributes) request;
        return servletRequest.getRequest().getRequestURI();
    }
}
