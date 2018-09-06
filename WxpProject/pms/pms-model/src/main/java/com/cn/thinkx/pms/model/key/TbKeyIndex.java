package com.cn.thinkx.pms.model.key;

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
@Table(name = "TB_KEY_INDEX", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbKeyIndex {

	// Fields

	private String keyId;
	private TbKeyVersion tbKeyVersion;
	private String keyIndex;
	private String keyName;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;

	// Constructors

	/** default constructor */
	public TbKeyIndex() {
	}

	/** minimal constructor */
	public TbKeyIndex(String keyId) {
		this.keyId = keyId;
	}
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 22)
	public String getKeyId() {
		return this.keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "version_id")
	public TbKeyVersion getTbKeyVersion() {
		return this.tbKeyVersion;
	}

	public void setTbKeyVersion(TbKeyVersion tbKeyVersion) {
		this.tbKeyVersion = tbKeyVersion;
	}

	@Column(name = "key_index", length = 3)
	public String getKeyIndex() {
		return this.keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}

	@Column(name = "key_name", length = 128)
	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
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

}