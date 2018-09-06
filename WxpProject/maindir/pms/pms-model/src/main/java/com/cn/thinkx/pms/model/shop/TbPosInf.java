package com.cn.thinkx.pms.model.shop;

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

import com.cn.thinkx.pms.model.key.TbKeyVersion;

@Entity
@Table(name = "TB_POS_INF")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TbPosInf {

	private String id;
	private String termId;
	private String mchntCode;
	private TbShopInf tbShopInf;
	private String shopCode;
	private String mchntName;
	private String termSigninStat;
	private String termPik;
	private String termMak;
	private String termTmk;
	private Integer termBatchNo;
	private String termTransFlag;
	private Integer prmType1;
	private Integer prmVersion1;
	private Boolean prmChangeFlag1;
	private Integer prmType2;
	private Integer prmVersion2;
	private Boolean prmChangeFlag2;
	private Integer prmType3;
	private Integer prmVersion3;
	private Boolean prmChangeFlag3;
	private String termModel;
	private String consumePerFlag;
	private String reloadPerFlag;
	private String reprintCtrlFlag;
	private Long currTxnCnt;
	private Long maxTxnCnt;
	private String tmkDownTime;
	private String lastTxnTime;
	private String termStat;
	private String isValidateMac;
	private TbKeyVersion tbKeyVersion;
	private String posIndex;
	private String tmkIndex;
	private String pikIndex;
	private String makIndex;
	private String remarks;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	private Integer lockVersion;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 22)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TERM_ID", nullable = false, length = 8)
	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	@Column(name = "MCHNT_CODE", nullable = false, length = 15)
	public String getMchntCode() {
		return this.mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHOP_ID")
	public TbShopInf getTbShopInf() {
		return tbShopInf;
	}

	public void setTbShopInf(TbShopInf tbShopInf) {
		this.tbShopInf = tbShopInf;
	}

	@Column(name = "SHOP_CODE", nullable = false, length = 8)
	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	@Column(name = "MCHNT_NAME", nullable = false, length = 128)
	public String getMchntName() {
		return this.mchntName;
	}

	public void setMchntName(String mchntName) {
		this.mchntName = mchntName;
	}

	@Column(name = "TERM_SIGNIN_STAT", length = 1)
	public String getTermSigninStat() {
		return this.termSigninStat;
	}

	public void setTermSigninStat(String termSigninStat) {
		this.termSigninStat = termSigninStat;
	}

	@Column(name = "TERM_PIK", length = 32)
	public String getTermPik() {
		return this.termPik;
	}

	public void setTermPik(String termPik) {
		this.termPik = termPik;
	}

	@Column(name = "TERM_MAK", length = 32)
	public String getTermMak() {
		return this.termMak;
	}

	public void setTermMak(String termMak) {
		this.termMak = termMak;
	}

	@Column(name = "TERM_TMK", length = 32)
	public String getTermTmk() {
		return this.termTmk;
	}

	public void setTermTmk(String termTmk) {
		this.termTmk = termTmk;
	}

	@Column(name = "TERM_BATCH_NO", precision = 6, scale = 0)
	public Integer getTermBatchNo() {
		return this.termBatchNo;
	}

	public void setTermBatchNo(Integer termBatchNo) {
		this.termBatchNo = termBatchNo;
	}

	@Column(name = "TERM_TRANS_FLAG", nullable = false, length = 1)
	public String getTermTransFlag() {
		return this.termTransFlag;
	}

	public void setTermTransFlag(String termTransFlag) {
		this.termTransFlag = termTransFlag;
	}

	@Column(name = "PRM_TYPE1", precision = 5, scale = 0)
	public Integer getPrmType1() {
		return this.prmType1;
	}

	public void setPrmType1(Integer prmType1) {
		this.prmType1 = prmType1;
	}

	@Column(name = "PRM_VERSION1", precision = 5, scale = 0)
	public Integer getPrmVersion1() {
		return this.prmVersion1;
	}

	public void setPrmVersion1(Integer prmVersion1) {
		this.prmVersion1 = prmVersion1;
	}

	@Column(name = "PRM_CHANGE_FLAG1", precision = 1, scale = 0)
	public Boolean getPrmChangeFlag1() {
		return this.prmChangeFlag1;
	}

	public void setPrmChangeFlag1(Boolean prmChangeFlag1) {
		this.prmChangeFlag1 = prmChangeFlag1;
	}

	@Column(name = "PRM_TYPE2", precision = 5, scale = 0)
	public Integer getPrmType2() {
		return this.prmType2;
	}

	public void setPrmType2(Integer prmType2) {
		this.prmType2 = prmType2;
	}

	@Column(name = "PRM_VERSION2", precision = 5, scale = 0)
	public Integer getPrmVersion2() {
		return this.prmVersion2;
	}

	public void setPrmVersion2(Integer prmVersion2) {
		this.prmVersion2 = prmVersion2;
	}

	@Column(name = "PRM_CHANGE_FLAG2", precision = 1, scale = 0)
	public Boolean getPrmChangeFlag2() {
		return this.prmChangeFlag2;
	}

	public void setPrmChangeFlag2(Boolean prmChangeFlag2) {
		this.prmChangeFlag2 = prmChangeFlag2;
	}

	@Column(name = "PRM_TYPE3", precision = 5, scale = 0)
	public Integer getPrmType3() {
		return this.prmType3;
	}

	public void setPrmType3(Integer prmType3) {
		this.prmType3 = prmType3;
	}

	@Column(name = "PRM_VERSION3", precision = 5, scale = 0)
	public Integer getPrmVersion3() {
		return this.prmVersion3;
	}

	public void setPrmVersion3(Integer prmVersion3) {
		this.prmVersion3 = prmVersion3;
	}

	@Column(name = "PRM_CHANGE_FLAG3", precision = 1, scale = 0)
	public Boolean getPrmChangeFlag3() {
		return this.prmChangeFlag3;
	}

	public void setPrmChangeFlag3(Boolean prmChangeFlag3) {
		this.prmChangeFlag3 = prmChangeFlag3;
	}

	@Column(name = "TERM_MODEL", nullable = false, length = 64)
	public String getTermModel() {
		return this.termModel;
	}

	public void setTermModel(String termModel) {
		this.termModel = termModel;
	}

	@Column(name = "CONSUME_PER_FLAG", nullable = false, length = 1)
	public String getConsumePerFlag() {
		return this.consumePerFlag;
	}

	public void setConsumePerFlag(String consumePerFlag) {
		this.consumePerFlag = consumePerFlag;
	}

	@Column(name = "RELOAD_PER_FLAG", nullable = false, length = 1)
	public String getReloadPerFlag() {
		return this.reloadPerFlag;
	}

	public void setReloadPerFlag(String reloadPerFlag) {
		this.reloadPerFlag = reloadPerFlag;
	}

	@Column(name = "REPRINT_CTRL_FLAG", nullable = false, length = 1)
	public String getReprintCtrlFlag() {
		return this.reprintCtrlFlag;
	}

	public void setReprintCtrlFlag(String reprintCtrlFlag) {
		this.reprintCtrlFlag = reprintCtrlFlag;
	}

	@Column(name = "CURR_TXN_CNT", precision = 10, scale = 0)
	public Long getCurrTxnCnt() {
		return this.currTxnCnt;
	}

	public void setCurrTxnCnt(Long currTxnCnt) {
		this.currTxnCnt = currTxnCnt;
	}

	@Column(name = "MAX_TXN_CNT", nullable = false, precision = 10, scale = 0)
	public Long getMaxTxnCnt() {
		return this.maxTxnCnt;
	}

	public void setMaxTxnCnt(Long maxTxnCnt) {
		this.maxTxnCnt = maxTxnCnt;
	}

	@Column(name = "TMK_DOWN_TIME", length = 14)
	public String getTmkDownTime() {
		return this.tmkDownTime;
	}

	public void setTmkDownTime(String tmkDownTime) {
		this.tmkDownTime = tmkDownTime;
	}

	@Column(name = "LAST_TXN_TIME", length = 14)
	public String getLastTxnTime() {
		return this.lastTxnTime;
	}

	public void setLastTxnTime(String lastTxnTime) {
		this.lastTxnTime = lastTxnTime;
	}

	@Column(name = "TERM_STAT", nullable = false, length = 1)
	public String getTermStat() {
		return this.termStat;
	}

	public void setTermStat(String termStat) {
		this.termStat = termStat;
	}

	@Column(name = "IS_VALIDATE_MAC", nullable = false, length = 1)
	public String getIsValidateMac() {
		return this.isValidateMac;
	}

	public void setIsValidateMac(String isValidateMac) {
		this.isValidateMac = isValidateMac;
	}

	@Column(name = "POS_INDEX", nullable = false, length = 3)
	public String getPosIndex() {
		return this.posIndex;
	}

	public void setPosIndex(String posIndex) {
		this.posIndex = posIndex;
	}

	@Column(name = "TMK_INDEX", nullable = false, length = 3)
	public String getTmkIndex() {
		return this.tmkIndex;
	}

	public void setTmkIndex(String tmkIndex) {
		this.tmkIndex = tmkIndex;
	}

	@Column(name = "PIK_INDEX", nullable = false, length = 3)
	public String getPikIndex() {
		return this.pikIndex;
	}

	public void setPikIndex(String pikIndex) {
		this.pikIndex = pikIndex;
	}

	@Column(name = "MAK_INDEX", nullable = false, length = 3)
	public String getMakIndex() {
		return this.makIndex;
	}

	public void setMakIndex(String makIndex) {
		this.makIndex = makIndex;
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
	@Column(name = "CREATE_TIME", nullable = false, length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", nullable = false, length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERSION_ID")
	public TbKeyVersion getTbKeyVersion() {
		return tbKeyVersion;
	}

	public void setTbKeyVersion(TbKeyVersion tbKeyVersion) {
		this.tbKeyVersion = tbKeyVersion;
	}
	@Version
	@Column(name = "lock_version")
	public Integer getLockVersion() {
		return this.lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}
}