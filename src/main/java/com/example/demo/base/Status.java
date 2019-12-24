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
    ACCESS_DENIED(1001, "权限不足"),
    AUTHENTICATION_ENTRY_POINT(1002, "认证失败"),

    // 访问限制
    ACCESS_LIMIT(10000, "您已超出访问限制"),
    NOT_LOGIN(10001, "用户未登录"),

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
