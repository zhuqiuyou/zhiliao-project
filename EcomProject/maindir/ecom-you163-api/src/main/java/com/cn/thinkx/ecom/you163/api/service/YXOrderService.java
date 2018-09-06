package com.cn.thinkx.ecom.you163.api.service;

import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderOutVO;

public interface YXOrderService {
	
	/**
	 * 创建网易优选订单
	 * @param platfShopOrder 商城二级订单
	 * @return
	 */
	JsonResult doCreateOrder(PlatfShopOrder platfShopOrder) throws Exception;

	/**
	 * 订单取消回调
	 * 
	 * @param orderId
	 * @return
	 */
	JsonResult cancelOrder(String orderCancelResult);
	
	/**
	 * 订单包裹物流绑单回调
	 * 
	 * @param orderPackage
	 * @return
	 */
	JsonResult deliveredOrder(String orderPackage);
	
	/**
	 * 订单异常回调
	 * 
	 * @param exceptionInfo
	 * @return
	 */
	JsonResult exceptionalOrder(String exceptionInfo);
	
	/**
	 * 严选创建订单 
	 * 保存包裹信息
	 * @param orderOut
	 */
	void doOrderPackageStates(OrderOutVO orderOut);
}
