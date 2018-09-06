package com.cn.thinkx.pms.pageModel.trans;

import java.util.Date;

public class TransInf  {

	private String transId;
	private String transName;
	private Integer transGroup;
	private String transCfg;
	private String accSvc;
	private String transDsp;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getTransCfg() {
		return transCfg;
	}
	public void setTransCfg(String transCfg) {
		this.transCfg = transCfg;
	}
	public Integer getTransGroup() {
		return transGroup;
	}
	public void setTransGroup(Integer transGroup) {
		this.transGroup = transGroup;
	}
	public String getAccSvc() {
		return accSvc;
	}
	public void setAccSvc(String accSvc) {
		this.accSvc = accSvc;
	}
	public String getTransDsp() {
		return transDsp;
	}
	public void setTransDsp(String transDsp) {
		this.transDsp = transDsp;
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
}