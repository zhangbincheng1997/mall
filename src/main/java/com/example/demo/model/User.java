package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA + MyBatis
 */

@Data // auto generate getter setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    @Column(name = "id")
    private Long id;

    // 手机号码
    @Column(name = "email", nullable = false, unique = true) // 不可为空 全局唯一
    private String email;

    // 密码
    @Column(name = "password", nullable = false) // 不可为空
    private String password;

    // 加盐
    @Column(name = "salt", nullable = false) // 不可为空
    private String salt;

    /**
     * 通过关联表来保存一对一的关系
     * JoinColumn 通过创建新列 "info_id"
     * JoinTable 通过创建新表 "tb_user_info"
     */
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "info_id", referencedColumnName = "id", unique = true)
//    @JoinTable(name = "tb_user_info",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "info_id"))
    private Info info;
}
