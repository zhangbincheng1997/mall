package com.example.demo.facade;

import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.entity.OrderMaster;
import com.example.demo.enums.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PayFacade {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    public void deal(Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.REFUND_REQUEST.getCode())) // 退款请求
            throw new GlobalException(Status.ORDER_NOT_REFUND_REQUEST);
        // 支付：退款
        boolean isSuccess = payService.refund(orderMaster.getId(), orderMaster.getAmount());
        if (!isSuccess) {
            throw new GlobalException(Status.REFUND_BUG);
        }
        // 订单：退款成功
        orderMasterFacade.returnStock(id);
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.REFUND_SUCCESS.getCode());
    }

    public void close(Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            throw new GlobalException(Status.ORDER_NOT_TO_BE_PAID);
        // 订单：GM关闭
        orderMasterFacade.returnStock(id);
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.GM_CLOSE.getCode());
    }

    public void ship(Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            throw new GlobalException(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单：待收货
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.TO_BE_RECEIVED.getCode());
    }
}
