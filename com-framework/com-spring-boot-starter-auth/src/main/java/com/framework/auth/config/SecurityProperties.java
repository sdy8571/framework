/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.framework.auth.config;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Jwt参数配置
 *
 * @date 2019年11月28日
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "security.jwt", ignoreUnknownFields = true)
@Component
public class SecurityProperties {

    /**
     * Request Headers ： Authorization
     */
    private String header;

    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    private String tokenStartWith;

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/秒
     */
    private Long tokenValidityInSeconds;

    /**
     * refreshToken 过期时间 此处单位/秒
     */
    private Long refreshTokenValidityInSeconds;

    /**
     * 记住我登录 令牌过期时间
     */
    private Long rememberLoginValidity;

    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    private String onlineKey;

    /**
     * 验证码 key
     */
    private String codeKey;

    /**
     * token 续期检查
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;

    /**
     * 用户登录存放redis key
     */
    private String userLoginKey;

    /**
     * 一个账户是否允许多个客户端登录
     */
    private boolean multiLogin;

    /**
     * 登录拦截器放行请求路径
     */
    private List<String> ignorePaths;

    public String getTokenStartWith() {
        if (StringUtils.hasText(tokenStartWith)) {
            return tokenStartWith + " ";
        }
        return "";
    }

    /**
     * 判断当前路径是否需要认证
     * @param path 路径
     * @return
     */
    public boolean isNeedAuth(String path) {
        boolean needAuth = true;
        PathMatcher matcher = new AntPathMatcher();
        if(ObjectUtil.isNotEmpty(this.getIgnorePaths())){
            for (String ignorePath : this.getIgnorePaths()) {
                if (matcher.match(ignorePath, path)) {
                    needAuth = false;
                }
            }
        }
        return needAuth;
    }

    public byte[] getSecretKey() {
        try {
            if (!StringUtils.hasText(base64Secret)) {
                throw new RuntimeException("请求秘钥为空，加载失败");
            }
            return base64Secret.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("获取秘钥失败，{}", ex);
            throw ex;
        }
    }

}
