package com.example.demo.base;

/**
 * 状态码
 */
public enum Status {

    // 服务器
    SUCCESS(200, "OK"),
    Forbidden(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BIND_EXCEPTION(1000, "Bind Exception"),
    RUNTIME_EXCEPTION(1001, "Runtime Exception"),
    IO_EXCEPTION(1002, "IO Exception"),

    // 登录
    NOT_LOGIN(50000, "用户未登录"),
    COOKIE_ERROR(50001, "COOKIE无效"),

    // 账号相关
    MOBILE_EXIST(50010, "手机号码已经存在"),
    MOBILE_NOT_EXIST(50011, "手机号码不存在"),
    PASSWORD_ERROR(50012, "密码错误");

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
