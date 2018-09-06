package com.cn.thinkx.pms.pageModel.product;

import java.util.Date;

public class Product  {
	private String productCode;
	private String versionId;
	private String versionCode;
	private String cardBin;
	private String productName;
	private String onymousStat;
	private String businessType;
	private String productType;
	private Short validityPeriod;
	private String maxBalance;
	private String consumTimes;
	private String rechargeTimes;
	private String cvv2ErrTimes;
	private String cardFace;
	private String dataStat;
	private String balKeyIndex;
	private String cvvKeyIndex;
	private String pwdKeyIndex;
	private Integer lastCardNum;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	private Date createTimeStart;
	private Date createTimeEnd;

	public Date getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getCardBin() {
		return cardBin;
	}
	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOnymousStat() {
		return onymousStat;
	}
	public void setOnymousStat(String onymousStat) {
		this.onymousStat = onymousStat;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public Short getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(Short validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	public String getMaxBalance() {
		return maxBalance;
	}
	public void setMaxBalance(String maxBalance) {
		this.maxBalance = maxBalance;
	}
	public String getConsumTimes() {
		return consumTimes;
	}
	public void setConsumTimes(String consumTimes) {
		this.consumTimes = consumTimes;
	}
	public String getRechargeTimes() {
		return rechargeTimes;
	}
	public void setRechargeTimes(String rechargeTimes) {
		this.rechargeTimes = rechargeTimes;
	}
	public String getCvv2ErrTimes() {
		return cvv2ErrTimes;
	}
	public void setCvv2ErrTimes(String cvv2ErrTimes) {
		this.cvv2ErrTimes = cvv2ErrTimes;
	}
	public String getCardFace() {
		return cardFace;
	}
	public void setCardFace(String cardFace) {
		this.cardFace = cardFace;
	}
	public String getDataStat() {
		return dataStat;
	}
	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
	}
	public String getBalKeyIndex() {
		return balKeyIndex;
	}
	public void setBalKeyIndex(String balKeyIndex) {
		this.balKeyIndex = balKeyIndex;
	}
	public String getCvvKeyIndex() {
		return cvvKeyIndex;
	}
	public void setCvvKeyIndex(String cvvKeyIndex) {
		this.cvvKeyIndex = cvvKeyIndex;
	}
	public String getPwdKeyIndex() {
		return pwdKeyIndex;
	}
	public void setPwdKeyIndex(String pwdKeyIndex) {
		this.pwdKeyIndex = pwdKeyIndex;
	}
	public Integer getLastCardNum() {
		if (lastCardNum==null) {
			return 0;
		}
		return lastCardNum;
	}
	public void setLastCardNum(Integer lastCardNum) {
		this.lastCardNum = lastCardNum;
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
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

}