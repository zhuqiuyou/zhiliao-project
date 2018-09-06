package com.cn.thinkx.pms.service.shop;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.shop.MerchantInf;

public interface MerchantService {

	List<MerchantInf> dataGrid(MerchantInf merchantInf, PageFilter ph);

	Long count(MerchantInf merchantInf, PageFilter ph);

	void add(MerchantInf merchantInf);

	void delete(String id);

	void edit(MerchantInf m);

	MerchantInf get(String id);

	MerchantInf getMerchantInfByCode(String name);

	List<Tree> tree();

	List<MerchantInf> findMerchantList(MerchantInf merchantInf);
}
