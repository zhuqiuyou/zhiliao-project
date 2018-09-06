package com.cn.thinkx.wecard.middleware.resp.hkbtxnfacade.vo;

import com.cn.thinkx.wecard.middleware.resp.domain.entity.BaseDomain;

/**
 * 商户信息查询
 * @author zqy
 *
 */
public class MerchantInfoQueryITFVo  extends BaseDomain {

	private String mchntId;
	private String insId;
	private String mchntCode;
	private String mchntName;
	private String mchntType;
	private String dataStat;
	private String accountStat;
	private String soldCount;
	private String activeRule;
	private String phoneNumber; // 手机号码
	private String insCode; // 机构code
	private String brandLogo;// 商户LOGO
	public String getMchntId() {
		return mchntId;
	}
	public void setMchntId(String mchntId) {
		this.mchntId = mchntId;
	}
	public String getInsId() {
		return insId;
	}
	public void setInsId(String insId) {
		this.insId = insId;
	}
	public String getMchntCode() {
		return mchntCode;
	}
	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}
	public String getMchntName() {
		return mchntName;
	}
	public void setMchntName(String mchntName) {
		this.mchntName = mchntName;
	}
	public String getMchntType() {
		return mchntType;
	}
	public void setMchntType(String mchntType) {
		this.mchntType = mchntType;
	}
	public String getDataStat() {
		return dataStat;
	}
	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
	}
	public String getAccountStat() {
		return accountStat;
	}
	public void setAccountStat(String accountStat) {
		this.accountStat = accountStat;
	}
	public String getSoldCount() {
		return soldCount;
	}
	public void setSoldCount(String soldCount) {
		this.soldCount = soldCount;
	}
	public String getActiveRule() {
		return activeRule;
	}
	public void setActiveRule(String activeRule) {
		this.activeRule = activeRule;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getInsCode() {
		return insCode;
	}
	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}
	public String getBrandLogo() {
		return brandLogo;
	}
	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}
}
