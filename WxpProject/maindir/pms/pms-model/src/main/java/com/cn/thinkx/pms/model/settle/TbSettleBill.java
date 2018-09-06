package com.cn.thinkx.pms.model.settle;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.cn.thinkx.pms.model.shop.TbMerchantInf;

@Entity
@Table(name = "TB_SETTLE_BILL")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbSettleBill{

	private String settleId;
	private String settleDate;
	private String settleDateStart;
	private String settleDateEnd;
	private String contractId;
	private String settleType;
	private String settleSrc;
	private TbMerchantInf tbMerchantInf;
	private String transAtSum;
	private String settleFee;
	private String settleAtSum;
	private String feeOffset;
	private String billStat;
	private String remark;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;

	@Id
	@Column(name = "SETTLE_ID", unique = true, nullable = false, length = 22)
	public String getSettleId() {
		return this.settleId;
	}

	public void setSettleId(String settleId) {
		this.settleId = settleId;
	}

	@Column(name = "SETTLE_DATE", length = 8)
	public String getSettleDate() {
		return this.settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	@Column(name = "SETTLE_DATE_START", length = 8)
	public String getSettleDateStart() {
		return this.settleDateStart;
	}

	public void setSettleDateStart(String settleDateStart) {
		this.settleDateStart = settleDateStart;
	}

	@Column(name = "SETTLE_DATE_END", length = 8)
	public String getSettleDateEnd() {
		return this.settleDateEnd;
	}

	public void setSettleDateEnd(String settleDateEnd) {
		this.settleDateEnd = settleDateEnd;
	}

	@Column(name = "CONTRACT_ID", length = 8)
	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	@Column(name = "SETTLE_TYPE", length = 2)
	public String getSettleType() {
		return this.settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	@Column(name = "SETTLE_SRC", length = 22)
	public String getSettleSrc() {
		return this.settleSrc;
	}

	public void setSettleSrc(String settleSrc) {
		this.settleSrc = settleSrc;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SETTLE_DES")
	public TbMerchantInf getTbMerchantInf() {
		return this.tbMerchantInf;
	}

	public void setTbMerchantInf(TbMerchantInf tbMerchantInf) {
		this.tbMerchantInf = tbMerchantInf;
	}

	@Column(name = "TRANS_AT_SUM", length = 12)
	public String getTransAtSum() {
		return this.transAtSum;
	}

	public void setTransAtSum(String transAtSum) {
		this.transAtSum = transAtSum;
	}

	@Column(name = "SETTLE_FEE", length = 12)
	public String getSettleFee() {
		return this.settleFee;
	}

	public void setSettleFee(String settleFee) {
		this.settleFee = settleFee;
	}

	@Column(name = "SETTLE_AT_SUM", length = 12)
	public String getSettleAtSum() {
		return this.settleAtSum;
	}

	public void setSettleAtSum(String settleAtSum) {
		this.settleAtSum = settleAtSum;
	}

	@Column(name = "FEE_OFFSET", length = 12)
	public String getFeeOffset() {
		return this.feeOffset;
	}

	public void setFeeOffset(String feeOffset) {
		this.feeOffset = feeOffset;
	}

	@Column(name = "BILL_STAT", length = 2)
	public String getBillStat() {
		return this.billStat;
	}

	public void setBillStat(String billStat) {
		this.billStat = billStat;
	}

	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "LOCK_VERSION", precision = 8, scale = 0)
	public Integer getLockVersion() {
		return this.lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}

}