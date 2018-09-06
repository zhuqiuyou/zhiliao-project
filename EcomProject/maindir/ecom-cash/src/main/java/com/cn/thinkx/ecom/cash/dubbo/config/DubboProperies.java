package com.cn.thinkx.ecom.cash.dubbo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboProperies {

	@Value("${spring.dubbo.application.name}")
	private String applicationName;
	
	@Value("${spring.dubbo.protocol.name}")
	private String protocolName;
	
	@Value("${spring.dubbo.registry.address}")
	private String registryAddress;
	
//	@Value("${spring.dubbo.protocol.host}")
//	private String protocolHost;
	
//	@Value("${spring.dubbo.protocol.port}")
//	private int protocolPort;
	
//	@Value("${spring.dubbo.protocol.accepts}")
//	private int protocolAccepts;
	
//	@Value("${spring.dubbo.provider.id}")
//	private String providerId;
	
//	@Value("${spring.dubbo.provider.filter}")
//	private String providerFilter;
	
//	@Value("${spring.dubbo.provider.timeout}")
//	private int providerTimeout;
	
//	@Value("${spring.dubbo.provider.delay}")
//	private int providerDelay;
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	
	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

//	public String getProtocolHost() {
//		return protocolHost;
//	}
//
//	public void setProtocolHost(String protocolHost) {
//		this.protocolHost = protocolHost;
//	}

//	public int getProtocolPort() {
//		return protocolPort;
//	}
//
//	public void setProtocolPort(int protocolPort) {
//		this.protocolPort = protocolPort;
//	}

//	public int getProtocolAccepts() {
//		return protocolAccepts;
//	}
//
//	public void setProtocolAccepts(int protocolAccepts) {
//		this.protocolAccepts = protocolAccepts;
//	}

//	public String getProviderId() {
//		return providerId;
//	}
//
//	public void setProviderId(String providerId) {
//		this.providerId = providerId;
//	}

//	public String getProviderFilter() {
//		return providerFilter;
//	}
//
//	public void setProviderFilter(String providerFilter) {
//		this.providerFilter = providerFilter;
//	}

//	public int getProviderTimeout() {
//		return providerTimeout;
//	}
//
//	public void setProviderTimeout(int providerTimeout) {
//		this.providerTimeout = providerTimeout;
//	}

//	public int getProviderDelay() {
//		return providerDelay;
//	}
//
//	public void setProviderDelay(int providerDelay) {
//		this.providerDelay = providerDelay;
//	}

}
