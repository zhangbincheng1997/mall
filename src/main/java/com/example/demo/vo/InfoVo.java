package com.example.demo.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Data
public class InfoVo {

    // 昵称
    @Size(min = 0, max = 12)
    private String nickname;

    // 生日
    // yyyy/MM/dd
    @Past
    private String birth;

    // 性别
    // 0 unknown 1 male 2 female
    @Range(min = 0, max = 2)
    private Integer sex;

    // 头像
    @URL
    private String headUrl;
}
