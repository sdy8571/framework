package com.framework.socket.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sdy
 * @description
 * @date 2024/12/18
 */
@Getter
@AllArgsConstructor
public enum CommandEnum {
    /**
     * 连接
     */
    CONNECTION(1),
    /**
     * 客户端关闭连接
     */
    CLOSE(9),
    /**
     * 默认消息处理器
     */
    DEFAULT(0),
    /**
     * 异常
     */
    ERROR(-1),
    ;

    private Integer code;

    public static CommandEnum match(Integer val) {
        if (val == null) {
            return ERROR;
        }
        return Arrays.stream(CommandEnum.values()).filter(c -> c.getCode().equals(val)).findFirst().orElse(null);
    }
}
