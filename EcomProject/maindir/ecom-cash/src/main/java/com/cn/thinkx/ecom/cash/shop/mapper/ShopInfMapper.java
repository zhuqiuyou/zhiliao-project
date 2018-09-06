package com.cn.thinkx.ecom.cash.shop.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.cn.thinkx.ecom.cash.shop.domain.ShopInf;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface ShopInfMapper extends BaseDao<ShopInf> {

	/**
	 * 根据门店号查询商户门店信息
	 * 
	 * @param shopCode
	 * @return
	 */
	ShopInf getShopInfByCode(String shopCode);
}
