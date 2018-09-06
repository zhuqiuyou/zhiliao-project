package com.cn.thinkx.ecom.you163.notify.domain.inventory;
/**
 * Sku校准记录
 * 
 * @author kpplg
 *
 */
public class SkuCheckVO implements java.io.Serializable{

	private static final long serialVersionUID = -6836937041404134694L;
	
	private long skuId;	//Sku Id
	private int count;	//当前库存
	public long getSkuId() {
		return skuId;
	}
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
