package com.cn.thinkx.pms.model.card;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_REL_CARD_ACCOUNT", schema = "")
@IdClass(composeCardAccountIdPK.class)
public class TbRelCardAccount implements Serializable {

	private TbCardInf card;
	private TbAccountInf account;
	private String dataStat;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;

	@Id
	@JoinColumn(name = "CARD_NO")
	public TbCardInf getCard() {
		return card;
	}

	public void setCard(TbCardInf card) {
		this.card = card;
	}

	@Id
	@JoinColumn(name = "ACCOUNT_NO")
	public TbAccountInf getAccount() {
		return account;
	}

	public void setAccount(TbAccountInf account) {
		this.account = account;
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

}