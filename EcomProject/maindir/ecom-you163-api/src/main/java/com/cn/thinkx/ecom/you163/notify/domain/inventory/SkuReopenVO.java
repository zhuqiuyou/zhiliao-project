package com.cn.thinkx.ecom.you163.notify.domain.inventory;
/**
 * 再次开售信息
 * 
 * @author kpplg
 *
 */
public class SkuReopenVO implements java.io.Serializable{
	
	private static final long serialVersionUID = -6836937041404134694L;

	private Long skuId;	//Sku Id
	
	private Integer inventory;	//当前剩余库存

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
}
