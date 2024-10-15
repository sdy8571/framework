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
import com.framework.pay.core.client.impl.weixin.WxPayClientConfig;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import com.framework.pay.core.enums.order.PayOrderDisplayModeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信支付【小程序】的 PayClient 实现类
 * 由于公众号和小程序的微信支付逻辑一致，所以直接进行继承
 * 文档：<a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_3_1.shtml">JSAPI 下单</>
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
@Slf4j
public class WxH5PayClient extends WxPubPayClient {

    public WxH5PayClient(Long channelId, WxPayClientConfig config) {
        super(channelId, PayChannelEnum.WX_H5.getCode(), config);
    }

    @Override
    protected void doInit() {
        super.doInit(WxPayConstants.TradeType.MWEB);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV2(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderRequest 对象
        WxPayUnifiedOrderRequest request = buildPayUnifiedOrderRequestV2(reqDTO);
        // 执行请求
        WxPayMpOrderResult response = client.createOrder(request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.URL.getMode(), JSONUtil.toJsonStr(response),
                reqDTO.getOutTradeNo(), response);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV3(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderV3Request 对象
        WxPayUnifiedOrderV3Request request = buildPayUnifiedOrderRequestV3(reqDTO);
        // 设置 H5 支付参数
        request.getSceneInfo().setH5Info(new WxPayUnifiedOrderV3Request.H5Info().setType(getH5Type(reqDTO)));
        // 执行请求
        WxPayUnifiedOrderV3Result.AppResult response = client.createOrderV3(TradeTypeEnum.H5, request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getMode(), JSONUtil.toJsonStr(response),
                reqDTO.getOutTradeNo(), response);
    }

    static String getH5Type(PayOrderUnifiedReqDTO reqDTO) {
        String type = MapUtil.getStr(reqDTO.getChannelExtras(), "type");
        if (StrUtil.isEmpty(type)) {
            throw new PayException(401, "H5 支付请求的 type 不能为空！");
        }
        return type;
    }

}
