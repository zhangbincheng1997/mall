package com.example.demo.service;

import com.example.demo.base.Result;
import com.example.demo.model.Goods;
import com.example.demo.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    Goods get(Long id);

    List<Goods> getList();

    int create(GoodsVo goodsVo);

    int delete(Long id);

    int update(Long id, GoodsVo goodsVo);
}
