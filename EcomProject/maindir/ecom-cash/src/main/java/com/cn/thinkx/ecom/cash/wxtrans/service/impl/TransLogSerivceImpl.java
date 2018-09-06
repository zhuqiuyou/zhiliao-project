package com.cn.thinkx.ecom.cash.wxtrans.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cn.thinkx.ecom.cash.wxtrans.domain.IntfaceTransLog;
import com.cn.thinkx.ecom.cash.wxtrans.domain.TransLog;
import com.cn.thinkx.ecom.cash.wxtrans.mapper.TransLogMapper;
import com.cn.thinkx.ecom.cash.wxtrans.service.TransLogService;

@Service("transLogService")
public class TransLogSerivceImpl implements TransLogService {
	
	@Autowired
	private TransLogMapper transLogMapper;
	
	@Override
	public String selectTransLog(TransLog transLog) {
		return this.transLogMapper.selectTransLog(transLog);
	}
	
	@Override
	public TransLog getTransLogByDmsId(String itfId) {
		return this.transLogMapper.getTransLogByDmsId(itfId);
	}

	@Override
	public IntfaceTransLog getIntfaceTransLogByDmsId(String dmsId) {
		return this.transLogMapper.getIntfaceTransLogByDmsId(dmsId);
	}

}
