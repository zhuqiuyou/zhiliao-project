package com.cn.thinkx.ecom.cash.wxtrans.service;

import com.cn.thinkx.ecom.cash.wxtrans.domain.UserMerchantAcct;

public interface UserMerchantAcctService {

	/**
	 * 获取客户所在商户的会员卡余额
	 * 
	 * @param acc
	 * @return
	 */
	String getAccBalance(UserMerchantAcct acc);
	
	/**
	 * 获取用户UserId
	 * 
	 * @param acc
	 * @return
	 */
	String getUserId(UserMerchantAcct acc);
	
	/**
	 * 获取用户ExternalId
	 * 
	 * @param acc
	 * @return
	 */
	String getExternalId(UserMerchantAcct acc);
}
