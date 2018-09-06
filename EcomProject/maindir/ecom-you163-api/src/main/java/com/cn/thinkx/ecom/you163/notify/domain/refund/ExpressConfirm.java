package com.cn.thinkx.ecom.you163.notify.domain.refund;

/**
 * 	退货包裹确认收货信息
 * @author zhuqiuyou
 *
 */
public class ExpressConfirm implements java.io.Serializable {

	private static final long serialVersionUID = -7734895837753984236L;
	
	private String applyId;  //申请单Id
	private String trackingCompany; //物流公司
	private String trackingNum;  //物流单号
	private Long trackingTime; //物流签收时间
	
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getTrackingCompany() {
		return trackingCompany;
	}
	public void setTrackingCompany(String trackingCompany) {
		this.trackingCompany = trackingCompany;
	}
	public String getTrackingNum() {
		return trackingNum;
	}
	public void setTrackingNum(String trackingNum) {
		this.trackingNum = trackingNum;
	}
	public Long getTrackingTime() {
		return trackingTime;
	}
	public void setTrackingTime(Long trackingTime) {
		this.trackingTime = trackingTime;
	}
}
