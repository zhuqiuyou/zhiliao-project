package com.cn.thinkx.pms.model.sys;

import java.io.Serializable;

import javax.persistence.Column;

public class composeIdAndVersion implements Serializable {

	protected Long id;
	private Integer prmType;
	private Integer prmVersion;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof composeIdAndVersion) {
			composeIdAndVersion pk = (composeIdAndVersion) obj;
			if (this.id.equals(pk.id) && this.prmVersion.equals(pk.prmVersion) && this.prmType.equals(pk.prmType)) {
				return true;
			}
		}
		return false;
	}

	@Column(name = "PRM_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PRM_TYPE", nullable = false, precision = 5, scale = 0)
	public Integer getPrmType() {
		return this.prmType;
	}

	public void setPrmType(Integer prmType) {
		this.prmType = prmType;
	}

	@Column(name = "PRM_VERSION")
	public Integer getPrmVersion() {
		return prmVersion;
	}

	public void setPrmVersion(Integer prmVersion) {
		this.prmVersion = prmVersion;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
