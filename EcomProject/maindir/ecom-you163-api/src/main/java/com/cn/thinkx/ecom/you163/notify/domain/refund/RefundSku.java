package com.cn.thinkx.ecom.you163.notify.domain.refund;

import java.math.BigDecimal;
import java.util.List;

import com.cn.thinkx.ecom.you163.api.vo.apply.ApplyPicVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplySkuReason;
import com.cn.thinkx.ecom.you163.api.vo.apply.OperateSkuVO;

/**
 * 申请售后的sku信息
 * @author zhuqiuyou
 *
 */
public class RefundSku implements java.io.Serializable{

	private static final long serialVersionUID = 1227562432717941147L;

	private String skuId;  //申请售后的skuId
	
	private Integer count;  //申请售后的sku数量
	
	private ApplySkuReason applySkuReason; //退换货原因
	
	private List<ApplyPicVO> applyPicList;
	
	private BigDecimal originalPrice;
	
	private BigDecimal subtotalPrice; //实付金额小计
	
	private List<OperateSkuVO> operateSkus;
	
	private RefundResultSkuAddress returnStoreHouse;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public ApplySkuReason getApplySkuReason() {
		return applySkuReason;
	}

	public void setApplySkuReason(ApplySkuReason applySkuReason) {
		this.applySkuReason = applySkuReason;
	}

	public List<ApplyPicVO> getApplyPicList() {
		return applyPicList;
	}

	public void setApplyPicList(List<ApplyPicVO> applyPicList) {
		this.applyPicList = applyPicList;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getSubtotalPrice() {
		return subtotalPrice;
	}

	public void setSubtotalPrice(BigDecimal subtotalPrice) {
		this.subtotalPrice = subtotalPrice;
	}

	public List<OperateSkuVO> getOperateSkus() {
		return operateSkus;
	}

	public void setOperateSkus(List<OperateSkuVO> operateSkus) {
		this.operateSkus = operateSkus;
	}

	public RefundResultSkuAddress getReturnStoreHouse() {
		return returnStoreHouse;
	}

	public void setReturnStoreHouse(RefundResultSkuAddress returnStoreHouse) {
		this.returnStoreHouse = returnStoreHouse;
	}
	
}
