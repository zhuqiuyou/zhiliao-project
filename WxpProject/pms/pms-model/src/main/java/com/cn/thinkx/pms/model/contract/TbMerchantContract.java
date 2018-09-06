package com.cn.thinkx.pms.model.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.cn.thinkx.pms.model.shop.TbMerchantInf;

@Entity
@Table(name = "TB_MERCHANT_CONTRACT", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbMerchantContract {

	private String id;
	private TbMerchantInf merchantInf;
	private String mchntCode;
	private String settleType;
	private String settleCycle;
	private String contractStartDate;
	private String contractEndDate;
	private String preSettleDate;
	private String dataStat;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;

	private List<TbMerchantContractList> merchantContractLists = new ArrayList<TbMerchantContractList>();

	@Id
	@Column(name = "MCHNT_CONTRACT_ID", unique = true, nullable = false, length = 8)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MCHNT_ID")
	public TbMerchantInf getMerchantInf() {
		return merchantInf;
	}

	public void setMerchantInf(TbMerchantInf merchantInf) {
		this.merchantInf = merchantInf;
	}

	@Column(name = "MCHNT_CODE", length = 15)
	public String getMchntCode() {
		return this.mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	@Column(name = "SETTLE_TYPE", length = 2)
	public String getSettleType() {
		return this.settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	@Column(name = "SETTLE_CYCLE", length = 4)
	public String getSettleCycle() {
		return this.settleCycle;
	}

	public void setSettleCycle(String settleCycle) {
		this.settleCycle = settleCycle;
	}

	@Column(name = "CONTRACT_START_DATE", length = 8)
	public String getContractStartDate() {
		return this.contractStartDate;
	}

	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	@Column(name = "CONTRACT_END_DATE", length = 8)
	public String getContractEndDate() {
		return this.contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	@Column(name = "PRE_SETTLE_DATE", length = 8)
	public String getPreSettleDate() {
		return this.preSettleDate;
	}

	public void setPreSettleDate(String preSettleDate) {
		this.preSettleDate = preSettleDate;
	}

	@Column(name = "DATA_STAT", length = 1)
	public String getDataStat() {
		return this.dataStat;
	}

	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
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
	@Column(name = "lock_version")
	public Integer getLockVersion() {
		return this.lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "merchantContract")
	public List<TbMerchantContractList> getMerchantContractLists() {
		return merchantContractLists;
	}

	public void setMerchantContractLists(List<TbMerchantContractList> merchantContractLists) {
		this.merchantContractLists = merchantContractLists;
	}

}