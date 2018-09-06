package com.cn.thinkx.ecom.cash.order.domain.hkb;

public class HKBOrder {

	private String code;			//返回码
	private String info;			//返回描述
	private String swtFlowNo;		//原交易流水号
	private String settleDate;		//清算日期，格式为yyyyMMdd
	private String txnFlowNo;		//交易流水号
	private String oriTxnAmount;	//原交易金额，活动前交易金额
	private String txnAmount;		//实际交易金额
	private String balance;			//余额，交易后账户余额
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSwtFlowNo() {
		return swtFlowNo;
	}
	public void setSwtFlowNo(String swtFlowNo) {
		this.swtFlowNo = swtFlowNo;
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
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "HKBOrder [code=" + code + ", info=" + info + ", swtFlowNo=" + swtFlowNo + ", settleDate=" + settleDate
				+ ", txnFlowNo=" + txnFlowNo + ", oriTxnAmount=" + oriTxnAmount + ", txnAmount=" + txnAmount
				+ ", balance=" + balance + "]";
	}
	
}
