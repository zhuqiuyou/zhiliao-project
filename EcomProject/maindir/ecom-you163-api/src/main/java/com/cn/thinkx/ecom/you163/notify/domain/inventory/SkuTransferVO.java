package com.cn.thinkx.ecom.you163.notify.domain.inventory;

/**
 * 具体sku的划拨数据
 * 
 * @author kpplg
 *
 */
public class SkuTransferVO implements java.io.Serializable{
	
	private static final long serialVersionUID = -6836937041404134694L;

	private Long skuId;	//SKU ID
	
	private Integer transferCount;	//划拨数量  大于表示划拨，小于0表示回拨 

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getTransferCount() {
		return transferCount;
	}

	public void setTransferCount(Integer transferCount) {
		this.transferCount = transferCount;
	}
}
