package com.cn.thinkx.pms.model.order;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_ORDER" )
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbOrder {
	private String id;
	private String productCode;
	private String productName;
	private String orderType;
	private String orderDate;
	private String orderStat;
	private Integer cardNum;
	private String actStat;
	private String pinStat;
	private String nopintTxnAmt;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;
	private Set<TbOrderList> orderListSet = new HashSet<TbOrderList>();

	@Id
	@Column(name = "ORDER_ID", unique = true, nullable = false, length = 22)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="order" )
	public Set<TbOrderList> getOrderListSet() {
		return orderListSet;
	}

	public void setOrderListSet(Set<TbOrderList> orderListSet) {
		this.orderListSet = orderListSet;
	}

	@Column(name = "PRODUCT_CODE", length = 8)
	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Column(name = "PRODUCT_NAME", length = 128)
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "ORDER_TYPE", length = 6)
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Column(name = "ORDER_DATE", length = 8)
	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	@Column(name = "ORDER_STAT", length = 2)
	public String getOrderStat() {
		return this.orderStat;
	}

	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
	}

	@Column(name = "CARD_NUM", precision = 8, scale = 0)
	public Integer getCardNum() {
		return this.cardNum;
	}

	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}

	@Column(name = "ACT_STAT", length = 1)
	public String getActStat() {
		return this.actStat;
	}

	public void setActStat(String actStat) {
		this.actStat = actStat;
	}
	@Column(name = "PIN_STAT", length = 2)
	public String getPinStat() {
		return this.pinStat;
	}

	public void setPinStat(String pinStat) {
		this.pinStat = pinStat;
	}

	@Column(name = "NOPIN_TXN_AMT", length = 12)
	public String getNopintTxnAmt() {
		return this.nopintTxnAmt;
	}

	public void setNopintTxnAmt(String nopintTxnAmt) {
		this.nopintTxnAmt = nopintTxnAmt;
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
	@Column(name = "LOCK_VERSION", precision = 8, scale = 0)
	public Integer getLockVersion() {
		return this.lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}

}