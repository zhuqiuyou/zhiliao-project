package com.cn.thinkx.pms.model.key;

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

/**
 * TbKeyVersiong entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_KEY_VERSIONG", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbKeyVersion{
	private String versionId;
	private String versionCode;
	private String versionType;
	private String dftStat;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	private Set<TbKeyIndex> keyIndexSet;

	@Id
	@Column(name = "version_id", unique = true, nullable = false, length = 22)
	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	@Column(name = "version_code", length = 2)
	public String getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	@Column(name = "version_type", length = 2)
	public String getVersionType() {
		return this.versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	@Column(name = "dft_stat", length = 1)
	public String getDftStat() {
		return this.dftStat;
	}

	public void setDftStat(String dftStat) {
		this.dftStat = dftStat;
	}

	@Column(name = "remarks", length = 256)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "create_user", length = 8)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "update_user", length = 8)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", length = 7)
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
	 @OneToMany(mappedBy = "tbKeyVersion")
	public Set<TbKeyIndex> getKeyIndexSet() {
		return keyIndexSet;
	}

	public void setKeyIndexSet(Set<TbKeyIndex> keyIndexSet) {
		this.keyIndexSet = keyIndexSet;
	}
}