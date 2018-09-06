package com.cn.thinkx.pms.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_SYS_PARAMETER", schema = "")
@SequenceGenerator(name = "sysParameterSeq", sequenceName = "tb_sys_parameter_seq")
@IdClass(composeIdAndVersion.class)
public class TbSysParameter {

	private Integer prmType;
	private Integer prmVersion;
	private Boolean prmStat;
	private String prmName;
	private String prmDesc;
	private String prmVal;
	private Integer prmLen;
	private String rsvd1;
	private String rsvd2;
	protected Long id;

	@Id
	@Column(name = "PRM_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@Column(name = "PRM_TYPE", nullable = false, precision = 5, scale = 0)
	public Integer getPrmType() {
		return this.prmType;
	}

	public void setPrmType(Integer prmType) {
		this.prmType = prmType;
	}

	@Column(name = "PRM_STAT", precision = 1, scale = 0)
	public Boolean getPrmStat() {
		return this.prmStat;
	}

	public void setPrmStat(Boolean prmStat) {
		this.prmStat = prmStat;
	}

	@Column(name = "PRM_NAME", length = 32)
	public String getPrmName() {
		return this.prmName;
	}

	public void setPrmName(String prmName) {
		this.prmName = prmName;
	}

	@Column(name = "PRM_DESC", length = 64)
	public String getPrmDesc() {
		return this.prmDesc;
	}

	public void setPrmDesc(String prmDesc) {
		this.prmDesc = prmDesc;
	}

	@Column(name = "PRM_VAL", length = 64)
	public String getPrmVal() {
		return this.prmVal;
	}

	public void setPrmVal(String prmVal) {
		this.prmVal = prmVal;
	}

	@Column(name = "PRM_LEN", precision = 2, scale = 0)
	public Integer getPrmLen() {
		return this.prmLen;
	}

	public void setPrmLen(Integer prmLen) {
		this.prmLen = prmLen;
	}
	
	@Id
	@Column(name = "PRM_VERSION")
	public Integer getPrmVersion() {
		return this.prmVersion;
	}

	public void setPrmVersion(Integer prmVersion) {
		this.prmVersion = prmVersion;
	}

	@Column(name = "RSVD1", length = 64)
	public String getRsvd1() {
		return this.rsvd1;
	}

	public void setRsvd1(String rsvd1) {
		this.rsvd1 = rsvd1;
	}

	@Column(name = "RSVD2", length = 128)
	public String getRsvd2() {
		return this.rsvd2;
	}

	public void setRsvd2(String rsvd2) {
		this.rsvd2 = rsvd2;
	}

}