package com.example.demo.component;

import com.example.demo.component.pay.PayService;
import com.example.demo.entity.OrderMaster;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.facade.OrderMasterFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@Transactional
public class PayTask {

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @Autowired
    private PayService payService;

    private static final long ONE_MIN = 60 * 1000; // 1min
    private static final long ONE_HOUR = 50 * 60 * 1000; // 1h

    /**
     * 定时任务：一小时内的订单
     */
    @Scheduled(fixedRate = ONE_MIN)
    public void paySync() {
        List<OrderMaster> orderMasterList = orderMasterFacade.payList(ONE_HOUR);
        for (OrderMaster orderMaster : orderMasterList) {
            Long orderId = orderMaster.getId();
            // 支付成功
            if (payService.query(orderId)) {
                orderMasterFacade.updateOrderStatus(orderId, OrderStatusEnum.TO_BE_SHIPPED.getCode());
                log.info("支付成功，待发货");
            }
        }
    }
}
