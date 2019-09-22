package com.example.demo.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserInfoVo {

    @Size(min = 3, max = 12, message = "昵称长度为3-12")
    private String nickname;

    @URL(message = "请输入正确的链接")
    private String icon;

    @Range(min = 0, max = 2, message = "性别：0->未知 1->男性 2->女性")
    private Integer gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "生日必须是一个过去的日期")
    private Date birthday;
}
