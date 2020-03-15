package com.example.demo.vo;

import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OrderVo {

    private Long id;

    private String nickname;

    private String email;

    private String amount;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // 返回orderStatus
    public String getStatus() {
        return EnumUtil.getByCode(status, OrderStatusEnum.class).getMsg();
    }
}
