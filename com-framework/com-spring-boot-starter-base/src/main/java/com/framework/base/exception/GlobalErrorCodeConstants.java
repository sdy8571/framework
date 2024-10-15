package com.framework.base.exception;


/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/30
 */
public interface GlobalErrorCodeConstants {

	ErrorCode SUCCESS = new ErrorCode(200, "成功");
	ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数不正确");
	ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");
	ErrorCode FORBIDDEN = new ErrorCode(403, "没有该操作权限");
	ErrorCode NOT_FOUND = new ErrorCode(404, "请求未找到");
	ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "请求方法不正确");
	ErrorCode LOCKED = new ErrorCode(423, "请求失败，请稍后重试");
	ErrorCode TOO_MANY_REQUESTS = new ErrorCode(429, "请求过于频繁，请稍后重试");
	ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常");
	ErrorCode NOT_IMPLEMENTED = new ErrorCode(501, "功能未实现/未开启");
	ErrorCode REPEATED_REQUESTS = new ErrorCode(900, "重复请求，请稍后重试");
	ErrorCode DEMO_DENY = new ErrorCode(901, "演示模式，禁止写操作");
	ErrorCode UNKNOWN = new ErrorCode(999, "未知错误");
}
