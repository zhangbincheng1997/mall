package com.example.demo.service.impl;

import com.example.demo.dto.page.ProductPageRequest;
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
    public PageInfo<Product> list(ProductPageRequest pageRequest) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(true); // 上架状态

        String keyword = pageRequest.getKeyword();
        String select = pageRequest.getCategory();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        if (!StringUtils.isEmpty(select)) {
            criteria.andCategoryEqualTo(new Long(select));
        }

        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        List<Product> productList = productMapper.selectByExample(example);
        return new PageInfo<>(productList);
    }
}
