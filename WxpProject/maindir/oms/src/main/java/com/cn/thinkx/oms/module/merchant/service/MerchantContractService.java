package com.cn.thinkx.oms.module.merchant.service;

import com.cn.thinkx.oms.module.merchant.model.MerchantContract;
import com.cn.thinkx.oms.module.merchant.model.MerchantInf;

public interface MerchantContractService {
	
	/**
	 * 查找商户合同信息
	 * @param merchantContractId
	 * @return
	 */
	public MerchantContract getMerchantContractById(String merchantContractId);
	/**
	 * 根据商户id查找商户合同信息
	 * @param mchntId
	 * @return
	 */
	public MerchantContract getMerchantContractByMerchantId(String mchntId);
	/**
	 * 插入商户合同信息
	 * @param merchantContract
	 * @return
	 */
	public int insertMerchantContract(MerchantContract merchantContract);
	
	/**
	 * 插入商户默认合同信息
	 * @param merchantInf
	 * @return
	 */
	public int insertDefaultMerchantContract(MerchantInf merchantInf);
	
	/**
	 * 更新商户合同信息
	 * @param merchantContract
	 * @return
	 */
	public int updateMerchantContract(MerchantContract merchantContract);
	/**
	 * 删除商户合同信息
	 * @param merchantContractId
	 * @return
	 */
	public int deleteMerchantContract(String merchantContractId);

}
