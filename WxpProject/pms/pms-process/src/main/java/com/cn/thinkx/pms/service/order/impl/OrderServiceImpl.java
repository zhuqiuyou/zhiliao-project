package com.cn.thinkx.pms.service.order.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.LockTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.card.TbCardInf;
import com.cn.thinkx.pms.model.order.TbOrder;
import com.cn.thinkx.pms.model.order.TbOrderList;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.card.Card;
import com.cn.thinkx.pms.pageModel.order.Order;
import com.cn.thinkx.pms.pageModel.order.OrderList;
import com.cn.thinkx.pms.pageModel.product.Product;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.card.CardService;
import com.cn.thinkx.pms.service.order.OrderListService;
import com.cn.thinkx.pms.service.order.OrderService;
import com.cn.thinkx.pms.service.product.ProductService;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private BaseDaoI<TbOrder> orderDao;
	@Autowired
	private BaseDaoI<TbOrderList> orderListDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CardService cardService;
	@Autowired
	private BaseDaoI<TbCardInf> cardDao;
	@Autowired
	private OrderListService orderListService;

	@Override
	public List<Order> dataGrid(Order Order, PageFilter ph) {
		List<Order> kl = new ArrayList<Order>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbOrder t ";
		List<TbOrder> l = orderDao.find(hql + whereHql(Order, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbOrder t : l) {
			Order u = new Order();
			BeanUtils.copyProperties(t, u);
			u.setNopintTxnAmt(StringUtil.fromCentToYuan(u.getNopintTxnAmt()));
			kl.add(u);
		}
		return kl;
	}

	private String whereHql(Order kv, Map<String, Object> params) {
		String hql = "";
		if (kv != null) {
			hql += " where 1=1 ";
			if (!StringUtil.isNullOrEmpty(kv.getProductCode())) {
				hql += " and t.productCode like :productCode";
				params.put("productCode", "%%" + kv.getProductCode() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(kv.getProductName())) {
				hql += " and t.productName like :productName";
				params.put("productName", "%%" + kv.getProductName() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(kv.getId())) {
				hql += " and t.id like :id";
				params.put("id", "%%" + kv.getId() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(kv.getOrderType())) {
				hql += " and t.orderType like :orderType";
				params.put("orderType", "%%" + kv.getOrderType() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(kv.getOrderStat())) {
				hql += " and t.orderStat = :orderStat";
				params.put("orderStat", kv.getOrderStat());
			}
			if (!StringUtil.isNullOrEmpty(kv.getActStat())) {
				hql += " and t.actStat = :actStat";
				params.put("actStat", kv.getActStat());
			}
			if (kv.getCreateTimeStart() != null) {
				hql += " and t.createTime >= :createTimeStart";
				params.put("createTimeStart", kv.getCreateTimeStart());
			}
			if (kv.getCreateTimeEnd() != null) {
				hql += " and t.createTime <= :createTimeEnd";
				params.put("createTimeEnd", kv.getCreateTimeEnd());
			}
		}
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if (ph != null && !StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public Long count(Order kv, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbOrder t ";
		return orderDao.count("select count(*) " + hql + whereHql(kv, params), params);
	}

	@Override
	public void add(Order order) {
		TbOrder tb = new TbOrder();
		BeanUtils.copyProperties(order, tb);
		tb.setOrderStat(GlobalConstant.PUBLIC_ORDER_STATUS.DRAFT.getValue());
		tb.setOrderDate(DateUtil.getDateTime("yyyyMMdd", new Date()));
		tb.setId(commonService.initSeqId(TbOrder.class));
		// 100100 制卡订单
		// 200100 充值订单
		// 300100 激活订单
		// 400100 售卡订单
		if (!"100100".equals(tb.getOrderType())) {//
			String cardList = order.getCardNoList();
			tb.setCardNum(0);
			if (StringUtil.isNotBlank(cardList)) {
				List<TbOrderList> toll = new ArrayList<TbOrderList>();
				JSONArray cards = (JSONArray) JSONArray.parse(cardList.replaceAll("&quot;", "\""));
				Product product = productService.get(order.getProductCode());
				String cardNoList = "";
				for (Object cl : cards) {
					JSON json = (JSON) cl;
					TbOrderList tol = new TbOrderList();
					Card c = JSON.parseObject(json.toJSONString(), Card.class);
					cardNoList += ",'" + c.getCardNo() + "'";
					tol.setCardNo(c.getCardNo());
					tol.setAmount(StringUtil.fromYuanToCent(order.getAmount()));
					if (product.getValidityPeriod() != null) {
						Calendar cld = Calendar.getInstance();
						cld.add(Calendar.MONTH, product.getValidityPeriod());
						tol.setValidDate(DateUtil.getDateTime("yyyyMMdd", cld.getTime()));
					}
					tol.setCreateTime(new Date());
					tol.setStat("00");
					tol.setCreateUser(order.getCreateUser());
					tol.setOrder(tb);
					tol.setId(commonService.initSeqId(TbOrderList.class));
					toll.add(tol);
					tb.getOrderListSet().add(tol);
				}
				tb.setCardNum(cards.size());
				// orderListDao.batchSave(toll);
				if ("400100".equals(tb.getOrderType())) {
					cardDao.executeSql("UPDATE TB_CARD_INF SET STOCK_STAT = '10'  WHERE CARD_NO in("
							+ cardNoList.substring(1) + ")");
				}
			}
		}
		orderDao.save(tb);
	}

	@Override
	public void delete(String id) {
		TbOrder kv = orderDao.get(" from TbOrder p where p.id='" + id + "'");
		kv.setOrderStat("00");// 取消订单
		orderDao.saveOrUpdate(kv);
		if ("400100".equals(kv.getOrderType())) {
			cardDao.executeSql("UPDATE TB_CARD_INF SET STOCK_STAT = '00'  WHERE CARD_NO in( select tt.CARD_NO from TB_ORDER_LIST tt where tt.ORDER_ID='"
					+ id + "')");
		}
	}

	@Override
	public void edit(Order order) {
		TbOrder tborder = orderDao.get("select t from TbOrder t left join  t.orderListSet st  where t.id='"
				+ order.getId() + "'");
		if (tborder.getLockVersion() != order.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			order.setCreateTime(tborder.getCreateTime());
			order.setCreateUser(tborder.getCreateUser());
			BeanUtils.copyProperties(order, tborder);
		}
		if (!"100100".equals(tborder.getOrderType()) && "01".equals(tborder.getOrderStat())) {
			/* 删除原有草稿的订单明细 */
			List<TbOrderList> orderList = new ArrayList<TbOrderList>(tborder.getOrderListSet());
			Set<String> wDelCardNoSet = new HashSet<String>();
			Set<String> wUpdateCardNoSet = new HashSet<String>();
			List<String> Idlist = new ArrayList<String>();
			for (TbOrderList tbOrderList : orderList) {
				Idlist.add(tbOrderList.getId());
				wDelCardNoSet.add(tbOrderList.getCardNo());
			}
			orderListDao.batchDelete(TbOrderList.class, "id", Idlist);
			/* 生成最新订单明细 */
			String cardList = order.getCardNoList();
			if (StringUtil.isNotBlank(cardList)) {
				Set<TbOrderList> toll = new HashSet<TbOrderList>();
				JSONArray cards = (JSONArray) JSONArray.parse(cardList.replaceAll("&quot;", "\""));
				Product product = productService.get(order.getProductCode());
				for (Object cl : cards) {
					JSON json = (JSON) cl;
					TbOrderList tol = new TbOrderList();
					Card c = JSON.parseObject(json.toJSONString(), Card.class);
					String cn = c.getCardNo();
					if (wDelCardNoSet.contains(cn)) {
						wDelCardNoSet.remove(cn);
					} else {
						wUpdateCardNoSet.add(cn);
					}
					tol.setCardNo(cn);
					tol.setAmount(StringUtil.fromYuanToCent(order.getAmount()));
					if (product.getValidityPeriod() != null) {
						Calendar cld = Calendar.getInstance();
						cld.add(Calendar.MONTH, product.getValidityPeriod());
						tol.setValidDate(DateUtil.getDateTime("yyyyMMdd", cld.getTime()));
					}
					tol.setStat("00");
					tol.setCreateTime(new Date());
					tol.setCreateUser(order.getCreateUser());
					tol.setOrder(tborder);
					tol.setId(commonService.initSeqId(TbOrderList.class));
					toll.add(tol);
				}
				tborder.setOrderListSet(toll);
				tborder.setCardNum(cards.size());

				if ("400100".equals(tborder.getOrderType())) {
					if (!wUpdateCardNoSet.isEmpty()) {
						cardDao.executeSql("UPDATE TB_CARD_INF SET STOCK_STAT = '10'  WHERE CARD_NO in("
								+ convertStringSetToString(wUpdateCardNoSet) + ")");
					}
					if (!wDelCardNoSet.isEmpty()) {
						cardDao.executeSql("UPDATE TB_CARD_INF SET STOCK_STAT = '00'  WHERE CARD_NO in("
								+ convertStringSetToString(wDelCardNoSet) + ")");
					}
				}
				// orderListDao.batchSave(toll);
			}
		} else if ("100100".equals(tborder.getOrderType()) && "15".equals(tborder.getOrderStat())) {
			// 制卡订单完成时，将卡状态更新为已入库
			String hql = "UPDATE TB_CARD_INF C SET C.STOCK_STAT ='00' WHERE EXISTS( SELECT 1 FROM TB_ORDER_LIST O WHERE O.CARD_NO = C.CARD_NO AND O.ORDER_ID = '"
					+ order.getId() + "')";
			cardDao.executeSql(hql);
		}
		orderDao.update(tborder);
	}

	private String convertStringSetToString(Set<String> set) {
		if (set != null && set.size() > 0) {
			String t = "";
			for (String s : set) {
				t += ",'" + s + "'";
			}
			return t.substring(1);
		}
		return "";
	}

	@Override
	public Order get(String id) {
		TbOrder order = orderDao.get("select t from TbOrder t where t.id='" + id + "'");
		List<TbOrderList> tol = orderListDao.find("select t from TbOrderList t where t.order='" + id + "'");
		Order kv = new Order();
		BeanUtils.copyProperties(order, kv);
		org.json.JSONArray jsonArray = new org.json.JSONArray();
		for (TbOrderList tbo : tol) {
			kv.setAmount(StringUtil.fromCentToYuan(tbo.getAmount()));
			JSONObject json = new JSONObject();
			try {
				json.put("cardNo", tbo.getCardNo());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonArray.put(json);
		}
		kv.setCardNoList(jsonArray.toString());
		return kv;
	}

	@Override
	public List<Card> getCardByOrderId(String id, String startCardNum, String endCardNum) {

		String sql = "select t from TbOrderList t where t.order='" + id + "'";
		if (!StringUtil.isNullOrEmpty(startCardNum)) {
			sql += " and t.cardNo >= '" + startCardNum + "'";
		}
		if (!StringUtil.isNullOrEmpty(endCardNum)) {
			sql += " and t.cardNo <= '" + endCardNum + "'";
		}
		List<TbOrderList> tol = orderListDao.find(sql);
		List<Card> list = new ArrayList<Card>();
		for (TbOrderList tbo : tol) {
			Card card = new Card();
			card.setCardNo(tbo.getCardNo());
			list.add(card);
		}
		return list;
	}

	@Override
	public List<Tree> tree(String type) {
		List<Tree> lt = new ArrayList<Tree>();
		String hql = "select distinct t from TbOrder t  where 1=1";
		if (StringUtil.isNotEmpty(type)) {
			hql = hql + "and  t.orderType='" + type;
		}
		List<TbOrder> orders = orderDao.find(hql + "' order by t.createTime");

		if ((orders != null) && (orders.size() > 0)) {
			for (TbOrder r : orders) {
				Tree tree = new Tree();
				tree.setId(r.getId());
				tree.setText(r.getProductName() + "(" + r.getId() + ")");
				lt.add(tree);
			}
		}
		return lt;
	}

	@Override
	public void dealMarkCardOrder(Order order) throws Exception {
		/* 更新卡产品的最总卡号 */
		int num = order.getCardNum();
		Product product = productService.get(order.getProductCode());
		int lastNum = product.getLastCardNum();
		product.setLastCardNum(lastNum + num);
		productService.edit(product);
		/* 生成订单明细 */
		List<OrderList> orderLists = new ArrayList<OrderList>();
		for (int i = lastNum; i < lastNum + num; i++) {
			OrderList orderList = new OrderList();
			orderList.setCreateUser(order.getCreateUser());
			orderList.setOrderId(order.getId());
			if (product.getValidityPeriod() != null) {
				Calendar cl = Calendar.getInstance();
				cl.add(Calendar.MONTH, product.getValidityPeriod());
				orderList.setValidDate(DateUtil.getDateTime("yyyyMMdd", cl.getTime()));
			}
			/* 生成卡号 */
			String code18 = product.getCardBin() + StringUtil.leftPad(String.valueOf(i), 8, "0");
			orderList.setCardNo(cardService.getCardCode(code18));
			orderLists.add(orderList);
		}
		orderListService.batchSaveCardOrderList(orderLists);

		/* 更新订单状态 */
		edit(order);
	}

	@Override
	public void dealCardOrder(Order order) throws Exception {
		TbOrder td = orderDao.get("select t from TbOrder t left join t.orderListSet st where t.id='" + order.getId()
				+ "'");
		for (TbOrderList orderList : td.getOrderListSet()) {
			orderList.setStat("00");
		}
		if ("200100".equals(td.getOrderType())) {
			td.setOrderStat("21");
		} else if ("300100".equals(td.getOrderType())) {
			td.setOrderStat("31");
		} else if ("400100".equals(td.getOrderType())) {
			td.setOrderStat("41");
		}
		orderDao.update(td);
	}
}
