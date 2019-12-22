package com.example.demo.base;

import lombok.Data;

/**
 * API格式封装
 */
@Data
public class Result {

    private int code;
    private String msg;
    private Object data;

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
        this.code = Status.SUCCESS.getCode();
        this.msg = Status.SUCCESS.getMsg();
    }

    public static Result success() {
        return new Result(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), null);
    }

    public static Result success(Object data) {
        return new Result(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), data);
    }

    public static Result failure() {
        return new Result(Status.FAILURE.getCode(), Status.FAILURE.getMsg(), null);
    }

    public static Result failure(int code, String message) {
        return new Result(code, message, null);
    }

    public static Result failure(Status status) {
        return new Result(status.getCode(), status.getMsg(), null);
    }
}
