package com.cn.thinkx.ecom.you163.notify.domain.order;

import java.util.List;

/**
 * 订单包裹物流绑单回调
 * 物流详细信息
 * @author zhuqiuyou
 *
 */

public class ExpressDetailInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -6380615928892781956L;

	private String expressCompany;
	
	private String expressNo;
	
	private List<String> subExpressNos; //子物流单列表
	
	private List<Sku> skus;

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public List<String> getSubExpressNos() {
		return subExpressNos;
	}

	public void setSubExpressNos(List<String> subExpressNos) {
		this.subExpressNos = subExpressNos;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}
}
