package com.framework.pay.core.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 渠道的支付状态枚举
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
@Getter
@AllArgsConstructor
public enum PayOrderStatusRespEnum {

    WAITING(0, "未支付"),
    SUCCESS(10, "支付成功"),
    REFUND(20, "已退款"),
    CLOSED(30, "支付关闭"),
    ;

    private final Integer status;
    private final String name;

    /**
     * 判断是否支付成功
     *
     * @param status 状态
     * @return 是否支付成功
     */
    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, SUCCESS.getStatus());
    }

    /**
     * 判断是否已退款
     *
     * @param status 状态
     * @return 是否支付成功
     */
    public static boolean isRefund(Integer status) {
        return Objects.equals(status, REFUND.getStatus());
    }

    /**
     * 判断是否支付关闭
     *
     * @param status 状态
     * @return 是否支付关闭
     */
    public static boolean isClosed(Integer status) {
        return Objects.equals(status, CLOSED.getStatus());
    }

    /**
     * 判断统一下单是否成功
     * @param status 状态
     * @return 是否下单成功
     */
    public static boolean isUnifiedSuccess(Integer status) {
        return Arrays.asList(SUCCESS.getStatus(), WAITING.getStatus()).contains(status);
    }

}
