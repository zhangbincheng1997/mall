package cn.jnu.common.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    private Integer code;
    private String message;

    public GlobalException(ResultCode resultCode) {
        super("GlobalException{code=" + resultCode.getCode() + ", message=" + resultCode.getMessage() + "}");
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public GlobalException(Integer code, String message) {
        super("GlobalException{code=" + code + ", message=" + message + "}");
        this.code = code;
        this.message = message;
    }

    public GlobalException() {

    }
}