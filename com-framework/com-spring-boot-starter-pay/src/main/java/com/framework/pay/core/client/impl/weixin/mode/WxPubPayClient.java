package com.framework.pay.core.client.impl.weixin.mode;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.framework.pay.core.client.exception.PayException;
import com.framework.pay.core.client.impl.weixin.AbstractWxPayClient;
import com.framework.pay.core.client.impl.weixin.WxPayClientConfig;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import com.framework.pay.core.enums.order.PayOrderDisplayModeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信支付（公众号）的 PayClient 实现类
 * 文档：<a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml">JSAPI 下单</>
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
@Slf4j
public class WxPubPayClient extends AbstractWxPayClient {

    public WxPubPayClient(Long channelId, WxPayClientConfig config) {
        super(channelId, PayChannelEnum.WX_PUB, config);
    }

    protected WxPubPayClient(Long channelId, PayChannelEnum channel, WxPayClientConfig config) {
        super(channelId, channel, config);
    }

    @Override
    protected void doInit() {
        super.doInit(WxPayConstants.TradeType.JSAPI);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV2(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderRequest 对象
        WxPayUnifiedOrderRequest request = buildPayUnifiedOrderRequestV2(reqDTO)
                .setOpenid(getOpenid(reqDTO));
        // 执行请求
        WxPayMpOrderResult response = client.createOrder(request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getMode(), JSONUtil.toJsonStr(response),
                reqDTO.getOutTradeNo(), response);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV3(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderRequest 对象
        WxPayUnifiedOrderV3Request request = buildPayUnifiedOrderRequestV3(reqDTO)
                .setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(getOpenid(reqDTO)));
        // 执行请求
        WxPayUnifiedOrderV3Result.JsapiResult response = client.createOrderV3(TradeTypeEnum.JSAPI, request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getMode(), JSONUtil.toJsonStr(response),
                reqDTO.getOutTradeNo(), response);
    }

    // ========== 各种工具方法 ==========

    static String getOpenid(PayOrderUnifiedReqDTO reqDTO) {
        String openid = MapUtil.getStr(reqDTO.getChannelExtras(), "openid");
        if (StrUtil.isEmpty(openid)) {
            throw new PayException(401, "支付请求的 openid 不能为空！");
        }
        return openid;
    }

}
