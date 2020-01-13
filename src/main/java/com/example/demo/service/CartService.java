package com.example.demo.service;

import com.example.demo.dto.CartDto;

import java.util.List;

public interface CartService {

    List<CartDto> list(String username);

    void create(String username, CartDto cartDto);

    void update(String username, CartDto cartDto);

    void delete(String username, List<Long> ids);

    void updateAllCheck(String username, Boolean checked);

    void updateOneCheck(String username, Long id, Boolean checked);
}
