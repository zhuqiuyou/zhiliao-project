package com.cn.thinkx.ecom.cash.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.cash.order.domain.OrderInf;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface OrderInfMapper extends BaseDao<OrderInf> {
	
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
	
}
