package com.example.demo.base;

import lombok.Getter;

@Getter
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
        this.code = Status.SUCCESS.getCode();
        this.msg = Status.SUCCESS.getMsg();
    }

    public static <T> Result<T> success() {
        return new Result<T>(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> failure() {
        return new Result<T>(Status.FAILURE.getCode(), Status.FAILURE.getMsg(), null);
    }

    public static <T> Result<T> failure(Integer code, String message) {
        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> failure(Status status) {
        return new Result<T>(status.getCode(), status.getMsg(), null);
    }
}
