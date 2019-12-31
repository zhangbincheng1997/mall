package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class PayService {

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private AlipayClient alipayClient;

    // https://docs.open.alipay.com/api_1/alipay.trade.pay/
    public void pay(Long id, BigDecimal amount, HttpServletResponse response) {
        // 支付
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(payConfig.getReturnUrl()); // 沙箱设置授权回调地址
        request.setNotifyUrl(payConfig.getNotifyUrl()); // 沙箱设置授权回调地址
        // 设置参数
        Map<String, Object> biz = new HashMap<>();
        biz.put("out_trade_no", id);
        biz.put("product_code", "FAST_INSTANT_TRADE_PAY");
        biz.put("total_amount", amount);
        biz.put("subject", "PC支付测试");
        biz.put("body", "支付宝PC支付测试");
        log.info(JSON.toJSONString(biz));
        request.setBizContent(JSON.toJSONString(biz));
        // 渲染页面
        try {
            String form = alipayClient.pageExecute(request).getBody();
            response.setContentType("text/html;charset=" + payConfig.getCharset());
            response.getWriter().write(form); //直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            throw new GlobalException(Status.PAY_BUG);
        } catch (IOException e) {
            throw new GlobalException(Status.FAILURE);
        }
    }

    // https://docs.open.alipay.com/api_1/alipay.trade.refund/
    public boolean refund(Long id, BigDecimal amount) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Map<String, Object> biz = new HashMap<>();
        biz.put("out_trade_no", id);
        biz.put("refund_amount", amount); // 必选
        log.info(JSON.toJSONString(biz));
        request.setBizContent(JSON.toJSONString(biz));
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new GlobalException(Status.PAY_BUG);
        }
    }

    // 检查密钥
    public boolean check(HttpServletRequest request) {
        // 获取支付宝POST过来的反馈信息
        Map<String, String> map = requestToMap(request);
        try {
            return AlipaySignature.rsaCheckV1(map,
                    payConfig.getPublicKey(), payConfig.getCharset(), payConfig.getSignType());
        } catch (AlipayApiException e) {
            throw new GlobalException(Status.PAY_CHECK_BUG);
        }
    }

    private static Map<String, String> requestToMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
}
