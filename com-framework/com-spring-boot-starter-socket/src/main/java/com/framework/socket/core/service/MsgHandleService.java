package com.framework.socket.core.service;

import com.framework.common.pojo.Result;
import com.framework.socket.core.domain.BaseSocketReq;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
public interface MsgHandleService {

    int getCode();

    Result<?> handle(BaseSocketReq req);

}
