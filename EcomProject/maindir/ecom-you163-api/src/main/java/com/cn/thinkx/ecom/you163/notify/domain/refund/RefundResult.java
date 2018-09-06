package com.cn.thinkx.ecom.you163.notify.domain.refund;

import java.util.List;

/**
 * 退款结果信息
 * @author zhuqiuyou
 *
 */
public class RefundResult implements java.io.Serializable {

	private static final long serialVersionUID = -7930307139513021932L;

	private String applyId;
	
	private String orderId;
	
	private List<RefundSku> refundSkuList;

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

	public List<RefundSku> getRefundSkuList() {
		return refundSkuList;
	}

	public void setRefundSkuList(List<RefundSku> refundSkuList) {
		this.refundSkuList = refundSkuList;
	}
	
}
