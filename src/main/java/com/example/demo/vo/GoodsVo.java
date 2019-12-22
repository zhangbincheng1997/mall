package com.example.demo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GoodsVo {

    private long id;

    private String title;

    private String description;

    private String icon;

    private Double price;

    private Integer stock;

    private Integer status; // 商品状态：0正常、1下架，默认0

    private Date createTime;

    private Date updateTime;
}
