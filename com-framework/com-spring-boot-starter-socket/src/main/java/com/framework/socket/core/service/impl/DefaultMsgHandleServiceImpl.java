package com.framework.socket.core.service.impl;

import com.framework.common.pojo.Result;
import com.framework.socket.core.domain.BaseSocketReq;
import com.framework.socket.core.domain.CommandEnum;
import com.framework.socket.core.service.MsgHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
@Slf4j
@Service
public class DefaultMsgHandleServiceImpl implements MsgHandleService {

    @Override
    public int getCode() {
        return CommandEnum.DEFAULT.getCode();
    }

    @Override
    public Result<?> handle(BaseSocketReq req) {
        return Result.success("消息默认处理器。");
    }
}
