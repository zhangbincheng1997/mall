package com.example.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.example.demo.base.GlobalException;
import com.example.demo.base.PageResult;
import com.example.demo.base.Status;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.*;
import com.example.demo.service.OrderService;
import com.example.demo.utils.ConvertUtils;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    private List<OrderDetailVo> getDetail(Long id) {
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderIdEqualTo(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(example);
        List<OrderDetailVo> orderDetailVoList = orderDetailList
                .stream()
                .map(orderDetail -> ConvertUtils.convert(orderDetail, OrderDetailVo.class))
                .collect(Collectors.toList());
        return orderDetailVoList;
    }

    private PageResult getList(OrderMasterExample example) {
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        // page
        PageInfo<OrderMaster> pageInfo = new PageInfo<>(orderList);
        // convert
        List<OrderMasterVo> orderMasterVoList = pageInfo.getList()
                .stream()
                .map(order -> ConvertUtils.convert(order, OrderMasterVo.class))
                .collect(Collectors.toList());
        // add detail
        orderMasterVoList.forEach(orderMasterVo -> orderMasterVo.setProducts(getDetail(orderMasterVo.getId())));
        PageResult pageResult = PageResult.success(orderMasterVoList, pageInfo.getTotal());
        return pageResult;
    }

    /**
     * Seller
     */
    @Override
    public OrderMasterVo get(Long id) {
        OrderMaster order = orderMasterMapper.selectByPrimaryKey(id);
        // convert
        OrderMasterVo orderMasterVo = ConvertUtils.convert(order, OrderMasterVo.class);
        // add detail
        orderMasterVo.setProducts(getDetail(id));
        return orderMasterVo;
    }

    @Override
    public PageResult list(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        String keyword = pageRequest.getKeyword();
        OrderMasterExample example = new OrderMasterExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andUsernameLike("%" + keyword + "%");
        }
        return getList(example);
    }

    @Override
    public int updateOrderStatus(Long id, Integer orderStatus) {
        OrderMaster order = new OrderMaster();
        order.setOrderStatus(orderStatus);
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id);
        return orderMasterMapper.updateByExampleSelective(order, example);
    }

    @Override
    public int updatePayStatus(Long id, Integer payStatus) {
        OrderMaster order = new OrderMaster();
        order.setPayStatus(payStatus);
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id);
        return orderMasterMapper.updateByExampleSelective(order, example);
    }

    /**
     * Buyer
     */
    @Override
    public OrderMasterVo getByBuyer(String username, Long id) {
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id).andUsernameEqualTo(username);
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        if (orderList.size() == 0) throw new GlobalException(Status.ORDER_NOT_EXIST);
        // convert
        OrderMasterVo orderMasterVo = ConvertUtils.convert(orderList.get(0), OrderMasterVo.class);
        // add detail
        orderMasterVo.setProducts(getDetail(id));
        return orderMasterVo;
    }

    @Override
    public PageResult listByBuyer(String username, PageRequest pageRequest) {
        String keyword = pageRequest.getKeyword();
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andUsernameEqualTo(username);
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andUsernameLike("%" + keyword + "%");
        }
        return getList(example);
    }

    @Transactional // 事务ACID
    @Override
    public Long buy(String username, OrderMasterDto orderMasterDto) {
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
            orderDetailMapper.insert(orderDetail);

            // 累加价格
            amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
        }
        order.setAmount(amount);
        // 添加订单
        orderMasterMapper.insertSelective(order);
        return orderId;
    }
}
