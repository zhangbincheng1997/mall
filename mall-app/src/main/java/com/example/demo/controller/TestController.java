package com.example.demo.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.OrderMessage;
import com.example.demo.component.redis.RedisService;
import com.example.demo.dao.StockDao;
import com.example.demo.entity.User;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "测试")
@RestController
@RequestMapping("/test")
@Transactional
public class TestController {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // Lua脚本
    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(Constants.LUA_SCRIPT, Long.class);

    // 模拟购物车
    private static final Map<Long, Integer> cart = new HashMap<>();

    @ApiOperation("初始化")
    @GetMapping("/init")
    public Result<Map<Long, Integer>> init(
            @RequestParam(name = "type", defaultValue = "1") Integer type,
            @RequestParam(name = "stock", defaultValue = "1000") Integer stock) {
        cart.clear();
        if (type >= 1) {
            cart.put(1L, 1);
            redisService.set(Constants.PRODUCT_STOCK + "1", stock);
            productService.saveOrUpdate(new Product().setId(1L).setStock(stock));
        }
        if (type >= 2) {
            cart.put(2L, 1);
            redisService.set(Constants.PRODUCT_STOCK + "2", stock);
            productService.saveOrUpdate(new Product().setId(2L).setStock(stock));
        }
        if (type >= 3) {
            cart.put(3L, 1);
            redisService.set(Constants.PRODUCT_STOCK + "3", stock);
            productService.saveOrUpdate(new Product().setId(3L).setStock(stock));
        }
        return Result.success(cart);
    }

    @ApiOperation("查询结果")
    @GetMapping("/result")
    public Result<JSONObject> result() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Redis_7", redisService.get(Constants.PRODUCT_STOCK + "1"));
        jsonObject.put("MySQL_7", productService.getById(1L).getStock());
        jsonObject.put("Redis_8", redisService.get(Constants.PRODUCT_STOCK + "2"));
        jsonObject.put("MySQL_8", productService.getById(2L).getStock());
        jsonObject.put("Redis_9", redisService.get(Constants.PRODUCT_STOCK + "3"));
        jsonObject.put("MySQL_9", productService.getById(3L).getStock());
        return Result.success(jsonObject);
    }

    @ApiOperation("创建订单 MySQL简化版本")
    @GetMapping("/mysql")
    public String buy0() {
        Long orderId = snowflake.nextId();
        // 计算价格
        BigDecimal amount = new BigDecimal(0);
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer productQuantity = entry.getValue();
            boolean result = stockDao.subStock(productId, productQuantity); // 减库存
            if (!result) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH); // 回滚
            // 累加价格
            Product product = productService.getById(productId);
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
    @GetMapping("/redis")
    public String buy1() {
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            keys.add(Constants.PRODUCT_STOCK + entry.getKey());
            values.add(entry.getValue());
        }
        Long result = redisService.execute(redisScript, keys, values.toArray());
        if (result == 0) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);

        Long orderId = snowflake.nextId(); // 雪花算法 生成全局唯一ID
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(orderId);
        orderMessage.setUser(new User().setUsername("admin").setNickname("admin").setEmail("1656704949@qq.com"));
        orderMessage.setCart(cart);

        // 同步
        rocketMQTemplate.syncSend(OrderMessage.TOPIC, orderMessage);

        return String.valueOf(orderId);
    }
}
