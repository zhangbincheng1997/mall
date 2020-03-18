package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserInfoDto {

    @ApiModelProperty(value = "头像")
    @NotEmpty(message = "头像不能为空")
    @URL(message = "头像不符合规范")
    private String avatar;

    @ApiModelProperty(value = "昵称")
    @Size(min = 3, max = 12, message = "昵称长度为3-12")
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱不符合规范")
    private String email;

    @ApiModelProperty(value = "性别", example = "0") // Integer 需要 example
    @Range(min = 0, max = 2, message = "性别：0未知、1男性、2女性，默认0")
    private Integer gender;

    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "生日必须是一个过去的日期")
    private Date birthday;
}
