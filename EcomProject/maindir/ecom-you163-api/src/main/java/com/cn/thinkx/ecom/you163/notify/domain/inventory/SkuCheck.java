package com.cn.thinkx.ecom.you163.notify.domain.inventory;

import java.util.List;
/**
 * 库存校准记录
 * 
 * @author kpplg
 *
 */
public class SkuCheck implements java.io.Serializable{
	
	private static final long serialVersionUID = -6836937041404134694L;

	private String id;	//唯一标识符
	
	private List<SkuCheckVO> skuChecks;	//Sku校准记录
	
	private Long operateTime;	//操作时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<SkuCheckVO> getSkuChecks() {
		return skuChecks;
	}
	public void setSkuChecks(List<SkuCheckVO> skuChecks) {
		this.skuChecks = skuChecks;
	}
	public Long getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}
	
}
