package com.cn.thinkx.ecom.solr.api.model;

public class GoodsQueryParam implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4586384253461633553L;
	

	private String keyword; //关键字搜索
	
	private String ecomCode;
	
	private String psort; //1:综合 ,2：价格，3：购买，4：最新
	
	private String orderType; //排序类型
	
	private Integer starts;
	
	private Integer rows;
	
	private String catId1; //一级分类
	
	private String catId2; //二级分类
	
	private String catId3; //三级分类


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getEcomCode() {
		return ecomCode;
	}

	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}

	public String getPsort() {
		return psort;
	}

	public void setPsort(String psort) {
		this.psort = psort;
	}

	public Integer getStarts() {
		return starts;
	}

	public void setStarts(Integer starts) {
		this.starts = starts;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getCatId1() {
		return catId1;
	}

	public void setCatId1(String catId1) {
		this.catId1 = catId1;
	}

	public String getCatId2() {
		return catId2;
	}

	public void setCatId2(String catId2) {
		this.catId2 = catId2;
	}

	public String getCatId3() {
		return catId3;
	}

	public void setCatId3(String catId3) {
		this.catId3 = catId3;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
}
