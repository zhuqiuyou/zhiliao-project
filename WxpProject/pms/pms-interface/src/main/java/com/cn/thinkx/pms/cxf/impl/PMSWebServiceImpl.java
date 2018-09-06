package com.cn.thinkx.pms.cxf.impl;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.thinkx.pms.cxf.PMSWebService;
import com.cn.thinkx.pms.drools.fact.PaymentInfo;
import com.cn.thinkx.pms.drools.service.PmsDroolsService;

public class PMSWebServiceImpl implements PMSWebService {

	@Autowired
	private PmsDroolsService pdService;
	
	@Override
	public void discount(int money) {
		 
		try {
			KieSession kSession = pdService.getKieSession("KS_DISCOUNT");
			PaymentInfo message = new PaymentInfo();
	    	// 因子设置
			message.setInMoney(money);
			System.out.println("消费金额：" + message.getInMoney());
			pdService.excute(kSession, message, null, null, null, null);
	        System.out.println("优惠后金额：" + message.getOutMoney());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override 
	public void setUser(String username) {
		 
	}

}
