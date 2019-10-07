package com.example.demo.base;

/**
 * 状态码
 */
public enum Status {

    // 服务器
    SUCCESS(200, "SUCCESS"),
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
    CODE_EXPIRED(10002, "验证码过期"),
    CODE_ERROR(10003, "验证码错误"),

    // 账号相关
    USERNAME_EXIST(50010, "账号已经存在"),
    USERNAME_NOT_EXIST(50011, "账号不存在");

    private int code;
    private String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
