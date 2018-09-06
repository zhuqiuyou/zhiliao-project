package com.cn.thinkx.ecom.cash.order.domain;

/**
 * 异步回调参数
 * 
 * @author xiaomei
 *
 */
public class OrderBack {

	private String respResult;	//成功失败标识
	private String orderId;		//订单号
	private String channel;		//渠道号
	private String userId;		//用户ID
	private String txnFlowNo;	//交易流水号
	private String oriTxnAmount;//原交易金额（支付金额）
	private String txnAmount;	//实际交易金额
	private String innerMerchantNo;	//商户号
	private String innerShopNo;		//门店号
	private String settleDate;		//清算日期
	private String attach;	//附加信息
	private String sign;	//签名

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRespResult() {
		return respResult;
	}

	public void setRespResult(String respResult) {
		this.respResult = respResult;
	}

	public String getInnerMerchantNo() {
		return innerMerchantNo;
	}

	public void setInnerMerchantNo(String innerMerchantNo) {
		this.innerMerchantNo = innerMerchantNo;
	}

	public String getInnerShopNo() {
		return innerShopNo;
	}

	public void setInnerShopNo(String innerShopNo) {
		this.innerShopNo = innerShopNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getTxnFlowNo() {
		return txnFlowNo;
	}

	public void setTxnFlowNo(String txnFlowNo) {
		this.txnFlowNo = txnFlowNo;
	}

	public String getOriTxnAmount() {
		return oriTxnAmount;
	}

	public void setOriTxnAmount(String oriTxnAmount) {
		this.oriTxnAmount = oriTxnAmount;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "OrderBack [respResult=" + respResult + ", orderId=" + orderId + ", channel=" + channel + ", userId="
				+ userId + ", txnFlowNo=" + txnFlowNo + ", oriTxnAmount=" + oriTxnAmount + ", txnAmount=" + txnAmount
				+ ", innerMerchantNo=" + innerMerchantNo + ", innerShopNo=" + innerShopNo + ", settleDate=" + settleDate
				+ ", attach=" + attach + ", sign=" + sign + "]";
	}

}
