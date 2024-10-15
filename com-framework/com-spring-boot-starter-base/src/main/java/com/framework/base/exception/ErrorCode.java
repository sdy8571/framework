package com.framework.base.exception;

import lombok.Data;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/30
 */
@Data
public class ErrorCode {

	private final Integer code;
	private final String msg;

}
