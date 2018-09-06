package com.cn.thinkx.pms.model.shop;

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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_SHOP_INF")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbShopInf {
	private String shopId;
	private String shopCode;
	private TbMerchantInf merchantInf;
	private String shopName;
	private String dataStat;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	@Id
	@Column(name = "SHOP_ID", unique = true, nullable = false, length = 22)
	public String getShopId() {
		return this.shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	@Column(name = "SHOP_CODE", length = 8)
	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	

	@Column(name = "SHOP_NAME", length = 128)
	public String getShopName() {
		return this.shopName;
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

	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	@Column(name="lock_version")
	public Integer getLockVersion() {
		return this.lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}

}