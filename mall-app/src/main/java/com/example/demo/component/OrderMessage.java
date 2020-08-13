package com.example.demo.component;

import com.example.demo.entity.User;
import lombok.Data;

import java.util.Map;

@Data
public class OrderMessage {

    public static final String TOPIC = "ORDER";

    private Long orderId;

    private User user;

    private Map<Long, Integer> cart;
}
