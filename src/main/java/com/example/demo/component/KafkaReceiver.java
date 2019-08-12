package com.example.demo.component;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
public class KafkaReceiver {

    private Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(topics = "message")
    public void process(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            logger.info("Kafka Receiver  : " + message);
        }
    }

}
