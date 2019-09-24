package com.example.demo.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.model.Goods;
import com.example.demo.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public Goods get(Long id) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        return goods;
    }

    @Override
    public List<Goods> getList() {
        List<Goods> goodsList = goodsMapper.selectByExample(null);
        return goodsList;
    }

    @Override
    public int create(GoodsVo goodsVo) {
        Goods goods = new Goods();
        BeanUtil.copyProperties(goodsVo, goods);
        int res = goodsMapper.insert(goods);
        return res;
    }

    // 返回的结果int值代表着对多少条记录数据的操作
    @Override
    public int delete(Long id) {
        int res = goodsMapper.deleteByPrimaryKey(id);
        return res;
    }

    @Override
    public int update(Long id, GoodsVo goodsVo) {
        Goods goods = new Goods();
        goods.setId(id);
        BeanUtils.copyProperties(goodsVo, goods);
        int res = goodsMapper.updateByPrimaryKeySelective(goods);
        return res;
    }
}
