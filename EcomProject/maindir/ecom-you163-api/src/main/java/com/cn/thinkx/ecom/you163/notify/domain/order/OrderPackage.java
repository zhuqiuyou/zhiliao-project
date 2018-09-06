package com.cn.thinkx.ecom.you163.notify.domain.order;

import java.util.List;

/**
 * 订单包裹物流绑单回调
 * 订单包裹信息
 * @author zhuqiuyou
 *
 */
public class OrderPackage implements java.io.Serializable {

	private static final long serialVersionUID = -6380615928892781956L;

	private String orderId; // 订单号 最大128位
	private Long packageId; // 包裹号
	private List<ExpressDetailInfo> expressDetailInfos; // 物流详细信息（带上sku）
	private Long expCreateTime; // 发货时间

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public List<ExpressDetailInfo> getExpressDetailInfos() {
		return expressDetailInfos;
	}

	public void setExpressDetailInfos(List<ExpressDetailInfo> expressDetailInfos) {
		this.expressDetailInfos = expressDetailInfos;
	}

	public Long getExpCreateTime() {
		return expCreateTime;
	}

	public void setExpCreateTime(Long expCreateTime) {
		this.expCreateTime = expCreateTime;
	}

}
