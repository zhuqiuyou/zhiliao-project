package com.cn.thinkx.pms.service.settle;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.settle.SettleBill;

public interface SettleBillService {

	public List<SettleBill> dataGrid(SettleBill settleBill, PageFilter ph);

	public Long count(SettleBill settleBill, PageFilter ph);

	public void add(SettleBill settleBill);

	public void delete(String id);

	public void edit(SettleBill settleBill);

	public SettleBill get(String id);

}
