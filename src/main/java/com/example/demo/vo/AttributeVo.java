package com.example.demo.vo;

import lombok.Data;

import java.util.List;

@Data
public class AttributeVo {

    @Data
    public static class AttributeValueVo {
        private Long id;
        private String value;
    }

    private Long id;

    private String name;

    private List<AttributeValueVo> sub;
}
