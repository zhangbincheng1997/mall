package com.example.demo.dto.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPageRequest extends PageRequest {

    @ApiModelProperty(value = "状态")
    String status = "";
}
