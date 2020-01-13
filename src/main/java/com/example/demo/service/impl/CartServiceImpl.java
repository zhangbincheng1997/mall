package com.example.demo.service.impl;

import com.example.demo.component.RedisService;
import com.example.demo.dto.CartDto;
import com.example.demo.service.CartService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisService redisService;

    @Override
    public List<CartDto> list(String username) {
        Set<String> keys = redisService.keys(Constants.REDIS_PRODUCT_CART + username);
        List<Object> objects = redisService.multiGet(keys);
        return objects.stream()
                .map(object -> (CartDto) object)
                .sorted(Comparator.comparing(CartDto::getId).reversed()) // 保证顺序
                .collect(Collectors.toList());
    }

    @Override
    public void create(String username, CartDto cartDto) {
        String key = getKey(username, cartDto.getId());
        redisService.set(key, cartDto);
    }

    @Override
    public void update(String username, CartDto cartDto) {
        String key = getKey(username, cartDto.getId());
        CartDto redisCart = (CartDto) redisService.get(key);
        redisCart.setQuantity(cartDto.getQuantity());
        redisService.set(key, redisCart);
    }

    @Override
    public void delete(String username, List<Long> ids) {
        List<String> keys = ids.stream().map(id -> getKey(username, id)).collect(Collectors.toList());
        redisService.delete(keys);
    }

    @Override
    public void updateAllCheck(String username, Boolean checked) {
        Set<String> keys = redisService.keys(Constants.REDIS_PRODUCT_CART + username);
        keys.forEach(key -> updateCheck(key, checked));
    }

    @Override
    public void updateOneCheck(String username, Long id, Boolean checked) {
        String key = getKey(username, id);
        updateCheck(key, checked);
    }

    public void updateCheck(String key, Boolean checked) {
        CartDto cartDto = (CartDto) redisService.get(key);
        cartDto.setChecked(checked);
        redisService.set(key, cartDto);
    }

    private String getKey(String username, Long id) {
        return Constants.REDIS_PRODUCT_CART + username + ":" + id;
    }
}
