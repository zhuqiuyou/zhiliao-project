package com.cn.thinkx.pms.model.product;

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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.cn.thinkx.pms.model.key.TbKeyVersion;


/**
 * TbProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_PRODUCT")
public class TbProduct {


	private String productCode;
	private TbKeyVersion tbKeyVersion;
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

	// Property accessors
	@Id
	@Column(name = "PRODUCT_CODE", unique = true, nullable = false, length = 22)
	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	//@Column(name = "VERSION_ID", length = 22)
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERSION_ID")
	public TbKeyVersion getTbKeyVersion() {
		return this.tbKeyVersion;
	}

	public void setTbKeyVersion(TbKeyVersion tbKeyVersion) {
		this.tbKeyVersion = tbKeyVersion;
	}

	@Column(name = "CARD_BIN", length = 10)
	public String getCardBin() {
		return this.cardBin;
	}

	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
	}

	@Column(name = "PRODUCT_NAME", length = 128)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "ONYMOUS_STAT", length = 2)
	public String getOnymousStat() {
		return this.onymousStat;
	}

	public void setOnymousStat(String onymousStat) {
		this.onymousStat = onymousStat;
	}
	
	@Column(name = "BUSINESS_TYPE", length = 2)
	public String getBusinessType() {
		return this.businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	@Column(name = "PRODUCT_TYPE", length = 2)
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "VALIDITY_PERIOD", precision = 4, scale = 0)
	public Short getValidityPeriod() {
		return this.validityPeriod;
	}

	public void setValidityPeriod(Short validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	@Column(name = "MAX_BALANCE", length = 12)
	public String getMaxBalance() {
		return this.maxBalance;
	}

	public void setMaxBalance(String maxBalance) {
		this.maxBalance = maxBalance;
	}

	@Column(name = "CONSUM_TIMES", length = 6)
	public String getConsumTimes() {
		return this.consumTimes;
	}

	public void setConsumTimes(String consumTimes) {
		this.consumTimes = consumTimes;
	}

	@Column(name = "RECHARGE_TIMES", length = 6)
	public String getRechargeTimes() {
		return this.rechargeTimes;
	}

	public void setRechargeTimes(String rechargeTimes) {
		this.rechargeTimes = rechargeTimes;
	}

	@Column(name = "CVV2_ERR_TIMES", length = 2)
	public String getCvv2ErrTimes() {
		return this.cvv2ErrTimes;
	}

	public void setCvv2ErrTimes(String cvv2ErrTimes) {
		this.cvv2ErrTimes = cvv2ErrTimes;
	}

	@Column(name = "CARD_FACE", length = 256)
	public String getCardFace() {
		return this.cardFace;
	}

	public void setCardFace(String cardFace) {
		this.cardFace = cardFace;
	}

	@Column(name = "DATA_STAT", length = 1)
	public String getDataStat() {
		return this.dataStat;
	}

	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
	}

	@Column(name = "BAL_KEY_INDEX", length = 3)
	public String getBalKeyIndex() {
		return this.balKeyIndex;
	}

	public void setBalKeyIndex(String balKeyIndex) {
		this.balKeyIndex = balKeyIndex;
	}

	@Column(name = "CVV_KEY_INDEX", length = 3)
	public String getCvvKeyIndex() {
		return this.cvvKeyIndex;
	}

	public void setCvvKeyIndex(String cvvKeyIndex) {
		this.cvvKeyIndex = cvvKeyIndex;
	}

	@Column(name = "PWD_KEY_INDEX", length = 3)
	public String getPwdKeyIndex() {
		return this.pwdKeyIndex;
	}

	public void setPwdKeyIndex(String pwdKeyIndex) {
		this.pwdKeyIndex = pwdKeyIndex;
	}

	@Column(name = "LAST_CARD_NUM", precision = 8, scale = 0)
	public Integer getLastCardNum() {
		return this.lastCardNum;
	}

	public void setLastCardNum(Integer lastCardNum) {
		this.lastCardNum = lastCardNum;
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