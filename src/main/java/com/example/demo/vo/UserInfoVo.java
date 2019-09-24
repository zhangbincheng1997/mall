package com.example.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class UserInfoVo {

    //    @NotNull(message = "手机不能为空")
//    @Pattern(regexp = "1\\d{10}", message = "手机不符合规范")
//    private String mobile;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱不符合规范")
    private String email;

    @ApiModelProperty(value = "链接")
    private String icon;

    @ApiModelProperty(value = "昵称")
    @Size(min = 3, max = 12, message = "昵称长度为3-12")
    private String nickname;

    @ApiModelProperty(value = "性别", example = "0") // Integer 需要 example
    @Range(min = 0, max = 2, message = "性别：0->未知 1->男性 2->女性")
    private Integer gender;

    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "生日必须是一个过去的日期")
    private Date birthday;
}
