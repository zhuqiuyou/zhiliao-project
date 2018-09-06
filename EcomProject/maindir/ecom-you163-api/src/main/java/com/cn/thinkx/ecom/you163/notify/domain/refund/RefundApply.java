package com.cn.thinkx.ecom.you163.notify.domain.refund;

import java.util.List;

import com.cn.thinkx.ecom.you163.api.vo.ExpressInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplySkuVO;

/**
 * 售后申请返回信息
 * @author zhuqiuyou
 *
 */
public class RefundApply implements java.io.Serializable {

	private static final long serialVersionUID = 2400039963692495786L;

	private String applyId;
	
	private String orderId;
	
	private List<ApplySkuVO> applySkuList;
	
	private Integer returnType;
	
	private Integer status;
	
	private List<ExpressInfoVO> expressInfoList;
	
	private String denyReason;
	
	private Long createTime;
	
	private Long updateTime;

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<ApplySkuVO> getApplySkuList() {
		return applySkuList;
	}

	public void setApplySkuList(List<ApplySkuVO> applySkuList) {
		this.applySkuList = applySkuList;
	}

	public Integer getReturnType() {
		return returnType;
	}

	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<ExpressInfoVO> getExpressInfoList() {
		return expressInfoList;
	}

	public void setExpressInfoList(List<ExpressInfoVO> expressInfoList) {
		this.expressInfoList = expressInfoList;
	}

	public String getDenyReason() {
		return denyReason;
	}

	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
