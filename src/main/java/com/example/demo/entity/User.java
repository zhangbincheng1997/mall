package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String head;

    // 注册时间
    @Column(nullable = false)
    private Date register_date;

    // 上次登录时间
    @Column(nullable = false)
    private Date last_login_datet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }

    public Date getLast_login_datet() {
        return last_login_datet;
    }

    public void setLast_login_datet(Date last_login_datet) {
        this.last_login_datet = last_login_datet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head='" + head + '\'' +
                ", register_date=" + register_date +
                ", last_login_datet=" + last_login_datet +
                '}';
    }
}
