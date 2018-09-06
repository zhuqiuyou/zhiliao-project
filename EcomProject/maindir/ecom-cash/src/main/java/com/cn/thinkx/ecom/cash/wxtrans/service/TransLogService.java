package com.cn.thinkx.ecom.cash.wxtrans.service;

import com.cn.thinkx.ecom.cash.wxtrans.domain.IntfaceTransLog;
import com.cn.thinkx.ecom.cash.wxtrans.domain.TransLog;

/**
 *  交易流水查询
 * 
 * @author xiaomei
 *
 */
public interface TransLogService {
	
	/**
	 * 根据用户openID，mchntCode,shopCode查询退款流水信息
	 * 
	 * @param transLog
	 * @return
	 */
	String selectTransLog(TransLog transLog);
	
	/**
	 * 根据ITF主键查询TXN流水信息
	 * 
	 * @param itfId
	 * @return
	 */
	TransLog getTransLogByDmsId(String itfId);
	
	/**
	 * 根据外部流水（嘉福京东流水）查询ITF信息
	 * 
	 * @param dmsId
	 * @return
	 */
	IntfaceTransLog getIntfaceTransLogByDmsId(String dmsId);
	
}
