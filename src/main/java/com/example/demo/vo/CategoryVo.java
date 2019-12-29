package com.example.demo.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryVo {

    private Long id;

    private String title;

    private List<CategoryVo> children = new ArrayList<>();

    private Boolean spread = true; // 展开
}
