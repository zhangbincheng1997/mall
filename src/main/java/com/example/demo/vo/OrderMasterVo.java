package com.example.demo.vo;

import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.PayStatusEnum;
import com.example.demo.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderMasterVo {

    private String id; // Long -> String

    private String username;

    private String amount;

    private Integer orderStatus;

    private Integer payStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private List<OrderDetailVo> products;

    // 返回orderStatus
    public String getOrderStatus() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class).getMsg();
    }

    // 返回payStatus
    public String getPayStatus() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class).getMsg();
    }
}
