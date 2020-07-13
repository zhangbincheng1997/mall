package com.example.demo.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.redis.RedisService;
import com.example.demo.entity.*;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "测试")
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private RedisService redisService;

    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(Constants.LUA_SCRIPT, Long.class);

    // 模拟购物车
    private static final Map<Long, Integer> cart = new HashMap<>();

    @ApiOperation("初始化")
    @RequestMapping("/init")
    @ResponseBody
    public Result<Map<Long, Integer>> init(
            @RequestParam(name = "type", defaultValue = "1") Integer type,
            @RequestParam(name = "stock", defaultValue = "1000") Integer stock) {
        cart.clear();
        if (type >= 1) {
            cart.put(7L, 1);
            redisService.set(Constants.PRODUCT_STOCK + "7", stock);
            productService.saveOrUpdate(new Product().setId(7L).setStock(stock));
        }
        if (type >= 2) {
            cart.put(8L, 1);
            redisService.set(Constants.PRODUCT_STOCK + "8", stock);
            productService.saveOrUpdate(new Product().setId(8L).setStock(stock));
        }
        if (type >= 3) {
            cart.put(9L, 1);
            redisService.set(Constants.PRODUCT_STOCK + "9", stock);
            productService.saveOrUpdate(new Product().setId(9L).setStock(stock));
        }
        return Result.success(cart);
    }

    @ApiOperation("查询结果")
    @RequestMapping("/result")
    @ResponseBody
    public Result<JSONObject> result() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Redis_7", redisService.get(Constants.PRODUCT_STOCK + "7"));
        jsonObject.put("MySQL_7", productService.get(7L).getStock());
        jsonObject.put("Redis_8", redisService.get(Constants.PRODUCT_STOCK + "8"));
        jsonObject.put("MySQL_8", productService.get(8L).getStock());
        jsonObject.put("Redis_9", redisService.get(Constants.PRODUCT_STOCK + "9"));
        jsonObject.put("MySQL_9", productService.get(9L).getStock());
        return Result.success(jsonObject);
    }

    @ApiOperation("创建订单 MySQL简化版本")
    @RequestMapping("/mysql")
    @ResponseBody
    @Transactional
    public String buy0() {
        Long orderId = snowflake.nextId();
        // 计算价格
        BigDecimal amount = new BigDecimal(0);
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer productQuantity = entry.getValue();
            boolean result = productService.subStock(productId, productQuantity); // 减库存
            if (!result) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH); // 回滚
            Product product = productService.get(productId);
            amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setProductIcon(product.getIcon());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductPrice(product.getPrice());
            orderDetailService.save(orderDetail);
        }
        // 创建订单
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setId(orderId);
        orderMaster.setAmount(amount);
        orderMaster.setUsername("admin");
        orderMaster.setNickname("admin");
        orderMaster.setEmail("1656704949@qq.com");
        orderMasterService.save(orderMaster);
        return String.valueOf(orderId);
    }

    @ApiOperation("创建订单 Redis简化版本")
    @RequestMapping("/redis")
    @ResponseBody
    public String buy1() {
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            keys.add(Constants.PRODUCT_STOCK + entry.getKey());
            values.add(entry.getValue());
        }
        Long result = redisService.execute(redisScript, keys, values.toArray());
        if (result == 0) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);
        Long orderId = snowflake.nextId();
        return String.valueOf(orderId);
    }
}
