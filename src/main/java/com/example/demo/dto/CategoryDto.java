package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CategoryDto {

    @ApiModelProperty(value = "分类", required = true)
    @Size(min = 1, max = 255, message = "分类长度为1-255")
    private String title = "未分类";

    @ApiModelProperty(value = "PID", required = true)
    private Long pid = 1L;
}
