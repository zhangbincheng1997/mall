package com.example.demo.base;

import lombok.Getter;

@Getter
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public Result(Integer code, String msg, Object data) {
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

    public static Result failure(Integer code, String message) {
        return new Result(code, message, null);
    }

    public static Result failure(Status status) {
        return new Result(status.getCode(), status.getMsg(), null);
    }
}
