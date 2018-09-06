package com.cn.thinkx.pms.model.contract;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_MERCHANT_CONTRACT_LIST", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbMerchantContractList {

	private String id;
	private TbMerchantContract merchantContract;
	private String contractType;
	private String productCode;
	private Integer contractRate;
	private String dataStat;
	private String contractStartDate;
	private String contractEndDate;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;

	@Id
	@Column(name = "CONTRACT_DETAIL_ID", unique = true, nullable = false, length = 22)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "MCHNT_CONTRACT_ID")
	public TbMerchantContract getMerchantContract() {
		return merchantContract;
	}

	public void setMerchantContract(TbMerchantContract merchantContract) {
		this.merchantContract = merchantContract;
	}

	@Column(name = "CONTRACT_TYPE", length = 2)
	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	@Column(name = "PRODUCT_CODE", length = 8)
	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Column(name = "CONTRACT_RATE", precision = 8, scale = 0)
	public Integer getContractRate() {
		return this.contractRate;
	}

	public void setContractRate(Integer contractRate) {
		this.contractRate = contractRate;
	}

	@Column(name = "DATA_STAT", length = 1)
	public String getDataStat() {
		return this.dataStat;
	}

	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
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

}