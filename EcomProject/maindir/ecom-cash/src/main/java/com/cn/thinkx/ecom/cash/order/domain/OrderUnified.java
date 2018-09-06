package com.cn.thinkx.ecom.cash.order.domain;

/**
 * 下单参数
 * 
 * @author xiaomei
 *
 */
public class OrderUnified {

	private String channel;		//渠道号
	private String orderId; 	//订单号
	private String userId;		//用户ID
	private String commodityName;	//商品名称
	private String commodityNum;	//商品数量
	private String txnAmount;		//交易金额
	private String innerMerchantNo;	//商户号
	private String innerShopNo;		//门店号
	private String notify_url;		//支付通知地址
	private String redirect_type;	//重定向状态
	private String redirect_url;	//重定向地址
	private String attach;			//附加参数
	private String sign;			//签名

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityNum() {
		return commodityNum;
	}

	public void setCommodityNum(String commodityNum) {
		this.commodityNum = commodityNum;
	}

	public String getInnerMerchantNo() {
		return innerMerchantNo;
	}

	public void setInnerMerchantNo(String innerMerchantNo) {
		this.innerMerchantNo = innerMerchantNo;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRedirect_type() {
		return redirect_type;
	}

	public void setRedirect_type(String redirect_type) {
		this.redirect_type = redirect_type;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public String getInnerShopNo() {
		return innerShopNo;
	}

	public void setInnerShopNo(String innerShopNo) {
		this.innerShopNo = innerShopNo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "OrderUnified [channel=" + channel + ", orderId=" + orderId + ", userId=" + userId + ", commodityName="
				+ commodityName + ", commodityNum=" + commodityNum + ", txnAmount=" + txnAmount + ", innerMerchantNo="
				+ innerMerchantNo + ", innerShopNo=" + innerShopNo + ", notify_url=" + notify_url + ", redirect_type="
				+ redirect_type + ", redirect_url=" + redirect_url + ", attach=" + attach + ", sign=" + sign + "]";
	}

}
