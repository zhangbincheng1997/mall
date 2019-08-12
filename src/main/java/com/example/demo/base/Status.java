package com.example.demo.base;

/**
 * 响应码
 */
public enum Status {

    // 服务器
    SUCCESS(200, "OK"),
    Forbidden(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    RUNTIME_EXCEPTION(1000, "运行时异常"),
    IO_EXCEPTION(1001, "输入输出异常"),

    // 登录
    NOT_LOGIN(50000, "Not Login");

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
