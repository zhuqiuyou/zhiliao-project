package com.cn.thinkx.ecom.cash.wxtrans.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.beans.CtrlSystem;
import com.cn.thinkx.ecom.cash.wxtrans.mapper.CtrlSystemMapper;
import com.cn.thinkx.ecom.cash.wxtrans.service.CtrlSystemService;

@Service("ctrlSystemService")
public class CtrlSystemServiceImpl implements CtrlSystemService {

	@Autowired
	private CtrlSystemMapper ctrlSystemMapper;
	
	@Override
	public CtrlSystem getCtrlSystem() {
		return ctrlSystemMapper.getCtrlSystem();
	}

}
