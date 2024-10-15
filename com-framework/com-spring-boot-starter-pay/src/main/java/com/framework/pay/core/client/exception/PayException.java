package com.framework.pay.core.client.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayException extends RuntimeException {

	/**
	 * 业务错误码
	 *
	 * @see
	 */
	private Integer code;
	/**
	 * 错误提示
	 */
	private String message;

	/**
	 * 空构造方法，避免反序列化问题
	 */
	public PayException() {
	}

	public PayException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public PayException(Throwable cause) {
		super(cause);
	}

}
