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
    CODE_ERROR(10002, "验证码错误"),

    // 账号相关
    EMAIL_EXIST(50010, "邮箱已经存在"),
    EMAIL_NOT_EXIST(50011, "邮箱不存在"),
    PASSWORD_ERROR(50012, "密码错误"),

    FILE_UPLOAD_ERROR(60001, "文件上传错误");

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
