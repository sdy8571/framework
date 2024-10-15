package com.framework.base.constants;

import java.util.Objects;

/**
 * @author sdy
 * @description
 * @date 2024/5/31
 */
public class BaseConstants {

    public enum BASE_STATUS {

        ENABLE(0, "开启"),
        DISABLE(1, "关闭"),
        YES(1, "是"),
        NO(0,"否"),
        ;

        private final Integer status;
        private final String name;

        public Integer getStatus() {
            return this.status;
        }

        public String getName() {
            return this.name;
        }

        BASE_STATUS(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public static boolean isEnable(Integer status) {
            return Objects.equals(status, ENABLE.getStatus());
        }

        public static boolean isYes(Integer status) {
            return Objects.equals(status, YES.getStatus());
        }
    }

}
