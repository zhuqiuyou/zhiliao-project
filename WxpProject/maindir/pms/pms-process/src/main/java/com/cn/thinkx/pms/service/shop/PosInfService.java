package com.cn.thinkx.pms.service.shop;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.shop.PosInf;

public interface PosInfService {

	public List<PosInf> dataGrid(PosInf shop, PageFilter ph);

	public Long count(PosInf shop, PageFilter ph);

	public void add(PosInf shop);

	public void delete(String id);

	public void edit(PosInf shop);

	public PosInf get(String id);

	public PosInf getPosInfByTermId(String code);

}
