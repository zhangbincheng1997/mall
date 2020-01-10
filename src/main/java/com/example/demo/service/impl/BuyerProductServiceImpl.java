package com.example.demo.service.impl;

import com.example.demo.dto.PageRequest;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.BuyerProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BuyerProductServiceImpl implements BuyerProductService {

    @Autowired
    private ProductMapper productMapper;


    @Override
    public PageInfo<Product> list(PageRequest pageRequest) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(true); // 上架状态
        return list(example, pageRequest);
    }

    private PageInfo<Product> list(ProductExample example, PageRequest pageRequest) {
        String keyword = pageRequest.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            example.getOredCriteria().get(0).andNameLike("%" + keyword + "%");
        }
        String category = pageRequest.getCategory();
        if (!StringUtils.isEmpty(category)) {
            example.getOredCriteria().get(0).andCategoryEqualTo(new Long(category));
        }
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        List<Product> productList = productMapper.selectByExample(example);
        return new PageInfo<>(productList);
    }
}
