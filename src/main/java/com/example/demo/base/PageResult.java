package com.example.demo.base;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<T> {

    private Long count;

    public PageResult(Integer code, String msg, T data, Long count) {
        super(code, msg, data);
        this.count = count;
    }

    public static<T> PageResult<T> success(T data, Long count) {
        return new PageResult<T>(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), data, count);
    }
}
