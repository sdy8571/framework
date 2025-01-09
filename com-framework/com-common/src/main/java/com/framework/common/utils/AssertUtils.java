package com.framework.common.utils;

import com.framework.common.constatnts.GlobalErrorCodeConstants;
import com.framework.common.exception.util.ServiceExceptionUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
public class AssertUtils {

    public static void isNull(Object object) {
        if (object == null) {
            ServiceExceptionUtil.exception(GlobalErrorCodeConstants.IS_NULL);
        }
    }

    public static void isBlank(String str) {
        if (StringUtils.isBlank(str)) {
            ServiceExceptionUtil.exception(GlobalErrorCodeConstants.IS_BLANK);
        }
    }

}
