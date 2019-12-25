package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductVo {

    private long id;

    private String icon;

    private String name;

    private String description;

    private String price; // Product: BigDecimal -> ProductVo: String

    private Integer stock;

    private Integer status; // 商品状态：0关、1开，默认0

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
