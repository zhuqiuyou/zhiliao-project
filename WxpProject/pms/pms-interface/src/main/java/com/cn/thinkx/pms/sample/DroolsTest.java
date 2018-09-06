package com.cn.thinkx.pms.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.cn.thinkx.pms.drools.fact.PaymentInfo;

public class DroolsTest {

	public static void main(String[] args) {
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("HelloWorldKS");

    	PaymentInfo message = new PaymentInfo();
    	// 因子设置
		message.setInMoney(1000);
		System.out.println("消费金额：" + message.getInMoney());
        kSession.insert(message);
        kSession.fireAllRules();
        // kSession.dispose();
        System.out.println("优惠后金额：" + message.getOutMoney());
	}
	
}