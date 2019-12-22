package com.example.demo.base;

/**
 * 全局异常
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Status status;

    public GlobalException(Status status) {
        super(status.toString());
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setMessage(Status status) {
        this.status = status;
    }
}