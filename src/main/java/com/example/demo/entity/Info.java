package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_info")
public class Info implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    // 昵称
    @Column(name = "nickname")
    private String nickname;

    // 年龄
    @Column(name = "birth")
    private String birth;

    // 性别
    @Column(name = "sex")
    private String sex;

    // 头像
    @Column(name = "head")
    private String head;

    /**
     * "info"表明Info是关系被维护端
     * "info"是User实体中的属性
     */
    @OneToOne(mappedBy = "info")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "UserInfo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", birth=" + birth + '\'' +
                ", sex='" + sex + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}
