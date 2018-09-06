package com.cn.thinkx.common.wecard.domain.base;

import java.io.Serializable;

public class ResultHtml implements Serializable {

	private boolean status;

	private String code="1";  //1：页面提示 ，0:跳转   ------>可以自己定义

	private String msg;
	
	private String orderId;
	
	private String check;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}
}
