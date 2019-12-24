package com.example.demo.dto;

import com.example.demo.enums.SexEnum;
import com.example.demo.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class UserInfoDto {

    @ApiModelProperty(value = "昵称")
    @Size(min = 3, max = 12, message = "昵称长度为3-12")
    private String nickname;

    @ApiModelProperty(value = "性别", example = "0") // Integer 需要 example
    @Range(min = 0, max = 2, message = "性别：0未知、1男性、2女性，默认0")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "生日必须是一个过去的日期")
    private Date birthday;

    @JsonIgnore // 序列化时忽略属性
    public SexEnum getSexEnum() {
        return EnumUtils.getByCode(sex, SexEnum.class);
    }
}
