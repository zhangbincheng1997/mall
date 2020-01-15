package com.example.demo.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AttributeVo {

    private Long id;

    private String name;

    private List<AttributeValueVo> sub = new ArrayList<>(); // 默认null
}
