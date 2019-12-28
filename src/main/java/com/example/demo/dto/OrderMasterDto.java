package com.example.demo.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderMasterDto {

    @Valid
    @NotNull(message = "订单数量错误")
    private List<OrderDetailDto> products;
}
