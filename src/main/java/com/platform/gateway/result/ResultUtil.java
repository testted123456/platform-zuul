package com.platform.gateway.result;

public class ResultUtil {

	public static Result success(Object object) {
		Result result = new Result();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMsg(ResultCode.SUCCESS.getMsg());
		result.setData(object);
		return result;
	}
	
	public static Result success() {
		Result result = new Result();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMsg(ResultCode.SUCCESS.getMsg());
		return result;
	}

	public static Result error(int code, String msg) {
		Result result = new Result();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}
}
