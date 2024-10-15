package com.framework.encrytable.encrypt;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import java.security.PublicKey;

public class ImplEncryptablePropertyResolver implements EncryptablePropertyResolver {


    private final StandardPBEStringEncryptor encryptor;

    public ImplEncryptablePropertyResolver(String password) {
        encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        // 指定算法
        config.setAlgorithm("PBEWithMD5AndDES");
        // 指定秘钥，和yml配置文件中保持一致
        // 解密密码用RSA再加密一次
        PublicKey pub_key = BCUtil.readPemPublicKey(ResourceUtil.getStream("META-INF/key/public.pem"));
        RSA rsa = new RSA(null, pub_key);
        String pwd = new String(rsa.decrypt(password, KeyType.PublicKey));
        config.setPassword(pwd);
        //
        encryptor.setConfig(config);
    }

    @Override
    public String resolvePropertyValue(String value) {
        if (StrUtil.isNotBlank(value) && value.startsWith("Enc(") && value.endsWith(")")) {
            String data = StrUtil.sub(value, "Enc(".length(), value.length() - 1);
            return encryptor.decrypt(data);
        }
        return value;
    }
}
