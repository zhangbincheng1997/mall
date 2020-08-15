package com.example.demo.component.mq;

import lombok.Data;

@Data
public class DelayMessage {

    public static final String TOPIC = "DELAY";

    private Long orderId;
}
