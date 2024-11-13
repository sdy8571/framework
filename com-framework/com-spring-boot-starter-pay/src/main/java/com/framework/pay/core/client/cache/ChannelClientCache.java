package com.framework.pay.core.client.cache;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.framework.pay.core.client.PayClient;
import com.framework.pay.core.client.PayClientConfig;
import com.framework.pay.core.client.PayClientFactory;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import com.framework.pay.utils.PayUtils;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.time.Duration;

/**
 * @author sdy
 * @description
 * @date 2024/11/12
 */
@Component
public abstract class ChannelClientCache {

    @Resource
    private PayClientFactory payClientFactory;
    @Resource
    private Validator validator;

    protected abstract ChannelPO loadChannel(Long id);

    /**
     * {@link PayClient} 缓存，通过它异步清空 ClientFactory
     */
    @Getter
    protected final LoadingCache<Long, PayClient> clientCache = PayUtils.buildAsyncReloadingCache(Duration.ofSeconds(10L),
            new CacheLoader<Long, PayClient>() {
                @Override
                public PayClient load(Long id) {
                    // 查询，然后尝试清空
                    ChannelPO channel = loadChannel(id);
                    if (channel != null) {
                        PayClientConfig config = parseConfig(channel.getCode(), channel.getConfig());
                        payClientFactory.createOrUpdatePayClient(id, channel.getCode(), config);
                    }
                    return payClientFactory.getPayClient(id);
                }
            });

    /**
     * 解析并校验配置
     * @param code 渠道编码
     * @param configStr 配置
     * @return 支付配置
     */
    private PayClientConfig parseConfig(String code, String configStr) {
        // 解析配置
        Class<? extends PayClientConfig> payClass = PayChannelEnum.getByCode(code).getConfigClass();
        Assert.notNull(payClass);

        PayClientConfig config = JSONUtil.toBean(configStr, payClass);
        Assert.notNull(config);

        // 验证参数
        config.validate(validator);
        return config;
    }

}
