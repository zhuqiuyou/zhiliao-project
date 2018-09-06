package com.cn.thinkx.ecom.you163.notify.domain.order;

/**
 * 订单异常回调
 * 
 * 订单异常信息
 * @author zhuqiuyou
 *
 */
public class ExceptionInfo {

	/*
	 * 订单ID
	 */
	private String orderId;
	/*
	 * 异常说明
	 */
	private String reason;
	/*
	 * 额外补充数据
	 */
	private String extData;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getExtData() {
		return extData;
	}
	public void setExtData(String extData) {
		this.extData = extData;
	}
	
}
