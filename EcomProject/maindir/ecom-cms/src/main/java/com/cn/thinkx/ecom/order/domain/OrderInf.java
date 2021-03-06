package com.cn.thinkx.ecom.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class OrderInf extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 渠道号
	 */
	private String channel;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 电商订单号
	 */
	private String routerOrderNo;
	/**
	 * 商户号
	 */
	private String merchantNo;
	/**
	 * 门店号
	 */
	private String shopNo;
	/**
	 * 商品名称
	 */
	private String commodityName;
	/**
	 * 商品数量
	 */
	private String commodityNum;
	/**
	 * 交易金额
	 */
	private String txnAmount;
	/**
	 * 订单状态   0未支付   1支付失败   2支付完成
	 */
	private String orderType;
	/**
	 * 支付通知地址
	 */
	private String notifyUrl;
	/**
	 * 重定向地址
	 */
	private String redirectUrl;
	/**
	 *知了企服流水（itf主键） 
	 */
	private String txnFlowNo;
	/**
	 * 电商原交易订单号
	 */
	private String orgId;
	/**
	 * 备用字段1
	 */
	private String resv1;
	/**
	 * 备用字段2
	 */
	private String resv2;
	/**
	 * 备用字段3
	 */
	private String resv3;
	/**
	 * 备用字段4
	 */
	private String resv4;
	/**
	 * 备用字段5
	 */
	private String resv5;
	/**
	 * 备用字段6
	 */
	private String resv6;
	
	//用户名称
	private String personalName;
	//清算日期
	private String settleDate;
	//交易总金额
	private String sumMoney;
	//交易笔数
	private String total;
	//商户名称
	private String eshopName;
	//查询时间类型（当天或历史）
	private String queryType;
	//开始时间
	private String beginTime;
	//结束时间
	private String endTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getRouterOrderNo() {
		return routerOrderNo;
	}

	public void setRouterOrderNo(String routerOrderNo) {
		this.routerOrderNo = routerOrderNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
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

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getEshopName() {
		return eshopName;
	}

	public void setEshopName(String eshopName) {
		this.eshopName = eshopName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getResv1() {
		return resv1;
	}

	public void setResv1(String resv1) {
		this.resv1 = resv1;
	}

	public String getResv2() {
		return resv2;
	}

	public void setResv2(String resv2) {
		this.resv2 = resv2;
	}

	public String getResv3() {
		return resv3;
	}

	public void setResv3(String resv3) {
		this.resv3 = resv3;
	}

	public String getResv4() {
		return resv4;
	}

	public void setResv4(String resv4) {
		this.resv4 = resv4;
	}

	public String getResv5() {
		return resv5;
	}

	public void setResv5(String resv5) {
		this.resv5 = resv5;
	}

	public String getResv6() {
		return resv6;
	}

	public void setResv6(String resv6) {
		this.resv6 = resv6;
	}

}
