package com.cn.thinkx.pms.controller.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.order.OrderList;
import com.cn.thinkx.pms.service.order.OrderListService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;


@Controller
@RequestMapping("/orderList")
public class OrderListController extends BaseController {

	@Autowired
	private OrderListService orderListService;
	@Autowired
	private DictionaryServiceI dictionaryService;
	@RequestMapping("/manager/{type}")
	public String manager(HttpServletRequest request , @PathVariable("type") String type) {
		request.setAttribute("termSigninStatlist", GlobalConstant.convertJson(GlobalConstant.termSigninStatlist));
		request.setAttribute("pos_effectlist", GlobalConstant.convertJson(GlobalConstant.pos_effectlist));
		request.setAttribute("orderListTypeList",
				JSON.toJSONString(dictionaryService.comboxCodeAndTextByTypeCode("orderListType")));
		request.setAttribute("orderListPINStatusList",
				JSON.toJSONString(dictionaryService.comboxCodeAndTextByTypeCode("orderListPINStatus")));
		String returnPage = "/order/orderList";
		if ("100100".equals(type)) {

		} else if ("200100".equals(type)) {
			returnPage = "/order/rechargeableOrderList";
		} else if ("300100".equals(type)) {
			returnPage = "/order/activateOrderList";
		} else if ("400100".equals(type)) {
			returnPage = "/order/sellOrderList";
		}
		return returnPage;
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(OrderList ls, PageFilter ph, String orderId) {
		Grid grid = new Grid();
		ls.setOrderId(orderId);
		grid.setRows(orderListService.dataGrid(ls,ph));
		grid.setTotal(orderListService.count(ls, ph));
		return grid;
	}
}
