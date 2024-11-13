package com.framework.pay.core.client.impl.mock;

import cn.hutool.http.HttpUtil;
import com.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.framework.pay.core.client.dto.refund.PayRefundRespDTO;
import com.framework.pay.core.client.dto.refund.PayRefundUnifiedReqDTO;
import com.framework.pay.core.client.impl.AbstractPayClient;
import com.framework.pay.core.client.impl.NonePayClientConfig;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import com.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import com.framework.pay.core.enums.refund.PayRefundStatusRespEnum;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟支付的 PayClient 实现类
 * 模拟支付返回结果都是成功，方便大家日常流畅
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
public class MockPayClient extends AbstractPayClient<NonePayClientConfig> {

    private static final String MOCK_RESP_SUCCESS_DATA = "MOCK_SUCCESS";

    public MockPayClient(Long channelId, NonePayClientConfig config) {
        super(channelId, PayChannelEnum.MOCK, config);
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) {
        String channelOrderNo = "MOCK-P-" + reqDTO.getOutTradeNo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mockNotify(reqDTO.getNotifyUrl(), reqDTO.getOutTradeNo(),  channelOrderNo, null, null);
            }
        }).start();
        return PayOrderRespDTO.successOf(channelOrderNo, "", LocalDateTime.now(),
                reqDTO.getOutTradeNo(), getRawData());
    }

    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) {
        return PayOrderRespDTO.successOf("MOCK-P-" + outTradeNo, "", LocalDateTime.now(),
                outTradeNo, getRawData());
    }

    @Override
    protected PayRefundRespDTO doUnifiedRefund(PayRefundUnifiedReqDTO reqDTO) {
        String channelRefundNo = "MOCK-R-" + reqDTO.getOutRefundNo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mockNotify(reqDTO.getNotifyUrl(), reqDTO.getOutTradeNo(), null, reqDTO.getOutRefundNo(), channelRefundNo);
            }
        }).start();
        return PayRefundRespDTO.successOf(channelRefundNo, LocalDateTime.now(),
                reqDTO.getOutRefundNo(), getRawData());
    }

    @Override
    protected PayRefundRespDTO doGetRefund(String outTradeNo, String outRefundNo) {
        return PayRefundRespDTO.successOf("MOCK-R-" + outRefundNo, LocalDateTime.now(),
                outRefundNo, getRawData());
    }

    @Override
    protected PayRefundRespDTO doParseRefundNotify(Map<String, String> params, String body) {
        PayRefundRespDTO refundRespDTO = new PayRefundRespDTO();
        refundRespDTO.setOutRefundNo(params.get("outRefundNo"));
        refundRespDTO.setChannelRefundNo(params.get("channelRefundNo"));
        refundRespDTO.setStatus(PayRefundStatusRespEnum.SUCCESS.getStatus());
        return refundRespDTO;
    }

    @Override
    protected PayOrderRespDTO doParseOrderNotify(Map<String, String> params, String body) {
        PayOrderRespDTO orderRespDTO = new PayOrderRespDTO();
        orderRespDTO.setChannelOrderNo(params.get("channelOrderNo"));
        orderRespDTO.setOutTradeNo(params.get("outTradeNo"));
        orderRespDTO.setStatus(PayOrderStatusRespEnum.SUCCESS.getStatus());
        return orderRespDTO;
    }

    private void mockNotify(String url, String outTradeNo, String channelOrderNo, String outRefundNo, String channelRefundNo) {
        try {
            Thread.sleep(3000);
            Map<String, Object> body = new HashMap<>(6);
            body.put("outTradeNo", outTradeNo);
            body.put("outRefundNo", outRefundNo);
            body.put("channelOrderNo", channelOrderNo);
            body.put("channelRefundNo", channelRefundNo);
            HttpUtil.post(url, body);
        } catch (Exception ex) {
            throw new RuntimeException("模拟通知异常");
        }
    }

    private Map<String, String> getRawData() {
        Map<String, String> rawDataMap = new HashMap<>(6);
        rawDataMap.put("status", MOCK_RESP_SUCCESS_DATA);
        return rawDataMap;
    }

}
