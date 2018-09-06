package com.cn.thinkx.ecom.you163.api.vo.apply;

public class ApplyUserVO {

	/*
	 * 退货联系人，最大32位
	 */
	private String name;
	
	/*
	 * 退货联系人电话，最大32位
	 */
	private String mobile;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
