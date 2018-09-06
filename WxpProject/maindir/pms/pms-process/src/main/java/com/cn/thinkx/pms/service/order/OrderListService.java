package com.cn.thinkx.pms.service.order;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.order.OrderList;

public interface OrderListService {

	public List<OrderList> dataGrid(OrderList ls, PageFilter ph);

	public Long count(OrderList ls, PageFilter ph);

	public void batchSaveCardOrderList(List<OrderList> orderLists);
	
	public OrderList search(String card);

}
