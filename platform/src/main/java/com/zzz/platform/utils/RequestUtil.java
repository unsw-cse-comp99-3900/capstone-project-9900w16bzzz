package com.zzz.platform.utils;

import com.zzz.platform.api.login.domain.RequestUser;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.WeakReference;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
@Slf4j
public class RequestUtil {

    private static final ThreadLocal<WeakReference<RequestUser>> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static void setRequestUser(RequestUser requestUser) {
        REQUEST_THREAD_LOCAL.set(new WeakReference<>(requestUser));
    }

    public static RequestUser getRequestUser() {
        WeakReference<RequestUser> weakReference = REQUEST_THREAD_LOCAL.get();
        if (weakReference != null) {
            return weakReference.get();
        } else {
            return null;
        }
    }

    public static Long getRequestUserId() {
        RequestUser requestUser = getRequestUser();
        return null == requestUser ? null : requestUser.getUserId();
    }


    public static void remove() {
        REQUEST_THREAD_LOCAL.remove();
    }


}
