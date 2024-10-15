package com.framework.base.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author shen_dy@halcyonz.com
 * @date 2022/11/17
 */
public class DecimalUtils {

	public static final BigDecimal FEN = new BigDecimal("0.01");
	public static final BigDecimal HALF = new BigDecimal("0.5");
	public static final BigDecimal TWO = new BigDecimal(2);
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	public static final BigDecimal THOUSAND = new BigDecimal(1000);
	public static final BigDecimal TEN_THOUSAND = new BigDecimal(10000);
	private static final String NULL_STR = "null";

	/**
	 * decimal判空
	 * @param b 参数
	 * @return 结果
	 */
	public static boolean isNull(BigDecimal b) {
        return b == null;
	}
	/**
	 * decimal判断非空
	 * @param b 参数
	 * @return 结果
	 */
	public static boolean isNotNull(BigDecimal b) {
        return b != null;
	}
	/**
	 * 与 0 比较大小
	 * @param targetB 参数
	 * @return 大于返回1，小于返回-1，等于返回0
	 */
	public static int compareToZero(BigDecimal targetB) {
		return compareToZero(targetB, false);
	}
	/**
	 * 与 0 比较大小
	 * @param targetB 参数
	 * @param isNullFlag 是否判空
	 * @return 结果
	 */
	public static int compareToZero(BigDecimal targetB, boolean isNullFlag) {
		int res = 999;
		if (targetB == null) {
			if (isNullFlag) {
				throw new RuntimeException("目标对象为空");
			} else {
				targetB = BigDecimal.ZERO;
			}
		}
		
		if (targetB.compareTo(BigDecimal.ZERO) == 0) {
			res = 0;
		} else if (targetB.compareTo(BigDecimal.ZERO) > 0) {
			res = 1;
		} else if (targetB.compareTo(BigDecimal.ZERO) < 0) {
			res = -1;
		}
		return res;
	}

	/**
	 * 数值比较
	 * @param s1 值1
	 * @param s2 值2
	 * @return if b1 > b2 return 1;
	 *         if b1 = b2 return 0;
	 *         if b1 < b2 return -1;
	 */
	public static int compare(String s1, String s2) {
		// 校验
		if (StringUtils.isBlank(s1) || StringUtils.isBlank(s2)) {
			throw  new RuntimeException("比较对象不能为空");
		}
		return compare(toDecimal(s1), toDecimal(s2));
	}

