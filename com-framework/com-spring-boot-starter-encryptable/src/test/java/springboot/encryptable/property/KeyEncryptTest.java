package springboot.encryptable.property;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
public class KeyEncryptTest {

    private static RSA rsa;

    // 用于保护配置文件的密码
    private static final String PLAIN_KEY = "3zDFuT73qHytAjyV";

    private String decryptKey(String encKey) {
        String decData = Base64.encode(rsa.decrypt(Base64.decode(encKey), KeyType.PublicKey));
        String plainKey = new String(Base64.decode(decData));
        log.info("解密后的密钥:{}", plainKey);
        return plainKey;
    }

    @BeforeAll
    public static void beforeAll() {
        PrivateKey pri_key = BCUtil.readPemPrivateKey(ResourceUtil.getStream("key/private.pem"));
        PublicKey pub_key = BCUtil.readPemPublicKey(ResourceUtil.getStream("META-INF/key/public.pem"));
        rsa = new RSA(pri_key, pub_key);
    }

    @Test
    public void decryptPassword() {
        String encPassword = ResourceUtil.readUtf8Str("META-INF/key/password.txt");
        String plainPassword = new String(rsa.decrypt(encPassword, KeyType.PublicKey));
        log.info("密码明文:{}", plainPassword);
    }

    @Test
    public void encryptKey() {
        String encKey = rsa.encryptBase64(PLAIN_KEY, KeyType.PrivateKey);
        log.info("密钥密文:{}", encKey);
        String plainKey = decryptKey(encKey);
        log.info("密钥明文对比:{},{}", plainKey, PLAIN_KEY);
        Assertions.assertEquals(plainKey, PLAIN_KEY);
    }

    @Test
    public void encryptDevKey() {
        String encKey = rsa.encryptBase64("joycode", KeyType.PrivateKey);
        log.info("密钥密文:{}", encKey);
    }

}
