package com.example.demo.base;

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

    public void setStatus(Status status) {
        this.status = status;
    }
}