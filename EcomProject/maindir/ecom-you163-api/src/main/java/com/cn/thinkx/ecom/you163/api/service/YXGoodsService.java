package com.cn.thinkx.ecom.you163.api.service;

import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemTO;

public interface YXGoodsService {
	
	/**
	 * 插入商品信息数据
	 * @param item
	 * @return
	 */
	String insertGoods(APIItemTO item);
}
