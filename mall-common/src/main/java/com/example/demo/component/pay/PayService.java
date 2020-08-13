package com.example.demo.component.pay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.utils.Constants;
import com.example.demo.utils.RenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PayService {

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private AlipayClient alipayClient;

    // https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay
    public void pay(Long id, BigDecimal amount, HttpServletResponse response) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(payConfig.getReturnUrl()); // 沙箱设置授权回调地址
        request.setNotifyUrl(payConfig.getNotifyUrl()); // 沙箱设置授权回调地址
        Map<String, Object> biz = new HashMap<>();
        biz.put("out_trade_no", id);
        biz.put("total_amount", amount);
        biz.put("product_code", Constants.PAY_PRODUCT_CODE);
        biz.put("subject", Constants.PAY_SUBJECT);
        biz.put("body", Constants.PAY_BODY);
        log.info(JSON.toJSONString(biz));
        request.setBizContent(JSON.toJSONString(biz));
        try {
            String form = alipayClient.pageExecute(request).getBody();
            RenderUtil.render(response, form);
        } catch (AlipayApiException e) {
            throw new GlobalException(Status.PAY_BUG);
        } catch (IOException e) {
            throw new GlobalException(Status.FAILURE);
        }
    }

    // https://opendocs.alipay.com/apis/api_1/alipay.trade.refund/
    public boolean refund(Long id, BigDecimal amount) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Map<String, Object> biz = new HashMap<>();
        biz.put("out_trade_no", id);
        biz.put("refund_amount", amount); // 必填项
        log.info(JSON.toJSONString(biz));
        request.setBizContent(JSON.toJSONString(biz));
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new GlobalException(Status.PAY_BUG);
        }
    }

    // https://opendocs.alipay.com/apis/api_1/alipay.trade.close/
    public boolean close(Long id) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        Map<String, Object> biz = new HashMap<>();
        biz.put("out_trade_no", id);
        log.info(JSON.toJSONString(biz));
        request.setBizContent(JSON.toJSONString(biz));
        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new GlobalException(Status.PAY_BUG);
        }
    }

    // 检查密钥
    public boolean check(HttpServletRequest request) {
        Map<String, String> map = requestToMap(request);
        try {
            log.info(JSON.toJSONString(map));
            return AlipaySignature.rsaCheckV1(map, payConfig.getPublicKey(), payConfig.getCharset(), payConfig.getSignType());
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
