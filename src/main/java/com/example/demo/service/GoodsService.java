package com.example.demo.service;

import com.example.demo.model.Goods;
import com.example.demo.vo.GoodsVo;
import com.github.pagehelper.PageInfo;

public interface GoodsService {

    Goods get(Long id);

    long count();

    PageInfo list(String keyword, int pageNum, int pageSize);

    int create(GoodsVo goodsVo);

    int delete(Long id);

    int update(Long id, GoodsVo goodsVo);
}
