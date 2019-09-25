package com.example.demo.service;

import com.example.demo.mapper.GoodsMapper;
import com.example.demo.model.Goods;
import com.example.demo.model.GoodsExample;
import com.example.demo.vo.GoodsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
//    @Cacheable(value = "goods", key = "#id") // EnableCaching
    @Cacheable(value = "goods")
    public Goods get(Long id) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        return goods;
    }

    @Override
    public long count() {
        return goodsMapper.countByExample(null);
    }

    @Override
    public PageInfo list(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        GoodsExample example = new GoodsExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andTitleLike("%" + keyword + "%");
            example.or().andDescriptionLike("%" + keyword + "%");
        }
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        PageInfo page = new PageInfo(goodsList);
        return page;
    }

    @Override
    public int create(GoodsVo goodsVo) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsVo, goods);
        return goodsMapper.insert(goods);
    }

    @Override
    public int delete(Long id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Long id, GoodsVo goodsVo) {
        Goods goods = new Goods();
        goods.setId(id);
        BeanUtils.copyProperties(goodsVo, goods);
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }
}
