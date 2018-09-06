package com.cn.thinkx.ecom.you163.notify.domain.inventory;

import java.util.List;

/**
 * SKU库存划拨回调: 划拨记录
 * 
 * @author kpplg
 *
 */
public class SkuTransfer implements java.io.Serializable {

	private static final long serialVersionUID = -6836937041404134694L;

	private String id; // 划拨记录唯一标识符
	private List<SkuTransferVO> skuTransfers; // sku划拨记录
	private long operateTime; // 划拨时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<SkuTransferVO> getSkuTransfers() {
		return skuTransfers;
	}
	public void setSkuTransfers(List<SkuTransferVO> skuTransfers) {
		this.skuTransfers = skuTransfers;
	}
	public long getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(long operateTime) {
		this.operateTime = operateTime;
	}
	
}
