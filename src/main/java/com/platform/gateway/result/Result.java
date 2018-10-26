package com.platform.gateway.result;

import java.io.Serializable;

public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6946093044730138913L;
	private String msg;
	private int code;
	private Object data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String toString(){
		return "{" +
				"\"code\":" + code + "," +
				"\"msg\":\"" + msg + "\"," +
				"\"data:\"" + data
				+ "}"
				;
	}

}
