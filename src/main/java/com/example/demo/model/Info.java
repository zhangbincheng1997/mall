package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "info")
public class Info  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // 自增
    @Column(name = "id")
    private Long id;

    // 昵称
    @Column(name = "nickname")
    private String nickname;

    // 年龄
    @Column(name = "birth")
    private String birth;

    /**
     * 性别枚举
     * ORDINAL(int) default
     * STRING(String)
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "sex")
    private Sex sex;

    // 头像
    @Column(name = "head_url")
    private String headUrl;

    /**
     * 通过关联表来保存一对一的关系
     * JsonIgnore 防止JSON递归循环
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "info")
    @JsonIgnore
    private User user;

    /**
     * 性别枚举
     */
    public enum Sex {

        UNKNOWN(0),
        MALE(1),
        FEMALE(2);

        private int sex;

        Sex(int sex) {
            this.sex = sex;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public static Sex parseCode(int code) {
            for (Sex sex : Sex.values()) {
                if (sex.getSex() == code) {
                    return sex;
                }
            }
            return null;
        }
    }
}
