package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.example.demo.component.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

// https://github.com/apache/rocketmq-spring/wiki/Transactional-Message
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate") // default
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Autowired
    private RedisService redisService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            log.info("[executeLocalTransaction][执行本地事务，消息：{} 参数：{}]", message, o);
            OrderMessage orderMessage = JSON.parseObject(new String((byte[]) message.getPayload()), OrderMessage.class);
            // TODO 暂时不需要事务消息
            redisService.set("TX:" + orderMessage.getOrderId(), 1); // 成功消费
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("[checkLocalTransaction][回查本地事务，消息：{}]", message);
        OrderMessage orderMessage = (OrderMessage) message.getPayload();
        Boolean result = redisService.hasKey("TX:" + orderMessage.getOrderId());
        if (result) { // 存在 = 成功消费
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
