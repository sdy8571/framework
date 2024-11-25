package com.framework.banner.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Date;

/**
 * @author sdy
 * @description
 * @date 2024/11/25
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1000);
            log.info("启动时间：" + DateUtil.formatDateTime(new Date()));
            log.info("\n"
                    + " (♥◠‿◠)ﾉﾞ  服务启动成功   ლ(´ڡ`ლ)ﾞ \n"
                    + "              ___                            ___     \n"
                    + "            ,--.'|_                        ,--.'|_   \n"
                    + "            |  | :,'              __  ,-.  |  | :,'  \n"
                    + "  .--.--.   :  : ' :            ,' ,'/ /|  :  : ' :  \n"
                    + " /  /    '.;__,'  /    ,--.--.  '  | |' |.;__,'  /   \n"
                    + "|  :  /`./|  |   |    /       \\ |  |   ,'|  |   |    \n"
                    + "|  :  ;_  :__,'| :   .--.  .-. |'  :  /  :__,'| :    \n"
                    + " \\  \\    `. '  : |__  \\__\\/: . .|  | '     '  : |__  \n"
                    + "  `----.   \\|  | '.'| ,\" .--.; |;  : |     |  | '.'| \n"
                    + " /  /`--'  /;  :    ;/  /  ,.  ||  , ;     ;  :    ; \n"
                    + "'--'.     / |  ,   /;  :   .'   \\---'      |  ,   /  \n"
                    + "  `--'---'   ---`-' |  ,     .-./           ---`-'   \n"
                    + "                     `--`---'                        \n"
                    + "\n");
        });
    }

}
