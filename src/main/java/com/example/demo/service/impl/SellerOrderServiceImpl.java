package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.ProductCustomMapper;
import com.example.demo.model.*;
import com.example.demo.service.SellerOrderService;
import com.example.demo.utils.Constants;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Override
    public OrderMaster get(Long id) {
        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryKey(id);
        if (orderMaster == null) throw new GlobalException(Status.ORDER_NOT_EXIST);
        return orderMaster;
    }

    @Override
    public PageInfo<OrderMaster> list(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "update_time desc, id desc");
        String keyword = pageRequest.getKeyword();
        OrderMasterExample example = new OrderMasterExample();
        Long id = Convert.toLong(keyword);
        if (id != null) {
            example.or().andIdEqualTo(id);
        }
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        return new PageInfo<OrderMaster>(orderList);
    }
}
