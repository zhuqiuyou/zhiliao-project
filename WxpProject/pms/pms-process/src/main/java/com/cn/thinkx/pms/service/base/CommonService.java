package com.cn.thinkx.pms.service.base;

public interface CommonService {
	public <T> String initSeqId(Class<T> object);

	public String initProductCode();

	public String initContractCode();

	public Long initSysParamId();
}
