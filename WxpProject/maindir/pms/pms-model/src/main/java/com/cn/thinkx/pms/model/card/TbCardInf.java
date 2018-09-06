package com.cn.thinkx.pms.model.card;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_CARD_INF", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbCardInf {
	private String cardNo;
	private String productCode;
	private String cvvDate;
	private String validDate;
	private String svcCode;
	private String cvv2ErrTimes;
	private String cardStat;
	private String actDate;
	private String lockStat;
	private String cancelStat;
	private String lostStat;
	private String lockDate;
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
	private Set<TbRelCardAccount> cardAccountSet = new LinkedHashSet<TbRelCardAccount>();
	@Id
	@Column(name = "CARD_NO", unique = true, nullable = false, length = 19)
	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "PRODUCT_CODE", length = 8)
	public String getProductCode() {
		return this.productCode;
	}

	@Column(name = "STOCK_STAT", length = 2)
	public String getStockStat() {
		return stockStat;
	}

	public void setStockStat(String stockStat) {
		this.stockStat = stockStat;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Column(name = "CVV_DATE", length = 8)
	public String getCvvDate() {
		return this.cvvDate;
	}

	public void setCvvDate(String cvvDate) {
		this.cvvDate = cvvDate;
	}

	@Column(name = "VALID_DATE", length = 8)
	public String getValidDate() {
		return this.validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	@Column(name = "SVC_CODE", length = 3)
	public String getSvcCode() {
		return this.svcCode;
	}

	public void setSvcCode(String svcCode) {
		this.svcCode = svcCode;
	}

	@Column(name = "CVV2_ERR_TIMES", length = 2)
	public String getCvv2ErrTimes() {
		return this.cvv2ErrTimes;
	}

	public void setCvv2ErrTimes(String cvv2ErrTimes) {
		this.cvv2ErrTimes = cvv2ErrTimes;
	}

	@Column(name = "CARD_STAT", length = 2)
	public String getCardStat() {
		return this.cardStat;
	}

	public void setCardStat(String cardStat) {
		this.cardStat = cardStat;
	}

	@Column(name = "ACT_DATE", length = 8)
	public String getActDate() {
		return this.actDate;
	}

	public void setActDate(String actDate) {
		this.actDate = actDate;
	}

	@Column(name = "LOCK_STAT", length = 1)
	public String getLockStat() {
		return this.lockStat;
	}

	public void setLockStat(String lockStat) {
		this.lockStat = lockStat;
	}

	@Column(name = "CANCEL_STAT", length = 1)
	public String getCancelStat() {
		return this.cancelStat;
	}

	public void setCancelStat(String cancelStat) {
		this.cancelStat = cancelStat;
	}

	@Column(name = "LOST_STAT", length = 1)
	public String getLostStat() {
		return this.lostStat;
	}

	public void setLostStat(String lostStat) {
		this.lostStat = lostStat;
	}

	@Column(name = "FREEZE_STAT", length = 1)
	public String getFreezeStat() {
		return this.freezeStat;
	}

	public void setFreezeStat(String freezeStat) {
		this.freezeStat = freezeStat;
	}

	@Column(name = "TXN_PIN", length = 16)
	public String getTxnPin() {
		return this.txnPin;
	}

	public void setTxnPin(String txnPin) {
		this.txnPin = txnPin;
	}

	@Column(name = "PIN_STAT", length = 2)
	public String getPinStat() {
		return this.pinStat;
	}

	public void setPinStat(String pinStat) {
		this.pinStat = pinStat;
	}

	@Column(name = "NOPIN_TXN_AMT", length = 12)
	public String getNopintTxnAmt() {
		return this.nopintTxnAmt;
	}

	public void setNopintTxnAmt(String nopintTxnAmt) {
		this.nopintTxnAmt = nopintTxnAmt;
	}

	@Column(name = "PIN_ERR_TIMES", length = 2)
	public String getPinErrTimes() {
		return this.pinErrTimes;
	}

	public void setPinErrTimes(String pinErrTimes) {
		this.pinErrTimes = pinErrTimes;
	}

	@Column(name = "CREATE_DATE", length = 8)
	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "RECHARGE_NUM", length = 6)
	public String getRechargeNum() {
		return this.rechargeNum;
	}

	public void setRechargeNum(String rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	@Column(name = "CONSUM_NUM", length = 6)
	public String getConsumNum() {
		return this.consumNum;
	}

	public void setConsumNum(String consumNum) {
		this.consumNum = consumNum;
	}

	@Column(name = "REMARKS", length = 256)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "CREATE_USER", length = 8)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "UPDATE_USER", length = 8)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Version
	@Column(name = "LOCK_VERSION", precision = 8, scale = 0)
	public Integer getLockVersion() {
		return this.lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}
	 @OneToMany(mappedBy = "card")
	public Set<TbRelCardAccount> getCardAccountSet() {
		return cardAccountSet;
	}

	public void setCardAccountSet(Set<TbRelCardAccount> cardAccountSet) {
		this.cardAccountSet = cardAccountSet;
	}
	
	@Column(name = "LOCK_DATE", length = 8)
	public String getLockDate() {
		return lockDate;
	}

	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
	}

}