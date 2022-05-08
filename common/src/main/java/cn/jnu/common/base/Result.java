package cn.jnu.common.base;

import lombok.Getter;

@Getter
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private Integer total;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(Integer code, String message, T data, Integer total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public Result() {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getMessage();
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(T data, Long total) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, total.intValue());
    }

    public static <T> Result<T> failure() {
        return new Result<>(ResultCode.FAILURE.getCode(), ResultCode.FAILURE.getMessage(), null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(ResultCode.FAILURE.getCode(), message, null);
    }

    public static <T> Result<T> failure(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> failure(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> failure(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

    public static <T> Result<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failure();
        }
    }
}
