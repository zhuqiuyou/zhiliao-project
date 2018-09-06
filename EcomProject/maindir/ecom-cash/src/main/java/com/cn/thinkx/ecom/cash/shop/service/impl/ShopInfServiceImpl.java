package com.cn.thinkx.ecom.cash.shop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.cash.shop.domain.ShopInf;
import com.cn.thinkx.ecom.cash.shop.mapper.ShopInfMapper;
import com.cn.thinkx.ecom.cash.shop.service.ShopInfService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("shopInfService")
public class ShopInfServiceImpl extends BaseServiceImpl<ShopInf> implements ShopInfService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ShopInfMapper shopInfMapper;

	@Override
	public ShopInf getShopInfByCode(String shopCode) {
		return shopInfMapper.getShopInfByCode(shopCode);
	}

}
