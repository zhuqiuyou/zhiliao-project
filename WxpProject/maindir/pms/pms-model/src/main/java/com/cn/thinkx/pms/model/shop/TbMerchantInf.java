package com.cn.thinkx.pms.model.shop;

import java.util.Date;
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
@Table(name = "TB_MERCHANT_INF")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbMerchantInf {

	private String mchntId;
	private String mchntCode;
	private String mchntName;
	private String mchntType;
	private String dataStat;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	private Set<TbShopInf> shopInfSet;
	@Id
	@Column(name = "MCHNT_ID", unique = true, nullable = false, length = 22)
	public String getMchntId() {
		return this.mchntId;
	}

	public void setMchntId(String mchntId) {
		this.mchntId = mchntId;
	}

	@Column(name = "MCHNT_CODE", length = 15)
	public String getMchntCode() {
		return this.mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	@Column(name = "MCHNT_NAME", length = 128)
	public String getMchntName() {
		return this.mchntName;
	}

	public void setMchntName(String mchntName) {
		this.mchntName = mchntName;
	}

	@Column(name = "MCHNT_TYPE", length = 2)
	public String getMchntType() {
		return this.mchntType;
	}

	public void setMchntType(String mchntType) {
		this.mchntType = mchntType;
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

	@OneToMany(mappedBy="merchantInf")
	public Set<TbShopInf> getShopInfSet() {
		return shopInfSet;
	}

	public void setShopInfSet(Set<TbShopInf> shopInfSet) {
		this.shopInfSet = shopInfSet;
	}

}