package com.cn.thinkx.pms.model.trans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@MappedSuperclass
public class TbTransLog {
	private String txnPrimaryKey;
	private String settleDate;
	private String orgTxnPrimaryKey;
	private String dmsRelatedKey;
	private String orgDmsRelatedKey;
	private String transId;
	private Integer transSt;
	private String termCode;
	private String mchntCode;
	private String respCode;
	private String priAcctNo;
	private String cardNo;
	private String userName;
	private String productCode;
	private String transAmt;
	private String transCurrCd;
	private String cardAttr;
	private String transChnl;
	private String transFee;
	private String transFeeType;
	private String tfrInAcctNo;
	private String tfrOutAcctNo;
	private String additionalInfo;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;

	@Id
	@Column(name = "TXN_PRIMARY_KEY", unique = true, nullable = false, length = 22)
	public String getTxnPrimaryKey() {
		return this.txnPrimaryKey;
	}

	public void setTxnPrimaryKey(String txnPrimaryKey) {
		this.txnPrimaryKey = txnPrimaryKey;
	}
	@Column(name = "SETTLE_DATE", length = 8)
	public String getSettleDate() {
		return this.settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	@Column(name = "ORG_TXN_PRIMARY_KEY", length = 22)
	public String getOrgTxnPrimaryKey() {
		return this.orgTxnPrimaryKey;
	}

	public void setOrgTxnPrimaryKey(String orgTxnPrimaryKey) {
		this.orgTxnPrimaryKey = orgTxnPrimaryKey;
	}

	@Column(name = "DMS_RELATED_KEY", length = 40)
	public String getDmsRelatedKey() {
		return this.dmsRelatedKey;
	}

	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}

	@Column(name = "ORG_DMS_RELATED_KEY", length = 40)
	public String getOrgDmsRelatedKey() {
		return this.orgDmsRelatedKey;
	}

	public void setOrgDmsRelatedKey(String orgDmsRelatedKey) {
		this.orgDmsRelatedKey = orgDmsRelatedKey;
	}

	@Column(name = "TRANS_ID", length = 3)
	public String getTransId() {
		return this.transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	@Column(name = "TRANS_ST", precision = 8, scale = 0)
	public Integer getTransSt() {
		return this.transSt;
	}

	public void setTransSt(Integer transSt) {
		this.transSt = transSt;
	}

	@Column(name = "TERM_CODE", length = 8)
	public String getTermCode() {
		return this.termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	@Column(name = "MCHNT_CODE", length = 15)
	public String getMchntCode() {
		return this.mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	@Column(name = "RESP_CODE", length = 2)
	public String getRespCode() {
		return this.respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	@Column(name = "PRI_ACCT_NO", length = 20)
	public String getPriAcctNo() {
		return this.priAcctNo;
	}

	public void setPriAcctNo(String priAcctNo) {
		this.priAcctNo = priAcctNo;
	}

	@Column(name = "CARD_NO", length = 19)
	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "USER_NAME", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PRODUCT_CODE", length = 8)
	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Column(name = "TRANS_AMT", length = 12)
	public String getTransAmt() {
		return this.transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	@Column(name = "TRANS_CURR_CD", length = 3)
	public String getTransCurrCd() {
		return this.transCurrCd;
	}

	public void setTransCurrCd(String transCurrCd) {
		this.transCurrCd = transCurrCd;
	}

	@Column(name = "CARD_ATTR", length = 2)
	public String getCardAttr() {
		return this.cardAttr;
	}

	public void setCardAttr(String cardAttr) {
		this.cardAttr = cardAttr;
	}

	@Column(name = "TRANS_CHNL", length = 8)
	public String getTransChnl() {
		return this.transChnl;
	}

	public void setTransChnl(String transChnl) {
		this.transChnl = transChnl;
	}

	@Column(name = "TRANS_FEE", length = 12)
	public String getTransFee() {
		return this.transFee;
	}

	public void setTransFee(String transFee) {
		this.transFee = transFee;
	}

	@Column(name = "TRANS_FEE_TYPE", length = 2)
	public String getTransFeeType() {
		return this.transFeeType;
	}

	public void setTransFeeType(String transFeeType) {
		this.transFeeType = transFeeType;
	}

	@Column(name = "TFR_IN_ACCT_NO", length = 20)
	public String getTfrInAcctNo() {
		return this.tfrInAcctNo;
	}

	public void setTfrInAcctNo(String tfrInAcctNo) {
		this.tfrInAcctNo = tfrInAcctNo;
	}

	@Column(name = "TFR_OUT_ACCT_NO", length = 20)
	public String getTfrOutAcctNo() {
		return this.tfrOutAcctNo;
	}

	public void setTfrOutAcctNo(String tfrOutAcctNo) {
		this.tfrOutAcctNo = tfrOutAcctNo;
	}

	@Column(name = "ADDITIONAL_INFO", length = 256)
	public String getAdditionalInfo() {
		return this.additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
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
