package com.cn.thinkx.oms.module.trans.util;


/**
 * 聚合支付交易返回对象(初始化为code:999 info:null)
 * 
 * @author xiaomei
 *
 */
public class BaseTransResp extends BaseResp {

	private String swtFlowNo;		//交易流水
	private String termSwtFlowNo;	//终端交易流水号
	private String transDate;		//交易时间
	private String remarks;			//备注
	private String transAmt;		//交易金额
	private String preferentialAmt;	//优惠金额
	private String termOrderNo;		//终端订单号
	private String paymentType;		//支付方式
	private long timestamp;		//时间戳
	private String sign;			//签名
	
	public String getSwtFlowNo() {
		return swtFlowNo;
	}
	public void setSwtFlowNo(String swtFlowNo) {
		this.swtFlowNo = swtFlowNo;
	}
	public String getTermSwtFlowNo() {
		return termSwtFlowNo;
	}
	public void setTermSwtFlowNo(String termSwtFlowNo) {
		this.termSwtFlowNo = termSwtFlowNo;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getPreferentialAmt() {
		return preferentialAmt;
	}
	public void setPreferentialAmt(String preferentialAmt) {
		this.preferentialAmt = preferentialAmt;
	}
	public String getTermOrderNo() {
		return termOrderNo;
	}
	public void setTermOrderNo(String termOrderNo) {
		this.termOrderNo = termOrderNo;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
