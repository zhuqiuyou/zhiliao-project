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
@Table(name = "TB_ACCOUNT_INF", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbAccountInf {
	private String accountNo;
	private String userId;
	private String personalId;
	private String accountType;
	private String accountStat;
	private String accBal;
	private String accBalCode;
	private String maxTxnAmt1;
	private String maxDayTxnAmt1;
	private String dayTotalAmt1;
	private String maxTxnAmt2;
	private String maxDayTxnAmt2;
	private String dayTotalAmt2;
	private String freezeAmt;
	private String lastTxnDate;
	private String lastTxnTime;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	private Set<TbRelCardAccount> cardAccountSet = new LinkedHashSet<TbRelCardAccount>();
	@Id
	@Column(name = "ACCOUNT_NO", unique = true, nullable = false, length = 20)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "USER_ID", length = 22)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "PERSONAL_ID", length = 22)
	public String getPersonalId() {
		return this.personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	@Column(name = "ACCOUNT_TYPE", length = 3)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "ACCOUNT_STAT", length = 2)
	public String getAccountStat() {
		return this.accountStat;
	}

	public void setAccountStat(String accountStat) {
		this.accountStat = accountStat;
	}

	@Column(name = "ACC_BAL", length = 12)
	public String getAccBal() {
		return this.accBal;
	}

	public void setAccBal(String accBal) {
		this.accBal = accBal;
	}

	@Column(name = "ACC_BAL_CODE", length = 16)
	public String getAccBalCode() {
		return this.accBalCode;
	}

	public void setAccBalCode(String accBalCode) {
		this.accBalCode = accBalCode;
	}

	@Column(name = "MAX_TXN_AMT1", length = 12)
	public String getMaxTxnAmt1() {
		return this.maxTxnAmt1;
	}

	public void setMaxTxnAmt1(String maxTxnAmt1) {
		this.maxTxnAmt1 = maxTxnAmt1;
	}

	@Column(name = "MAX_DAY_TXN_AMT1", length = 12)
	public String getMaxDayTxnAmt1() {
		return this.maxDayTxnAmt1;
	}

	public void setMaxDayTxnAmt1(String maxDayTxnAmt1) {
		this.maxDayTxnAmt1 = maxDayTxnAmt1;
	}

	@Column(name = "DAY_TOTAL_AMT1", length = 12)
	public String getDayTotalAmt1() {
		return this.dayTotalAmt1;
	}

	public void setDayTotalAmt1(String dayTotalAmt1) {
		this.dayTotalAmt1 = dayTotalAmt1;
	}

	@Column(name = "MAX_TXN_AMT2", length = 12)
	public String getMaxTxnAmt2() {
		return this.maxTxnAmt2;
	}

	public void setMaxTxnAmt2(String maxTxnAmt2) {
		this.maxTxnAmt2 = maxTxnAmt2;
	}

	@Column(name = "MAX_DAY_TXN_AMT2", length = 12)
	public String getMaxDayTxnAmt2() {
		return this.maxDayTxnAmt2;
	}

	public void setMaxDayTxnAmt2(String maxDayTxnAmt2) {
		this.maxDayTxnAmt2 = maxDayTxnAmt2;
	}

	@Column(name = "DAY_TOTAL_AMT2", length = 12)
	public String getDayTotalAmt2() {
		return this.dayTotalAmt2;
	}

	public void setDayTotalAmt2(String dayTotalAmt2) {
		this.dayTotalAmt2 = dayTotalAmt2;
	}

	@Column(name = "FREEZE_AMT", length = 12)
	public String getFreezeAmt() {
		return this.freezeAmt;
	}

	public void setFreezeAmt(String freezeAmt) {
		this.freezeAmt = freezeAmt;
	}

	@Column(name = "LAST_TXN_DATE", length = 8)
	public String getLastTxnDate() {
		return this.lastTxnDate;
	}

	public void setLastTxnDate(String lastTxnDate) {
		this.lastTxnDate = lastTxnDate;
	}

	@Column(name = "LAST_TXN_TIME", length = 6)
	public String getLastTxnTime() {
		return this.lastTxnTime;
	}

	public void setLastTxnTime(String lastTxnTime) {
		this.lastTxnTime = lastTxnTime;
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
	 @OneToMany(mappedBy = "account")
	public Set<TbRelCardAccount> getCardAccountSet() {
		return cardAccountSet;
	}

	public void setCardAccountSet(Set<TbRelCardAccount> cardAccountSet) {
		this.cardAccountSet = cardAccountSet;
	}
}