package com.cn.thinkx.ecom.you163.notify.domain.refund;


/**
 * 退货回调
 * 退货地址信息
 * @author zhuqiuyou
 *
 */
public class RefundAddress implements java.io.Serializable {
	
	private static final long serialVersionUID = -1332193862052141701L;

	private String applyId;
	
	private String orderId;
	
	private Integer type;
	
	private ReturnAddr returnAddr;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public ReturnAddr getReturnAddr() {
		return returnAddr;
	}

	public void setReturnAddr(ReturnAddr returnAddr) {
		this.returnAddr = returnAddr;
	}

}
