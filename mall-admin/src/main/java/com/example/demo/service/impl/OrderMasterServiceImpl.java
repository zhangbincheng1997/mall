package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.component.redis.RedisService;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.OrderTimeline;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.OrderTimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderMasterService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderTimelineService orderTimelineService;

    @Autowired
    private RedisService redisService;

    @Override
    public OrderMaster get(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<OrderDetail> getDetail(Long id) {
        return orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery()
                .eq(OrderDetail::getOrderId, id));
    }

    @Override
    public List<OrderTimeline> getTimeline(Long id) {
        return orderTimelineService.list(Wrappers.<OrderTimeline>lambdaQuery()
                .eq(OrderTimeline::getOrderId, id));
    }

    @Override
    public Page<OrderMaster> list(OrderPageRequest pageRequest) {
        Page<OrderMaster> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        QueryWrapper<OrderMaster> wrappers = new QueryWrapper<OrderMaster>();
        if (!StringUtils.isEmpty(pageRequest.getStatus()))
            wrappers.eq("status", pageRequest.getStatus());
        wrappers.orderByDesc("update_time", "id");
        return baseMapper.selectPage(page, wrappers);
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        OrderMaster orderMaster = new OrderMaster()
                .setId(id)
                .setStatus(status);
        baseMapper.updateById(orderMaster);

        OrderTimeline orderTimeline = new OrderTimeline()
                .setOrderId(id)
                .setStatus(status);
        orderTimelineService.save(orderTimeline);
    }

    @Override
    @Transactional
    public void addStockMySQL(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> productService.addStock(orderDetail.getProductId(), orderDetail.getProductQuantity()));
    }

    @Override
    public void addStockRedis(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> redisService.increment(Constants.PRODUCT_STOCK + orderDetail.getProductId(), orderDetail.getProductQuantity()));
    }
}
