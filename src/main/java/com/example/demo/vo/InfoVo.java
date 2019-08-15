package com.example.demo.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

public class InfoVo {

    // 昵称
    // 要么为空，要么长度3-12
    @Length(min = 3, max = 12)
    private String nickname;

    // 生日
    // yyyy/MM/dd
    @Past
    private String birth;

    // 性别
    // 0 male 1 female 2 none
    @Size(min = 0, max = 2)
    private String sex;

    // 头像
    private String head;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "InfoVo{" +
                "nickname='" + nickname + '\'' +
                ", birth=" + birth + '\'' +
                ", sex='" + sex + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}
