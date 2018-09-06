package com.cn.thinkx.ecom.cash.order.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.cn.thinkx.ecom.cash.order.domain.OrderBack;
import com.cn.thinkx.ecom.cash.order.domain.OrderInf;
import com.cn.thinkx.ecom.cash.order.domain.OrderRedirect;
import com.cn.thinkx.ecom.cash.order.domain.OrderRefund;
import com.cn.thinkx.ecom.cash.order.domain.OrderSearch;
import com.cn.thinkx.ecom.cash.order.domain.OrderUnified;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface OrderInfService extends BaseService<OrderInf> {

	/**
	 * 订单查询列表
	 * 
	 * @return
	 */
	List<OrderInf> getList();
	
	/**
	 * 根据电商订单号查询信息
	 * 
	 * @param routerOrderNo
	 * @return
	 */
	OrderInf selectByRouterOrderNo(String routerOrderNo);
	
	/**
	 * 新增订单表信息
	 * 
	 * @param order
	 * @return
	 */
	int insertOrder(OrderInf order);
	
	/**
	 * 修改订单表信息
	 * 
	 * @param order
	 * @return
	 */
	int updateOrder(OrderInf order);
	
	/**
	 * 根据商户号查询机构号
	 * 
	 * @param mchntCode
	 * @return
	 */
	String getInsCodeByMchntCode(String mchntCode);
	
	/**
	 * 根据多条件查询订单信息
	 * 
	 * @return
	 */
	OrderInf getOrderInfByOrderInf(OrderInf orderInf);
	
	/**
	 * 电商平台统一下单方法
	 * 
	 * @param resp
	 * @param orderUnified
	 * @return
	 * @throws Exception
	 */
	String ecomTransOrderUnified(HttpServletResponse resp, OrderUnified orderUnified) throws Exception;
	
	/**
	 * 电商平台重定向方法
	 * 
	 * @param orderRedirect
	 * @return
	 * @throws Exception
	 */
	String ecomTransRedirect(OrderRedirect orderRedirect) throws Exception;
	
	/**
	 * 电商平台异步回调方法
	 * 
	 * @return
	 */
	String ecomTransNotify(OrderBack orderBack) throws Exception;
	
	/**
	 * 电商平台订单查询方法
	 * 
	 * @param searchOrder
	 * @return
	 * @throws Exception
	 */
	String ecomTransOrderQuery(OrderSearch searchOrder) throws Exception;
	
	/**
	 * 电商平台订单退款方法
	 * 
	 * @param orderRefund
	 * @return
	 * @throws Exception
	 */
	String ecomTransOrderRefund(OrderRefund orderRefund) throws Exception;
	
}
