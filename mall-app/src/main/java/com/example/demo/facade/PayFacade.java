package com.example.demo.facade;

import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.User;
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

    public String create(User user) {
        // 订单创建
        return orderMasterFacade.create(user);
    }

    public void pay(String username, Long id, HttpServletResponse response) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 订单支付
        payService.pay(orderMaster.getId(), orderMaster.getAmount(), response);
    }

    public void cancel(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            throw new GlobalException(Status.ORDER_NOT_TO_BE_PAID);
        // 订单处理关闭
        boolean isSuccess = payService.close(orderMaster.getId());
        if (!isSuccess) {
            throw new GlobalException(Status.CLOSE_BUG);
        }
        // 订单取消
        orderMasterFacade.returnStock(id);
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.CLOSE.getCode());
    }

    public void receive(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_RECEIVED.getCode())) // 待收货
            throw new GlobalException(Status.ORDER_NOT_TO_BE_RECEIVED);
        // 订单完成
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
    }

    public void refund(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            throw new GlobalException(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单申请退款
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
    }

    public String query(String username, Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        return payService.query(orderMaster.getId());
    }
}
