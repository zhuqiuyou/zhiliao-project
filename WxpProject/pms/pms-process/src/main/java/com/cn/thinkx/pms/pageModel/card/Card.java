package com.cn.thinkx.pms.pageModel.card;

import java.util.Date;

public class Card  {
	private String cardNo;
	private String productCode;
	private String cvvDate;
	private String validDate;
	private String svcCode;
	private String cvv2ErrTimes;
	private String cardStat;
	private String cardStatCN;
	private String actDate;
	private String lockStat;
	private String lockDate;
	private String cancelStat;
	private String lostStat;
	private String freezeStat;
	private String txnPin;
	private String pinStat;
	private String nopintTxnAmt;
	private String pinErrTimes;
	private String createDate;
	private String rechargeNum;
	private String consumNum;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	private String stockStat;
	private Account account = new Account();
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getCardStatCN() {
		return cardStatCN;
	}
	public void setCardStatCN(String cardStatCN) {
		this.cardStatCN = cardStatCN;
	}
	public String getStockStat() {
		return stockStat;
	}
	public void setStockStat(String stockStat) {
		this.stockStat = stockStat;
	}
	public String getAccountAmt() {
		return accountAmt;
	}
	public void setAccountAmt(String accountAmt) {
		this.accountAmt = accountAmt;
	}
	private String accountAmt;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getCvvDate() {
		return cvvDate;
	}
	public void setCvvDate(String cvvDate) {
		this.cvvDate = cvvDate;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public String getSvcCode() {
		return svcCode;
	}
	public void setSvcCode(String svcCode) {
		this.svcCode = svcCode;
	}
	public String getCvv2ErrTimes() {
		return cvv2ErrTimes;
	}
	public void setCvv2ErrTimes(String cvv2ErrTimes) {
		this.cvv2ErrTimes = cvv2ErrTimes;
	}
	public String getCardStat() {
		return cardStat;
	}
	public void setCardStat(String cardStat) {
		this.cardStat = cardStat;
	}
	public String getActDate() {
		return actDate;
	}
	public void setActDate(String actDate) {
		this.actDate = actDate;
	}
	public String getLockStat() {
		return lockStat;
	}
	public void setLockStat(String lockStat) {
		this.lockStat = lockStat;
	}
	public String getCancelStat() {
		return cancelStat;
	}
	public void setCancelStat(String cancelStat) {
		this.cancelStat = cancelStat;
	}
	public String getLostStat() {
		return lostStat;
	}
	public void setLostStat(String lostStat) {
		this.lostStat = lostStat;
	}
	public String getFreezeStat() {
		return freezeStat;
	}
	public void setFreezeStat(String freezeStat) {
		this.freezeStat = freezeStat;
	}
	public String getTxnPin() {
		return txnPin;
	}
	public void setTxnPin(String txnPin) {
		this.txnPin = txnPin;
	}
	public String getPinStat() {
		return pinStat;
	}
	public void setPinStat(String pinStat) {
		this.pinStat = pinStat;
	}
	public String getNopintTxnAmt() {
		return nopintTxnAmt;
	}
	public void setNopintTxnAmt(String nopintTxnAmt) {
		this.nopintTxnAmt = nopintTxnAmt;
	}
	public String getPinErrTimes() {
		return pinErrTimes;
	}
	public void setPinErrTimes(String pinErrTimes) {
		this.pinErrTimes = pinErrTimes;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getRechargeNum() {
		return rechargeNum;
	}
	public void setRechargeNum(String rechargeNum) {
		this.rechargeNum = rechargeNum;
	}
	public String getConsumNum() {
		return consumNum;
	}
	public void setConsumNum(String consumNum) {
		this.consumNum = consumNum;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getLockVersion() {
		return lockVersion;
	}
	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}
	public String getLockDate() {
		return lockDate;
	}
	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
	}
}