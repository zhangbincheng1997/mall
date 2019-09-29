package com.example.demo.base;

/**
 * 状态码
 */
public enum Status {

    // 服务器
    SUCCESS(200, "SUCCESS"),
    FAILED(250, "FAILED"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BIND_EXCEPTION(1000, "Bind Exception"),
    RUNTIME_EXCEPTION(1001, "Runtime Exception"),
    IO_EXCEPTION(1002, "IO Exception"),

    // 访问限制
    ACCESS_LIMIT(10000, "您已超出访问限制"),
    NOT_LOGIN(10001, "用户未登录"),
    CODE_EXPIRED(10002, "验证码过期"),
    CODE_ERROR(10003, "验证码错误"),
    TOKEN_ERROR(10004, "没有登录，token无效"),
    PERMISSION_DENIED(10005, "权限不足"),

    // 账号相关
    USERNAME_EXIST(50010, "账号已经存在"),
    USERNAME_NOT_EXIST(50011, "账号不存在"),
    EMAIL_EXIST(50012, "邮箱已经存在"),
    EMAIL_NOT_EXIST(50013, "邮箱不存在"),
    USERNAME_PASSWORD_ERROR(50014, "用户名或者密码错误"),
    USERNAME_PASSWORD_EXIST(50015, "用户名或者密码存在");

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
