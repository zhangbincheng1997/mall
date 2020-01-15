package com.example.demo.vo;

import com.example.demo.common.enums.OrderStatusEnum;
import com.example.demo.common.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OrderTimelineVo {

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    // 返回orderStatus
    public String getStatus() {
        return EnumUtil.getByCode(status, OrderStatusEnum.class).getMsg();
    }
}
