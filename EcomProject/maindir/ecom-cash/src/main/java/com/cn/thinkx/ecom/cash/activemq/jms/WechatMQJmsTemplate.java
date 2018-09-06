package com.cn.thinkx.ecom.cash.activemq.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class WechatMQJmsTemplate {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("connect")
	private SingleConnectionFactory connectionFactory;
	
	@Value("${wecard.wechat.msg}")
    private String wechatMSg;
	
	@Value("${wecard.template.msg.mcht.v1}")
    private String templateMSg;
	
	@Bean(name = "consumerMsgJmsTemplate")
	public JmsTemplate getConsumerMsgJmsTemplate(){
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(wechatMSg);
		return jmsTemplate;
	}
	
	@Bean(name = "templateMsgJmsTemplate")
	public JmsTemplate getTemplateMsgJmsTemplate(){
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(templateMSg);
		return jmsTemplate;
	}

}
