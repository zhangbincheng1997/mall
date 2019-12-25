package com.example.demo.base;

import lombok.Getter;

/**
 * 状态码
 */
@Getter
public enum Status {

    // 服务器
    SUCCESS(0, "SUCCESS"),
    FAILURE(250, "FAILURE"),
    FORBIDDEN(403, "拒绝执行"),
    NOT_FOUND(404, "请求失败"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    BIND_EXCEPTION(1000, "参数错误"),

    // 认证权限
    ACCESS_DENIED(1001, "权限不足:拒绝访问"),
    AUTHENTICATION_ENTRY_POINT(1002, "权限不足：认证入口点"),
    AUTHENTICATION_FAILURE(1003, "认证失败"),
    USERNAME_NOT_FOUND(1004, "认证失败：用户名不存在"),
    BAD_CREDENTIALS(1005, "认证失败：密码错误"),
    ACCOUNT_EXPIRED(1006, "认证失败：用户过期"),
    ACCOUNT_LOCKED(1007, "认证失败：用户锁定"),
    CREDENTIALS_EXPIRED(1008, "认证失败：证书过期"),
    DISABLED(1009, "认证失败：用户不可用"),

    // 访问限制
    ACCESS_LIMIT(10000, "您已超出访问限制"),

    // 验证码
    CAPTCHA_EXPIRE(20000, "验证码过期"),
    CAPTCHA_ERROR(20001, "验证码错误"),

    // 账号相关
    USERNAME_EXIST(50010, "账号已经存在"),
    USERNAME_NOT_EXIST(50011, "账号不存在");

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
