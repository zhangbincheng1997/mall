package cn.jnu.common.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum implements CodeEnum {

    ADMIN(1, "管理员"),
    USER(2, "普通用户");

    private Integer code;
    private String message;

    UserRoleEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
