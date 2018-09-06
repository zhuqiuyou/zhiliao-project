package com.cn.thinkx.pms.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.order.TbOrder;
import com.cn.thinkx.pms.model.order.TbOrderList;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.order.OrderList;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.order.OrderListService;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class OrderListServiceImpl implements OrderListService {
	@Autowired
	private BaseDaoI<TbOrderList> orderListDao;
	@Autowired
	private BaseDaoI<TbOrder> orderDao;
	@Autowired
	private CommonService commonService;

	@Override
	public List<OrderList> dataGrid(OrderList ls, PageFilter ph) {
		List<OrderList> ols = new ArrayList<OrderList>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select  t from TbOrderList t left join t.order to ";
		List<TbOrderList> l = orderListDao.find(hql + whereHql(ls,params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbOrderList t : l) {
			OrderList u = new OrderList();
			BeanUtils.copyProperties(t, u);
			u.setAmount(StringUtil.fromCentToYuan(u.getAmount()));
			ols.add(u);
		}
		return ols;
	}

	private String whereHql(OrderList ls, Map<String, Object> params) {
		String hql = "";
		if (ls != null) {
			hql += " where 1=1 ";

			if (ls.getCreateTimeStart() != null) {
				hql += " and t.createTime >= :createTimeStart";
				params.put("createTimeStart", ls.getCreateTimeStart());
			}
			if (ls.getCreateTimeEnd() != null) {
				hql += " and t.createTime <= :createTimeEnd";
				params.put("createTimeEnd", ls.getCreateTimeEnd());
			}
			if (!StringUtil.isNullOrEmpty(ls.getCardNo())) {
				hql += " and t.cardNo like :cardNo";
				params.put("cardNo", "%%"  +ls.getCardNo() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(ls.getOrderId())) {
				hql += " and to.id = :orderId";
				params.put("orderId", ls.getOrderId());
			}
			
		}
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if (!StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public Long count(OrderList orderList, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select count(t.id)  from TbOrderList t left join t.order to ";
		return orderListDao.count(hql + whereHql(orderList, params), params);
	}

	@Override
	public void batchSaveCardOrderList(List<OrderList> orderLists) {
		List<TbOrderList> tbList = new ArrayList<TbOrderList>();
		if (orderLists != null && orderLists.size() > 0) {
			for (OrderList orderList : orderLists) {
				TbOrderList tb = new TbOrderList();
				BeanUtils.copyProperties(orderList, tb);
				tb.setCreateTime(new Date());
//				tb.setPinStat("00");
				tb.setStat("00");
				TbOrder tbOrder = orderDao.get(TbOrder.class, orderList.getOrderId());
				tb.setOrder(tbOrder);
				tb.setId(commonService.initSeqId(TbOrderList.class));
				tbList.add(tb);
				orderListDao.save(tb);
			}
		}
//		orderListDao.batchSave(tbList);
	}
	

	@Override
	public OrderList search(String card) {
		if (StringUtil.isNotBlank(card)) {
			String hql = "  from TbOrderList t where t.cardNo='" + card+"'";
			TbOrderList ol = orderListDao.get(hql);
			if (ol != null) {
				OrderList l = new OrderList();
				BeanUtils.copyProperties(ol, l);
				return l;
			}
		}
		return null;
	}
}
