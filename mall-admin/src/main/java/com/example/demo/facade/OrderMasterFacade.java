package com.example.demo.facade;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.component.redis.RedisService;
import com.example.demo.dao.StockDao;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class OrderMasterFacade {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private RedisService redisService;

    public OrderMaster get(Long id) {
        return orderMasterService.getById(id);
    }

    public List<OrderDetail> getDetail(Long id) {
        return orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, id));
    }

    public Page<OrderMaster> list(OrderPageRequest pageRequest) {
        Page<OrderMaster> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(pageRequest.getStatus()))
            wrapper.lambda().eq(OrderMaster::getStatus, pageRequest.getStatus());
        wrapper.lambda().orderByDesc(OrderMaster::getUpdateTime);
        return orderMasterService.page(page, wrapper);
    }

    public void updateOrderStatus(Long id, Integer status) {
        OrderMaster orderMaster = new OrderMaster()
                .setId(id)
                .setStatus(status);
        orderMasterService.updateById(orderMaster);
    }

    public void returnStock(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail ->
        {
            stockDao.addStock(orderDetail.getProductId(),
                    orderDetail.getProductQuantity());
            redisService.increment(Constants.PRODUCT_STOCK + orderDetail.getProductId(),
                    orderDetail.getProductQuantity());
        });
    }
}
