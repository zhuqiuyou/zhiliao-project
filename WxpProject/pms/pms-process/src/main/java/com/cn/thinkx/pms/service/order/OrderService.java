package com.cn.thinkx.pms.service.order;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.card.Card;
import com.cn.thinkx.pms.pageModel.order.Order;

public interface OrderService {

	List<Order> dataGrid(Order Order, PageFilter ph);

	Long count(Order Order, PageFilter ph);

	void add(Order Order);

	void delete(String id);

	void edit(Order m);

	Order get(String id);
	
	List<Card> getCardByOrderId(String id, String startCardNum, String endCardNum);

	List<Tree> tree(String type);

	/* 生成制卡订单明细 */
	void dealMarkCardOrder(Order order) throws Exception;

	/* 处理非制卡订单，仅仅需要更新明细的状态为 未处理就可以了 */
	void dealCardOrder(Order order) throws Exception;
}
