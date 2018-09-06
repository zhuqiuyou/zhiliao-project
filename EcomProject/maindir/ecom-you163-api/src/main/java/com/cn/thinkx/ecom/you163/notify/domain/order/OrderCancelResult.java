package com.cn.thinkx.ecom.you163.notify.domain.order;

/**
 * 	订单回调
 * 	取消订单结果
 * @author zhuqiuyou
 *
 */
public class OrderCancelResult implements java.io.Serializable {

	private static final long serialVersionUID = -6836937041404134694L;
	
	private String orderId; //订单ID

	private int cancelStatus;  //取消状态 0:不允许取消，1:允许取消，2:待审核 
	
	private String rejectReason; //拒绝取消原因
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(int cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
}
