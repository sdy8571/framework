package com.framework.pay.core.enums.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 渠道的通知类型状态枚举
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
@Getter
@AllArgsConstructor
public enum PayNotifyTypeEnum {

    PAYMENT(0, "支付通知"),
    REFUND(10, "退款通知"),
    ;

    private final Integer status;
    private final String name;

    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, PAYMENT.getStatus());
    }

    public static boolean isFailure(Integer status) {
        return Objects.equals(status, REFUND.getStatus());
    }

}
