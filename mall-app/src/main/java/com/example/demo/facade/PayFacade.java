package com.example.demo.facade;

import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.entity.OrderMaster;
import com.example.demo.enums.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
public class PayFacade {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    public void pay(String username, Long id, HttpServletResponse response) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 支付
        payService.pay(orderMaster.getId(), orderMaster.getAmount(), response);
    }

    public void close(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            throw new GlobalException(Status.ORDER_NOT_TO_BE_PAID);
        // 订单：USER关闭
        orderMasterFacade.returnStock(id);
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.USER_CLOSE.getCode());
    }

    public void receive(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_RECEIVED.getCode())) // 待收货
            throw new GlobalException(Status.ORDER_NOT_TO_BE_RECEIVED);
        // 订单：订单完成
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
    }

    public void refund(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            throw new GlobalException(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单：退款申请
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
    }
}
