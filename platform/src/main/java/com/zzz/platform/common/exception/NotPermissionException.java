package com.zzz.platform.common.exception;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class NotPermissionException extends RuntimeException {
    private static final long serialVersionUID = 6806129545290130141L;
    private final String permission;
    private final String loginType;

    public String getPermission() {
        return this.permission;
    }

    public String getLoginType() {
        return this.loginType;
    }

    public NotPermissionException(String permission) {
        this(permission, "");
    }

    public NotPermissionException(String permission, String loginType) {
        super("No permission：" + permission);
        this.permission = permission;
        this.loginType = loginType;
    }

}
