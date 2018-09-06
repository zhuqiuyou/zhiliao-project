package com.cn.thinkx.pms.service.contract;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.contract.MerchantContract;

public interface MerchantContractService {

	List<MerchantContract> dataGrid(MerchantContract contract, PageFilter ph);

	Long count(MerchantContract contract, PageFilter ph);

	/**
	 * 获取合同号
	 * @return
	 */
	String initContractCode();
	
	void add(MerchantContract contract);

	void delete(String id);

	void edit(MerchantContract m,String userName);

	MerchantContract get(String id);
	
	void deleteDetail(String detailId);
	
	List<MerchantContract> queryByMerchantId(String merchantId);

}
