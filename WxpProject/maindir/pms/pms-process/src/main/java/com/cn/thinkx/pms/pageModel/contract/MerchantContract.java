package com.cn.thinkx.pms.pageModel.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MerchantContract {

	private String id;
	private String merchantInfId;
	private String mchntCode;
	private String mchntName;
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

	private String merchantContractListStr;

	private List<MerchantContractList> merchantContractListSet = new ArrayList<MerchantContractList>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerchantInfId() {
		return merchantInfId;
	}

	public void setMerchantInfId(String merchantInfId) {
		this.merchantInfId = merchantInfId;
	}

	public String getMchntCode() {
		return mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public String getSettleCycle() {
		return settleCycle;
	}

	public void setSettleCycle(String settleCycle) {
		this.settleCycle = settleCycle;
	}

	public String getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getPreSettleDate() {
		return preSettleDate;
	}

	public void setPreSettleDate(String preSettleDate) {
		this.preSettleDate = preSettleDate;
	}

	public String getDataStat() {
		return dataStat;
	}

	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
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

	public String getMerchantContractListStr() {
		return merchantContractListStr;
	}

	public void setMerchantContractListStr(String merchantContractListStr) {
		this.merchantContractListStr = merchantContractListStr;
	}

	public List<MerchantContractList> getMerchantContractListSet() {
		return merchantContractListSet;
	}

	public void setMerchantContractListSet(List<MerchantContractList> merchantContractListSet) {
		this.merchantContractListSet = merchantContractListSet;
	}

	public String getMchntName() {
		return mchntName;
	}

	public void setMchntName(String mchntName) {
		this.mchntName = mchntName;
	}

}