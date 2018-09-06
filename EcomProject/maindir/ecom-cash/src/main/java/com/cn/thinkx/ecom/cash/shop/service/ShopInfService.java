package com.cn.thinkx.ecom.cash.shop.service;

import com.cn.thinkx.ecom.cash.shop.domain.ShopInf;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface ShopInfService extends BaseService<ShopInf> {

	/**
	 * 根据门店号查询商户门店信息
	 * 
	 * @param shopCode
	 * @return
	 */
	ShopInf getShopInfByCode(String shopCode);
}