	/**
	 * 数值比较
	 * @param b1 值1
	 * @param b2 值2
	 * @return if b1 > b2 return 1;
	 *         if b1 = b2 return 0;
	 *         if b1 < b2 return -1;
	 */
	public static int compare(BigDecimal b1, BigDecimal b2) {
		// 判空
		if (b1 == null || b2 == null) {
			throw  new RuntimeException("比较对象不能为空");
		}

		if (b1.compareTo(b2) == 0) {
			return 0;
		} else if (b1.compareTo(b2) > 0) {
			return 1;
		} else if (b1.compareTo(b2) < 0) {
			return -1;
		} else {
			return 999;
		}
	}
	/**
	 * 设置经度 默认四舍五入，精度为2
	 * @param val 参数
	 * @return 结果
	 */
	public static String scaleStr(String val) {
		if (StringUtils.isBlank(val)) {
			return null;
		}
		return scaleStr(new BigDecimal(val));
	}
	/**
	 * 设置经度 默认四舍五入，精度为2
	 * @param _b1 参数
	 * @return 结果
	 */
	public static String scaleStr(BigDecimal _b1) {
		return String.valueOf(scale(_b1));
	}
	/**
	 * 设置经度 默认四舍五入，精度为2
	 * @param _b1 参数
	 * @return 结果
	 */
	public static BigDecimal scale(BigDecimal _b1) {
		return scale(_b1, 2, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 设置经度 默认四舍五入，精度为2
	 * @param val 参数
	 * @return 结果
	 */
	public static BigDecimal scale(String val) {
		if (StringUtils.isBlank(val)) {
			return null;
		}
		return scale(new BigDecimal(val));
	}
	/**
	 * 设置经度
	 * @param _b1 参数
	 * @param scale 参数
	 * @param mode 参数
	 * @return 结果
	 */
	public static BigDecimal scale(BigDecimal _b1, int scale, int mode) {
		if (_b1 == null) {
			return null;
		}
		return _b1.setScale(scale, mode);
	}
	
	/**
	 * 加法
	 * @param bs 参数
	 * @return 结果
	 */
	public static BigDecimal add(BigDecimal... bs) {
		BigDecimal res = BigDecimal.ZERO;
		for (BigDecimal b : bs) {
			if (b == null) {
				b = BigDecimal.ZERO;
			}
			res = res.add(b);
		}
		return res;
	}

	/**
	 * 加法
	 * @param ss 参数
	 * @return 结果
	 */
	public static BigDecimal add(String... ss) {
		BigDecimal res = BigDecimal.ZERO;
		if (ss == null) {
			return res;
		}
		for (String s : ss) {
			if (StringUtils.isBlank(s)) {
				s = "0";
			}
			res = res.add(toDecimal(s));
		}
		return res;
	}
	
	/**
	 * 减法
	 * @param b1 被减数
	 * @param b2 减数
	 * @return 结果
	 */
	public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
		if (b1 == null) {
			b1 = BigDecimal.ZERO;
		}
		if (b2 == null) {
			b2 = BigDecimal.ZERO;
		}
		return b1.subtract(b2);
	}
	
	/**
	 * 乘法
	 * @param b1 参数
	 * @param b2 参数
	 * @return 结果
	 */
	public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
		if (b1 == null) {
			b1 = BigDecimal.ZERO;
		}
		if (b2 == null) {
			b2 = BigDecimal.ZERO;
		}
		return b1.multiply(b2);
	}
	/**
	 * 乘法
	 * @param b 参数
	 * @return 结果
	 */
	public static BigDecimal multiplyHundred(String b) {
		return multiplyHundred(toDecimal(b));
	}
	/**
	 * 乘法
	 * @param b 参数
	 * @return 结果
	 */
	public static BigDecimal multiplyHundred(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : b.multiply(HUNDRED);
	}
	/**
	 * 乘法
	 * @param b 参数
	 * @return 结果
	 */
	public static BigDecimal multiplyThousand(String b) {
		return multiplyThousand(toDecimal(b));
	}
	/**
	 * 乘法
	 * @param b 参数
	 * @return 结果
	 */
	public static BigDecimal multiplyThousand(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : b.multiply(THOUSAND);
	}
	/**
	 * 乘法
	 * @param b 参数
	 * @return 结果
	 */
	public static BigDecimal multiplyTenThousand(String b) {
		return multiplyTenThousand(toDecimal(b));
	}
	/**
	 * 乘法
	 * @param b 参数
	 * @return 结果
	 */
	public static BigDecimal multiplyTenThousand(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : b.multiply(TEN_THOUSAND);
	}

	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideHundred(String b) {
		return divideHundred(toDecimal(b));
	}
	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideHundred(Long b) {
		return divideHundred(toDecimal(b));
	}
	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideHundred(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : divide(b, HUNDRED);
	}
	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideThousand(String b) {
		return divideThousand(toDecimal(b));
	}
	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideThousand(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : divide(b, THOUSAND);
	}
	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideTenThousand(String b) {
		return divideTenThousand(toDecimal(b));
	}
	/**
	 * 除法
	 * @param b 被除数
	 * @return 结果
	 */
	public static BigDecimal divideTenThousand(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : divide(b, TEN_THOUSAND);
	}
	/**
	 * 除法
	 * @param b1 被除数
	 * @param b2 除数
	 * @return 结果
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
		return divide(b1, b2, 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 除法
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 结果
	 */
	public static BigDecimal divide(Long v1, Long v2) {
		return divide(toDecimal(v1), toDecimal(v2), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 除法
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 结果
	 */
	public static BigDecimal divide(Integer v1, Integer v2) {
		return divide(toDecimal(v1), toDecimal(v2), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 除法
	 * @param b1 被除数
	 * @param b2 除数
	 * @param scale 精度
	 * @return 结果
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale) {
		return divide(b1, b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 除法
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 精度
	 * @return 结果
	 */
	public static BigDecimal divide(Long v1, Long v2, int scale) {
		return divide(toDecimal(v1), toDecimal(v2), scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 除法
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 精度
	 * @return 结果
	 */
	public static BigDecimal divide(Integer v1, Integer v2, int scale) {
		return divide(toDecimal(v1), toDecimal(v2), scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 除法
	 * @param b1 被除数
	 * @param b2 除数
	 * @param scale 精度
	 * @param mode 舍入模式
	 * @return 结果
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale, int mode) {
		if (b1 == null) {
			b1 = BigDecimal.ZERO;
		}
		if (b2 == null || compareToZero(b2) == 0) {
			b2 = BigDecimal.ONE;
		}
		BigDecimal res = b1.divide(b2, scale, mode);
		return res;
	}

	/**
	 * 幂运算
	 * @param b1 底数
	 * @param b2 指数
	 * @return 结果
	 */
	public static BigDecimal pow(BigDecimal b1, int b2) {
		return pow(b1, b2, 2);
	}

	/**
	 * 幂运算
	 * @param b1 底数
	 * @param b2 指数
	 * @param scale 精度
	 * @return 结果
	 */
	public static BigDecimal pow(BigDecimal b1, int b2, int scale) {
		return pow(b1, b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 幂运算
	 * @param b1 底数
	 * @param b2 指数
	 * @param scale 精度
	 * @param mode 舍入模式
	 * @return 结果
	 */
	public static BigDecimal pow(BigDecimal b1, int b2, int scale, int mode) {
		if (b1 == null) {
			b1 = BigDecimal.ZERO;
		}
		BigDecimal res = b1.pow(b2);
		return scale(res, scale, mode);
	}

	/**
	 * bigDecimal 转 integer
	 * @param b 源数
	 * @return 结果
	 */
	public static Integer toInteger(BigDecimal b) {
		int i = 0;
		if (b != null) {
			i = b.intValueExact();
		}
		return i;
	}

	/**
	 * bigDecimal 转 long
	 * @param b 源数
	 * @return 结果
	 */
	public static Long toLong(BigDecimal b) {
		long l = 0L;
		if (b != null) {
			l = b.longValueExact();
		}
		return l;
	}

	/**
	 * bigDecimal 转 double
	 * @param b 源数
	 * @return 结果
	 */
	public static Double toDouble(BigDecimal b) {
		double d = 0.0;
		if (b != null) {
			d = b.doubleValue();
		}
		return d;
	}

	/**
	 * String 转换成 decimal
	 * @param value 参数
	 * @return 结果
	 */
	public static BigDecimal toDecimal(String value) {
		if (value== null || StringUtils.isBlank(value) || NULL_STR.equalsIgnoreCase(value)) {
			return BigDecimal.ZERO;
		} else {
			try{
				return new BigDecimal(value.trim());
			}catch (Exception e) {
				return BigDecimal.ZERO;
			}

		}
	}

	/**
	 * integer 转换成 decimal
	 * @param value 参数
	 * @return 结果
	 */
	public static BigDecimal toDecimal(Integer value) {
		if (value == null) {
			return null;
		} else {
			return new BigDecimal(value);
		}
	}

	/**
	 * long 转换成 decimal
	 * @param value 参数
	 * @return 结果
	 */
	public static BigDecimal toDecimal(Long value) {
		if (value == null) {
			return null;
		} else {
			return new BigDecimal(value);
		}
	}

	/**
	 * double 转换成 deciaml
	 * @param value 参数
	 * @return 结果
	 */
	public static BigDecimal toDecimal(Double value) {
		if (value == null) {
			return null;
		} else {
			return new BigDecimal(value);
		}
	}

	/**
	 * 金额分转元
	 * @param value 参数
	 * @return  结果
	 */
	public static BigDecimal moneyF2Y(Integer value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		return divideHundred(toDecimal(value));
	}

	/**
	 * 金额分转元
	 * @param value 参数
	 * @return  结果
	 */
	public static String moneyF2YStr(Integer value) {
		if (value == null) {
			return "0";
		}
		return scaleStr(divideHundred(toDecimal(value)));
	}

	/**
	 * 金额元转分
	 * @param value 参数
	 * @return  结果
	 */
	public static Integer moneyY2F(BigDecimal value) {
		if (value == null) {
			return 0;
		}
		return toInteger(multiplyHundred(value));
	}

	/**
	 * 金额元转分
	 * @param value 参数
	 * @return  结果
	 */
	public static Integer moneyY2F(String value) {
		if (value == null) {
			return 0;
		}
		return toInteger(multiplyHundred(value));
	}

}
