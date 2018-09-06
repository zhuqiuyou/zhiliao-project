package com.cn.thinkx.ecom.cash.wxtrans.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cn.thinkx.ecom.cash.wxtrans.domain.UserMerchantAcct;
import com.cn.thinkx.ecom.cash.wxtrans.mapper.UserMerchantAcctMapper;
import com.cn.thinkx.ecom.cash.wxtrans.service.UserMerchantAcctService;
import com.cn.thinkx.ecom.common.util.NumberUtils;

@Service("userMerchantAcctService")
public class UserMerchantAcctServiceImpl implements UserMerchantAcctService {

	@Autowired
	private UserMerchantAcctMapper userMerchantAcctMapper;

	@Override
	public String getAccBalance(UserMerchantAcct acc) {
		String accBal = this.userMerchantAcctMapper.getAccBalance(acc);
		return NumberUtils.RMBCentToYuan(accBal);
	}

	@Override
	public String getUserId(UserMerchantAcct acc) {
		return this.userMerchantAcctMapper.getUserId(acc);
	}

	@Override
	public String getExternalId(UserMerchantAcct acc) {
		return this.userMerchantAcctMapper.getExternalId(acc);
	}

}
