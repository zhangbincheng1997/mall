package com.example.demo.service;

import com.example.demo.model.Goods;
import com.example.demo.dto.GoodsDto;
import com.github.pagehelper.PageInfo;

public interface GoodsService {

    Goods get(Long id);

    PageInfo<Goods> list(String keyword, int page, int limit);

    int create(GoodsDto goodsDto);

    int delete(Long id);

    int update(Long id, GoodsDto goodsDto);
}
