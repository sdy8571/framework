package com.framework.auth;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthTest {

    /**
     * 加密数据测试
     */
    @Test
    public void test() {
        String secret = "MTEyMzE0c2RmY3NmdmU0NTZ5YmNmbjU2NzU2dTU2bmZoNHJ0NTY3NDU2NDU2NDU2ZHYzMmhreWxveGNiczEyeGI0NTY0NXU3NTg2N2k5dm5zZGYyMzQ2";

        Map<String, Object> payload = new HashMap<>(6);
        payload.put("username", "1861120618");
        payload.put("buyerUserId", 10091);
        payload.put("shopId", 189);
        payload.put(JWTPayload.ISSUED_AT, DateTime.now());
        payload.put(JWTPayload.EXPIRES_AT, 1000000000);

        JWTSigner signer = JWTSignerUtil.hs512(secret.getBytes());
        String token = JWTUtil.createToken(payload, signer);
        System.out.println(token);
        boolean res = JWTUtil.verify(token, signer);
        System.out.println(res);

        JWT jwt = JWTUtil.parseToken(token);
        Object username = jwt.getPayload("username");
        System.out.println(username);
        Object buyerUserId = jwt.getPayload("buyerUserId");
        System.out.println(buyerUserId);
        Object shopId = jwt.getPayload("shopId");
        System.out.println(shopId);
        JSONObject jwtJson = jwt.getPayloads();
        System.out.println(jwtJson.getLong("buyerUserId"));
    }

}
