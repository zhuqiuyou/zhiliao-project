package com.cn.thinkx.pms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.thinkx.pms.drools.fact.PaymentInfo;
import com.cn.thinkx.pms.drools.service.PmsDroolsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-interface-drools.xml"})
public class DroolsTest {
	
	@Autowired
	private PmsDroolsService pdService;
	
	@Test
	public void testDrools() {
		try {
			KieSession kSession = pdService.getKieSession("KS_DISCOUNT");
			PaymentInfo message = new PaymentInfo();
	    	// 因子设置
			message.setInMoney(2000);
			System.out.println("消费金额：" + message.getInMoney());
			pdService.excute(kSession, message, null, null, null, null);
	        System.out.println("优惠后金额：" + message.getOutMoney());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String[] args) {
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
	}*/
	
}