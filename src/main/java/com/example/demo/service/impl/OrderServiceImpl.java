package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.ProductCustomMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.*;
import com.example.demo.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCustomMapper productCustomMapper;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetail> getDetail(Long id) {
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderIdEqualTo(id);
        return orderDetailMapper.selectByExample(example);
    }

    /**
     * Seller
     */
    @Override
    @Cacheable(cacheNames = "order", key = "#id")
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

    @Override
    @CacheEvict(cacheNames = "order", key = "#id")
    public int updateOrderStatus(Long id, Integer orderStatus) {
        OrderMaster order = new OrderMaster();
        order.setOrderStatus(orderStatus);
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id);
        return orderMasterMapper.updateByExampleSelective(order, example);
    }

    @Override
    @CacheEvict(cacheNames = "order", key = "#id")
    public int updatePayStatus(Long id, Integer payStatus) {
        OrderMaster order = new OrderMaster();
        order.setPayStatus(payStatus);
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id);
        return orderMasterMapper.updateByExampleSelective(order, example);
    }

    @Override
    @Transactional
    public void returnStock(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> productCustomMapper.increaseStock(orderDetail.getProductId(),
                orderDetail.getProductQuantity()));
    }

    /**
     * Buyer
     */
    @Override
    @Cacheable(cacheNames = "order", key = "#id")
    public OrderMaster getByBuyer(String username, Long id) {
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id).andUsernameEqualTo(username);
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        if (orderList.size() == 0) throw new GlobalException(Status.ORDER_NOT_EXIST);
        return orderList.get(0);
    }

    @Override
    public PageInfo<OrderMaster> listByBuyer(String username, PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "update_time desc, id desc");
        String keyword = pageRequest.getKeyword();
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andUsernameEqualTo(username);
        Long id = Convert.toLong(keyword);
        if (id != null) {
            example.getOredCriteria().get(0).andIdEqualTo(id);
        }
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        return new PageInfo<OrderMaster>(orderList);
    }

    @Transactional // 事务ACID
    @Override
    public OrderMaster buy(String username, OrderMasterDto orderMasterDto) {
        // 创建订单
        OrderMaster order = new OrderMaster();
        order.setUsername(username);
        // 雪花算法 生成全局唯一ID
        Long orderId = snowflake.nextId();
        order.setId(orderId);
        // 计算价格
        BigDecimal amount = new BigDecimal(0);
        for (OrderDetailDto orderDetailDto : orderMasterDto.getProducts()) {
            Long productId = orderDetailDto.getId();
            Integer productQuantity = orderDetailDto.getQuantity();
            Product product = productMapper.selectByPrimaryKey(productId);
            // 商品不存在
            if (product == null) throw new GlobalException(Status.PRODUCT_NOT_EXIST);
            // 下架状态
            if (!product.getStatus()) throw new GlobalException(Status.PRODUCT_STATUS_NOT_ON);
            // 库存不足
            if (product.getStock() < productQuantity) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);
            // 减库存
            product.setStock(product.getStock() - productQuantity);
            productMapper.updateByPrimaryKeySelective(product);

            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductIcon(product.getIcon());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductPrice(product.getPrice());
            orderDetail.setProductQuantity(productQuantity);
            // 添加订单详情
            orderDetailMapper.insertSelective(orderDetail);

            // 累加价格
            amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
        }
        order.setAmount(amount);
        // 添加订单
        orderMasterMapper.insertSelective(order);
        return order;
    }
}
