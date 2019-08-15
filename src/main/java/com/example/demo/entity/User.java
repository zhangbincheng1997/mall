package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    // 手机号码
    @Column(name = "mobile", nullable = false, unique = true) // 全局唯一
    private String mobile;

    // 密码
    @Column(name = "password", nullable = false)
    private String password;

    // 加盐
    @Column(name = "salt", nullable = false)
    private String salt;

    /**
     * 通过关联表来保存一对一的关系
     * 定义了一张叫"tb_user_info"的表
     * joinColumns定义一个外键叫"user_id"，指向关系维护端User的主键
     * inverseJoinColumns定义了一个外键叫"info_id",指向关系被维护端Info的主键
     */
    @OneToOne(cascade = CascadeType.ALL) // 一对一
    @JoinTable(name = "tb_user_info",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "info_id"))
    private Info info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", info=" + info +
                '}';
    }
}
