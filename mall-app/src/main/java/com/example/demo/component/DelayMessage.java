package com.example.demo.component;

import lombok.Data;

@Data
public class DelayMessage {

    public static final String TOPIC = "DELAY";

    private Long orderId;
}
