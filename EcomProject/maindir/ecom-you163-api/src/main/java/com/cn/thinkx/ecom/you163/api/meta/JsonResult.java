package com.cn.thinkx.ecom.you163.api.meta;

import java.util.LinkedHashMap;

import org.springframework.util.Assert;

public class JsonResult extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -62401379125965102L;

	/*
	 * 响应状态码，默认ResponseCode.SUCCESS
	 */
	public static final String CODE = "code";

	/*
	 * 响应详情说明
	 */
	public static final String MESSAGE = "msg";

	/*
	 * 响应详情数据
	 */
	public static final String RESULT = "result";

	/*
	 * 默认构造器
	 */
	public JsonResult() {
		setCode(200);
	}

	/*
	 * 状态码构造器
	 */
	public JsonResult(int responseCode) {
		setCode(responseCode);
		setMessage("");
		setResult(null);
	}

	/*
	 * 状态码 + 消息详情构造器
	 */
	public JsonResult(int responseCode, String message) {
		setCode(responseCode);
		setMessage(message);
		setResult(null);
	}

	public void setCode(int responseCode) {
		Assert.notNull(responseCode, "responseCode can not be null");
		put(CODE, responseCode);
	}

	public String getCode() {
		return get(CODE).toString();
	}

	public void setMessage(String message) {
		put(MESSAGE, message);
	}

	public String getMessage() {
		return (String) get(MESSAGE);
	}

	public void setResult(Object result) {
		put(RESULT, result);
	}

	public Object getResult() {
		return (Object) get(RESULT);
	}
}
