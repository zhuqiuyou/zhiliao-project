package com.cn.thinkx.pms.service.shop;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.shop.ShopInf;

public interface ShopInfService {

	public List<ShopInf> dataGrid(ShopInf shop, PageFilter ph);

	public Long count(ShopInf shop, PageFilter ph);

	public void add(ShopInf shop);

	public void delete(String id);

	public void edit(ShopInf shop);

	public ShopInf get(String id);

	public ShopInf getShopInfByCode(String code);

	List<Tree> tree();

	public List<ShopInf> findShopList(ShopInf shopInfo);
	
	ShopInf getShopInfByMchntCode(String mchntCode);


}
