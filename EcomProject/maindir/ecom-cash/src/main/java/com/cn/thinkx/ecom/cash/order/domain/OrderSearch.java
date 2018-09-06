package com.cn.thinkx.ecom.cash.order.domain;

/**
 * 查询参数
 * 
 * @author xiaomei
 *
 */
public class OrderSearch {
	
	private String orderId;		//商户订单号
	private String txnFlowNo;	//知了企服交易流水号
	private String channel;		//渠道号
	private String userId;		//用户ID
	
	private String respResult;		//成功失败标识
	private String info;			//失败信息（只有当respResult=FAIL才会返回）
	private String innerMerchantNo;	//商户号
	private String innerShopNo;		//门店号
	private String settleDate;		//清算日期
	private String oriTxnAmount;	//原交易金额，支付请求支付金额
	private String txnAmount;		//实际交易金额
	private String attach;			//附加数据
	
	private String sign;	//签名

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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

	public String getTxnFlowNo() {
		return txnFlowNo;
	}

	public void setTxnFlowNo(String txnFlowNo) {
		this.txnFlowNo = txnFlowNo;
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

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "OrderSearch [orderId=" + orderId + ", txnFlowNo=" + txnFlowNo + ", channel=" + channel + ", userId="
				+ userId + ", respResult=" + respResult + ", info=" + info + ", innerMerchantNo=" + innerMerchantNo
				+ ", innerShopNo=" + innerShopNo + ", settleDate=" + settleDate + ", oriTxnAmount=" + oriTxnAmount
				+ ", txnAmount=" + txnAmount + ", attach=" + attach + ", sign=" + sign + "]";
	}

}
