package com.framework.pay.core.client.dto.order;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/6/17
 */
@Data
public class PayRawDataWechatDTO {
    /**
     * appId
     */
    private String appId;
    /**
     * 时间戳，从 1970 年 1 月 1 日 00:00:00 至今的秒数，即当前的时间
     */
    private String timeStamp;
    /**
     * 随机字符串，长度为32个字符以下
     */
    private String nonceStr;
    /**
     * 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***
     */
    private String packageValue;
    /**
     * 签名算法，应与后台下单时的值一致
     */
    private String signType;
    /**
     * 签名，具体见微信支付文档
     */
    private String paySign;

}
