package com.platform.gateway.result;

public enum ResultCode {
	SUCCESS(10000, "成功"), 
	UNKOWN_ERROR(10001, "未知错误"), 
	VALIDATION_ERROR(10002, "对象校验失败"), 
	EMPTY_ERROR(10003, "对象为空"), 
	DB_ERROR(10004, "数据库操作错误"), 
	EXCEPTION_ERROR(10005, "发生异常"),
	HTTP_ERROR(1006, "HTTP请求异常"),
	LOGIN_ERROR(1007, "Have not login"),
	INSUFFICIENT_ERROR(1008,"Insufficient right"),
	;

	private int code;
	private String msg;

	ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
