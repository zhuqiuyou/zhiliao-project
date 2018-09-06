package com.cn.thinkx.ecom.solr.api.model;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 商品搜索
 * 
 * @author zhuqiuyou
 *
 */
public class SolrGoods implements java.io.Serializable{


	private static final long serialVersionUID = -7485832354678681300L;
	
	@Field
	private String id;
	
	@Field
	private String goodsName;
	
	@Field
	private String spuCode;
	
	@Field
	private String ecomCode;
	
	@Field
	private String ecomName;
	
	@Field
	private String marketEnable;
	
	@Field
	private String pageTitle; //属性规格描述
	
	
	@Field
	private String picUrl;//图片
	
	@Field
	private double goodsPrice;//商品价格
	
	@Field
	private String catName; //类目名称
	
	@Field
	private String catId1; //一级分类
	
	@Field
	private String catId2; //二级分类
	
	@Field
	private String catId3; //三级分类
	
	@Field
	private Integer clickRate; //点击率
	
	@Field
	private Integer payRate;//购买率
	
	@Field
	private String keyword;

	public String getId() {
		return id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public String getEcomCode() {
		return ecomCode;
	}

	public String getEcomName() {
		return ecomName;
	}

	public String getMarketEnable() {
		return marketEnable;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public String getCatName() {
		return catName;
	}

	public String getCatId1() {
		return catId1;
	}

	public String getCatId2() {
		return catId2;
	}

	public String getCatId3() {
		return catId3;
	}

	public Integer getClickRate() {
		return clickRate;
	}

	public Integer getPayRate() {
		return payRate;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}

	public void setEcomName(String ecomName) {
		this.ecomName = ecomName;
	}

	public void setMarketEnable(String marketEnable) {
		this.marketEnable = marketEnable;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public void setCatId1(String catId1) {
		this.catId1 = catId1;
	}

	public void setCatId2(String catId2) {
		this.catId2 = catId2;
	}

	public void setCatId3(String catId3) {
		this.catId3 = catId3;
	}

	public void setClickRate(Integer clickRate) {
		this.clickRate = clickRate;
	}

	public void setPayRate(Integer payRate) {
		this.payRate = payRate;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


}
