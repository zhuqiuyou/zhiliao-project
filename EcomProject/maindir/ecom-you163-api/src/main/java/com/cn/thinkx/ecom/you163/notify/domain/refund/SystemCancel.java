package com.cn.thinkx.ecom.you163.notify.domain.refund;

/**
 * 严选系统取消退货信息
 * @author zhuqiuyou
 *
 */
public class SystemCancel implements java.io.Serializable{

	private static final long serialVersionUID = -6835552314139985545L;
	
	private String applyId;  //申请单Id
	
	private String orderId;  //订单Id
	
	private String errorMsg;  //系统错误原因

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
