package com.cn.thinkx.ecom.you163.notify.domain.refund;


/**
 * 	严选拒绝退货信息
 * @author zhuqiuyou
 *
 */
public class RejectInfo implements java.io.Serializable {

	private static final long serialVersionUID = -3156248346799042138L;
	
	String applyId; //申请单Id  
	
	String orderId;//订单Id 	
	
	String rejectReason;//
//	
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
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
}
