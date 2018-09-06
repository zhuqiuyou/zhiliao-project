package com.cn.thinkx.ecom.cash.dubbo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.cn.thinkx.facade.service.HKBTxnFacade;
import com.cn.thinkx.service.txn.Java2TxnBusinessFacade;

/**
 * 调用服务暴露的接口配置
 *
 */
@Configuration
public class DubboServiceConfig extends DubboConfig {

	@Bean
	public ReferenceBean<HKBTxnFacade> HKBTxnFacade() {
		ReferenceBean<HKBTxnFacade> referenceBean = new ReferenceBean<HKBTxnFacade>();
		
		referenceBean.setInterface(HKBTxnFacade.class.getName());
		referenceBean.setCluster("failfast");
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();
		
		/*** dubbo method cardBalanceQueryITF config*/
		MethodConfig methodConfig = new MethodConfig();
		methodConfig.setName("cardBalanceQueryITF");
		methodConfig.setTimeout(5000);
		methodConfig.setRetries(2);
		methods.add(methodConfig);
		
		referenceBean.setMethods(methods);
		return referenceBean;
	}
	
	@Bean
	public ReferenceBean<Java2TxnBusinessFacade> Java2TxnBusinessFacade() {
		ReferenceBean<Java2TxnBusinessFacade> referenceBean = new ReferenceBean<Java2TxnBusinessFacade>();
		
		referenceBean.setInterface(Java2TxnBusinessFacade.class.getName());
		referenceBean.setCluster("failfast");
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();
		
		/*** dubbo method consumeRefundITF 支持当日退款 config*/
		MethodConfig methodConfig = new MethodConfig();
		methodConfig.setName("consumeRefundITF");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig);
		
		/*** dubbo method transRefundITF 支持隔日退款 config*/
		methodConfig = new MethodConfig();
		methodConfig.setName("transRefundITF");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig);
		
		/*** dubbo method consumeTransactionITF config*/
		methodConfig = new MethodConfig();
		methodConfig.setName("consumeTransactionITF");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methodConfig.setMock(true);
		methods.add(methodConfig);
		
		/*** dubbo method transExceptionQueryITF config*/
		methodConfig = new MethodConfig();
		methodConfig.setName("transExceptionQueryITF");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(2);
		methods.add(methodConfig);
		
		referenceBean.setMethods(methods);
		return referenceBean;
	}
	
}
