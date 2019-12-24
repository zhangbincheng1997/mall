package com.example.demo.service;

import com.example.demo.mapper.GoodsMapper;
import com.example.demo.model.Goods;
import com.example.demo.model.GoodsExample;
import com.example.demo.dto.GoodsDto;
import com.example.demo.utils.SnowFlake;
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
    @Cacheable(value = "goods") // EnableCaching
    public Goods get(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Goods> list(String keyword, int page, int limit) {
        PageHelper.startPage(page, limit);
        GoodsExample example = new GoodsExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andTitleLike("%" + keyword + "%");
            example.or().andDescriptionLike("%" + keyword + "%");
        }
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        PageInfo<Goods> pageInfo = new PageInfo(goodsList);
        return pageInfo;
    }

    @Override
    public int save(GoodsDto goodsDto) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDto, goods);
        return goodsMapper.insert(goods);
    }

    @Override
    public int remove(Long id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Long id, GoodsDto goodsDto) {
        Goods goods = new Goods();
        goods.setId(id);
        BeanUtils.copyProperties(goodsDto, goods);
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }
}
