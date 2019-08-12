package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

//@Component
public class KafkaSender {

    private Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(Object obj) {
        String jsonObj = JSON.toJSONString(obj);
        ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send("message", jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.info("Kafka Sender : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                logger.info("Kafka Sender : " + stringObjectSendResult.getProducerRecord().value());
            }
        });
    }
}
