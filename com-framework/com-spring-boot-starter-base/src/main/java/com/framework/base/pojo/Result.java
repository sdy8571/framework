package com.framework.base.pojo;

import com.framework.base.exception.GlobalErrorCodeConstants;
import lombok.Data;
import org.springframework.util.Assert;
import com.framework.base.exception.ErrorCode;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/30
 */
@Data
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 错误码
	 *
	 * @see ErrorCode#getCode()
	 */
	private Integer code;
	/**
	 * 返回数据
	 */
	private T data;
	/**
	 * 错误提示，用户可阅读
	 *
	 * @see ErrorCode#getMsg() ()
	 */
	private String msg;

	/**
	 * 将传入的 result 对象，转换成另外一个泛型结果的对象
	 *
	 * 因为 A 方法返回的 CommonResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
	 *
	 * @param result 传入的 result 对象
	 * @param <T>    返回的泛型
	 * @return 新的 CommonResult 对象
	 */
	public static <T> Result<T> error(Result<?> result) {
		return error(result.code, result.msg);
	}

	public static <T> Result<T> error(Integer code, String message) {
		Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
		Result<T> result = new Result<>();
		result.code = code;
		result.msg = message;
		return result;
	}

	public static <T> Result<T> error(ErrorCode errorCode) {
		return error(errorCode.getCode(), errorCode.getMsg());
	}

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
		result.data = data;
		result.msg = "";
		return result;
	}

	public static boolean isSuccess(Integer code) {
		return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
	}

	public boolean isSuccess() {
		return isSuccess(this.code);
	}

}
