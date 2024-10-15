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
package com.framework.auth.jwt;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.framework.auth.config.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author /
 */
@Slf4j
@Component
public class TokenProvider {

    @Autowired
    @Qualifier("securityProperties")
    private SecurityProperties securityProperties;

    /**
     * 创建Token 设置永不过期，
     *
     * @param payload /
     * @return token
     */
    public String createToken(Map<String, Object> payload) {
        return create(payload, securityProperties.getTokenValidityInSeconds());
    }

    /**
     * 创建refreshToken
     * @param payload
     * @return 刷新 token
     */
    public String createRefreshToken(Map<String, Object> payload) {
        return create(payload, securityProperties.getRefreshTokenValidityInSeconds());
    }

    /**
     * 校验 token
     * @param token token
     * @return true 通过，false 不通过
     */
    public boolean verify(String token) {
        return JWTUtil.verify(getToken(token), getSigner());
    }

    /**
     * 获取参数
     * @param token  token
     * @param payloadKey key
     * @return 结果
     */
    public <T> T getTokenInfo(String token, String payloadKey, Class<T> clazz) {
        try {
            JSONObject jwJson = getTokenInfos(token);
            return jwJson.get(payloadKey, clazz);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Object is not an instance of " + clazz.getName());
        }
    }

    /**
     * 获取参数
     * @param token token
     * @return 结果
     */
    public JSONObject getTokenInfos(String token) {
        JWT jwt = JWTUtil.parseToken(getToken(token));
        return jwt.getPayloads();
    }

    private String create(Map<String, Object> payload, Long expiresTime) {
        payload.put(JWTPayload.ISSUED_AT, DateTime.now());
        payload.put(JWTPayload.EXPIRES_AT, expiresTime);
        return securityProperties.getTokenStartWith() + JWTUtil.createToken(payload, getSigner());
    }

    private JWTSigner getSigner() {
        return JWTSignerUtil.hs512(securityProperties.getSecretKey());
    }

    private String getToken(String token) {
        if (token != null && token.startsWith(securityProperties.getTokenStartWith())) {
            return token.substring(7);
        }
        return null;
    }

}
