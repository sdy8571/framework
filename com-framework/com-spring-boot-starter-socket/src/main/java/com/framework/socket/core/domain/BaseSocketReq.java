package com.framework.socket.core.domain;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/12/18
 */
@Data
public class BaseSocketReq {

    // 校验权限使用
    private String token;
    // 指令
    private Integer code;
    // 数据
    private String data;

}
