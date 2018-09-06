package com.cn.thinkx.pms.model.trans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_TRANS_INF")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbTransInf  {

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

	@Id
	@Column(name = "TRANS_ID", unique = true, nullable = false, length = 3)
	public String getTransId() {
		return this.transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	@Column(name = "TRANS_NAME", length = 64)
	public String getTransName() {
		return this.transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	@Column(name = "TRANS_GROUP", precision = 2, scale = 0)
	public Integer getTransGroup() {
		return this.transGroup;
	}

	public void setTransGroup(Integer transGroup) {
		this.transGroup = transGroup;
	}

	@Column(name = "TRANS_CFG", length = 40)
	public String getTransCfg() {
		return this.transCfg;
	}

	public void setTransCfg(String transCfg) {
		this.transCfg = transCfg;
	}

	@Column(name = "ACC_SVC", length = 20)
	public String getAccSvc() {
		return this.accSvc;
	}

	public void setAccSvc(String accSvc) {
		this.accSvc = accSvc;
	}

	@Column(name = "TRANS_DSP", length = 128)
	public String getTransDsp() {
		return this.transDsp;
	}

	public void setTransDsp(String transDsp) {
		this.transDsp = transDsp;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}