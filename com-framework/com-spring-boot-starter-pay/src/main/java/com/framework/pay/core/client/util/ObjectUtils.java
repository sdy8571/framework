package com.framework.pay.core.client.util;

import java.util.Arrays;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
public class ObjectUtils {
	@SafeVarargs
	public static <T> boolean equalsAny(T obj, T... array) {
		return Arrays.asList(array).contains(obj);
	}

}
