package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest {

    @ApiModelProperty(value = "分类")
    String keyword = "";

    @ApiModelProperty(value = "页面")
    Integer page = 1;

    @ApiModelProperty(value = "大小")
    Integer limit = 10;
}
