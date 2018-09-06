package com.cn.thinkx.pms.pageModel.card;


public class CardSearch {
	private String startCardNum;
	private String endCardNum;
	private String productSearchCode;
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStartCardNum() {
		return startCardNum;
	}

	public void setStartCardNum(String startCardNum) {
		this.startCardNum = startCardNum;
	}

	public String getEndCardNum() {
		return endCardNum;
	}

	public void setEndCardNum(String endCardNum) {
		this.endCardNum = endCardNum;
	}

	public String getProductSearchCode() {
		return productSearchCode;
	}

	public void setProductSearchCode(String productSearchCode) {
		this.productSearchCode = productSearchCode;
	}

}