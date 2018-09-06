package com.cn.thinkx.pms.pageModel.sys;


public class SysParameter {
	protected Long id;
	private Integer prmType;
	private Boolean prmStat;
	private String prmName;
	private String prmDesc;
	private String prmVal;
	private Integer prmLen;
	private Integer prmVersion;
	private String rsvd1;
	private String rsvd2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPrmType() {
		return prmType;
	}

	public void setPrmType(Integer prmType) {
		this.prmType = prmType;
	}

	public Boolean getPrmStat() {
		return prmStat;
	}

	public void setPrmStat(Boolean prmStat) {
		this.prmStat = prmStat;
	}

	public String getPrmName() {
		return prmName;
	}

	public void setPrmName(String prmName) {
		this.prmName = prmName;
	}

	public String getPrmDesc() {
		return prmDesc;
	}

	public void setPrmDesc(String prmDesc) {
		this.prmDesc = prmDesc;
	}

	public String getPrmVal() {
		return prmVal;
	}

	public void setPrmVal(String prmVal) {
		this.prmVal = prmVal;
	}

	public Integer getPrmLen() {
		return prmLen;
	}

	public void setPrmLen(Integer prmLen) {
		this.prmLen = prmLen;
	}

	public Integer getPrmVersion() {
		return prmVersion;
	}

	public void setPrmVersion(Integer prmVersion) {
		this.prmVersion = prmVersion;
	}

	public String getRsvd1() {
		return rsvd1;
	}

	public void setRsvd1(String rsvd1) {
		this.rsvd1 = rsvd1;
	}

	public String getRsvd2() {
		return rsvd2;
	}

	public void setRsvd2(String rsvd2) {
		this.rsvd2 = rsvd2;
	}

}