package springboot.encryptable.property;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class DataEncryptTest {

    private static final String ENC_PWD = "3zDFuT73qHytAjyV";

    // 创建加密器
    private static final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    @BeforeAll
    public static void beforeAll() {
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        // 指定算法
        config.setAlgorithm("PBEWithMD5AndDES");
        // 指定加密密码
        config.setPassword(ENC_PWD);
        // 指定加密器配置
        encryptor.setConfig(config);
    }

    /**
     * 数据加密
     */
    private void encryptData(String data) {
        // 生成加密数据
        log.info("数据加密:{}", encryptor.encrypt(data));
    }

    /**
     * 数据解密
     */
    private void decryptData(String data) {
        // 生成加密数据
        log.info("数据解密:{}", encryptor.decrypt(data));
    }

    /**
     * 加密数据测试
     */
    @Test
    public void encryptTest() {
        String data = "joycode";
        log.info("数据明文:{}", data);
        encryptData(data);
    }

    /**
     * 解密数据测试
     */
    @Test
    public void decryptTest() {
        String data = "fEuaWsDZyddKVSsslv7iSQ==";
        log.info("数据密文:{}", data);
        decryptData(data);
    }
}
