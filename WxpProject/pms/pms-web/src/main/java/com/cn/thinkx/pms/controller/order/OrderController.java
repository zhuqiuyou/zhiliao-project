package com.cn.thinkx.pms.controller.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.card.Card;
import com.cn.thinkx.pms.pageModel.card.CardSearch;
import com.cn.thinkx.pms.pageModel.order.Order;
import com.cn.thinkx.pms.pageModel.sys.Dictionary;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.card.CardService;
import com.cn.thinkx.pms.service.order.OrderListService;
import com.cn.thinkx.pms.service.order.OrderService;
import com.cn.thinkx.pms.service.product.ProductService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

/**
 * 
 * @author sunyue //100100 制卡订单 //200100 充值订单 //300100 激活订单 //400100 售卡订单
 * 
 */
@Component
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

	Logger log = Logger.getLogger("OrderController");
	@Autowired
	private OrderService orderService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ProductService productService;
	@Autowired
	private DictionaryServiceI dictionaryService;
	@Autowired
	private OrderListService orderListService;
	@Autowired
	private CardService cardService;
	@Value("${downloadedStatus}")
	private String downloadedStatus;
	@Value("${orderDownloadPath}")
	private String orderDownPath;

	@RequestMapping("/manager/{type}")
	public String manager(HttpServletRequest request, @PathVariable("type") String type) {
		
		List<Dictionary> statusList = dictionaryService.comboxCodeAndTextByTypeCode("makeorderstatus");
		if (StringUtil.isNotEmpty(type)) {
			request.setAttribute("orderTypeList",
					JSON.toJSONString(dictionaryService.comboxCodeAndTextByTypeCode("ordertype")));
			
			request.setAttribute("makeorderstatusList",
					JSON.toJSONString(statusList));
			
			request.setAttribute("productList", JSON.toJSONString(productService.findProductList(null)));
		}
		String returnPage = "/order/order";
		if ("100100".equals(type)) {
			List<Dictionary> makeStatusList = convertMakeStatus(statusList);
			request.setAttribute("orderstatusList",makeStatusList);
			request.setAttribute("cardPinStatList", GlobalConstant.convertJson(GlobalConstant.card_pin_status));
		} else if ("200100".equals(type)) {
			List<Dictionary> rechargeStatusList = convertRechargeStatus(statusList);
			request.setAttribute("orderstatusList",rechargeStatusList);
			returnPage = "/order/rechargeableOrder";
		} else if ("300100".equals(type)) {
			List<Dictionary> activateStatusList = convertActivateStatus(statusList);
			request.setAttribute("orderstatusList",activateStatusList);
			returnPage = "/order/activateOrder";
		} else if ("400100".equals(type)) {
			List<Dictionary> sellStatusList = convertSellStatus(statusList);
			request.setAttribute("orderstatusList",sellStatusList);
			request.setAttribute("orderActivateSts", GlobalConstant.order_activate_sts);
			request.setAttribute("orderActivateStsJson", GlobalConstant.convertJson(GlobalConstant.order_activate_sts));
			returnPage = "/order/sellOrder";
		}
		return returnPage;
	}

	@RequestMapping("/dataGrid/{type}")
	@ResponseBody
	public Grid dataGrid(Order order, PageFilter ph, @PathVariable("type") String type) {
		Grid grid = new Grid();
		order.setOrderType(type);
		grid.setRows(orderService.dataGrid(order, ph));
		grid.setTotal(orderService.count(order, ph));
		return grid;
	}

	@RequestMapping("/cardDataGrid/{type}")
	@ResponseBody
	public Grid cardDataGrid(@ModelAttribute CardSearch card, @PathVariable("type") String type, PageFilter ph) {
		Grid grid = new Grid();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(card.getProductSearchCode())) {
			map.put("productCode", card.getProductSearchCode());
		} else {
			return grid;
		}
		List<Card> cardList = null;
		if ("200100".equals(type)) {
			map.put("stockStat", "20");
			map.put("cancelStat", "1");
		} else if ("300100".equals(type)) {
			map.put("stockStat", "20");
			map.put("cardStat", "00");
		} else if ("400100".equals(type)) {
			map.put("stockStat", "00");
			cardList = orderService.getCardByOrderId(card.getOrderId(),card.getStartCardNum(),card.getEndCardNum());
		}
		if (StringUtil.isNotEmpty(card.getStartCardNum())) {
			map.put("startCardNum", card.getStartCardNum() );
		}
		if (StringUtil.isNotEmpty(card.getEndCardNum())) {
			map.put("endCardNum", card.getEndCardNum());
			
		}
		if ("400100".equals(type)) {
			if (cardList ==null || cardList.isEmpty()) {
				grid.setRows(cardService.dataCardGrid(map, ph));
				grid.setTotal(cardService.countCard(map, ph));
			} else {
				List<Card> list = cardService.dataCardGrid(map, ph);
				List<Card> allList = new ArrayList<Card>();
				allList.addAll(cardList);
				allList.addAll(list);
				grid.setRows(allList);
				grid.setTotal(new Long(allList.size()));
			}
		} else {
			grid.setRows(cardService.dataCardGrid(map, ph));
			grid.setTotal(cardService.countCard(map, ph));
		}
				
		return grid;
	}

	@RequestMapping("/addPage/{type}")
	public String addPage(HttpServletRequest request, @PathVariable("type") String type) {
		request.setAttribute("orderTypeList", dictionaryService.comboxCodeAndTextByTypeCode("ordertype"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataStat", "0");
		request.setAttribute("productList", productService.findProductList(params));
		String returnPage = "/order/orderAdd";
		if ("100100".equals(type)) {
			request.setAttribute("cardPinStatList", GlobalConstant.card_pin_status);
		} else if ("200100".equals(type)) {
			returnPage = "/order/rechargeableOrderAdd";
		} else if ("300100".equals(type)) {
			returnPage = "/order/activateOrderAdd";
		} else if ("400100".equals(type)) {
			request.setAttribute("orderActivateSts", GlobalConstant.order_activate_sts);
			returnPage = "/order/sellOrderAdd";
		}
		return returnPage;
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(Order order, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		try {
			order.setCreateUser(o.getLoginname());
			order.setCreateTime(new Date());
			order.setOrderDate(DateUtil.getDateTime(DateUtil.FORMAT_YYYYMMDD, new Date()));
			order.setNopintTxnAmt(StringUtil.fromYuanToCent(order.getNopintTxnAmt()));
			orderService.add(order);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/get")
	@ResponseBody
	public Order get(String id) {
		return orderService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			orderService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/submitOrder")
	@ResponseBody
	public Json submitOrder(String id, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		try {
			Order order = orderService.get(id);
			order.setUpdateTime(new Date());
			order.setUpdateUser(o.getLoginname());
			order.setOrderStat("11");
			if ("100100".equals(order.getOrderType())) {
				orderService.dealMarkCardOrder(order);
			} else {
				orderService.dealCardOrder(order);
			}
			j.setMsg("订单提交成功!");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String type, String id) {

		request.setAttribute("orderTypeList", dictionaryService.comboxCodeAndTextByTypeCode("ordertype"));
		request.setAttribute("productList", productService.findProductList(null));
		Order u = orderService.get(id);
		request.setAttribute("order", u);

		String returnPage = "/order/orderEdit";
		if ("100100".equals(type)) {
			request.setAttribute("cardPinStatList", GlobalConstant.card_pin_status);
		} else if ("200100".equals(type)) {
			returnPage = "/order/rechargeableOrderEdit";
		} else if ("300100".equals(type)) {
			returnPage = "/order/activateOrderEdit";
		} else if ("400100".equals(type)) {
			request.setAttribute("orderActivateSts", GlobalConstant.order_activate_sts);
			returnPage = "/order/sellOrderEdit";
		}
		return returnPage;
	}

	@RequestMapping("/viewPage")
	public String viewPage(HttpServletRequest request, String type, String id) {

		request.setAttribute("orderTypeList", dictionaryService.comboxCodeAndTextByTypeCode("ordertype"));
		request.setAttribute("productList", productService.findProductList(null));
		request.setAttribute("cardPinStatList", GlobalConstant.card_pin_status);
		Order u = orderService.get(id);
		
		request.setAttribute("order", u);
		String returnPage = "/order/orderView";
		if ("100100".equals(type)) {
			u.setNopintTxnAmt(StringUtil.fromCentToYuan(u.getNopintTxnAmt()));
		} else if ("200100".equals(type)) {
			returnPage = "/order/rechargeableOrderView";
		} else if ("300100".equals(type)) {
			returnPage = "/order/activateOrderView";
		} else if ("400100".equals(type)) {
			request.setAttribute("orderActivateSts", GlobalConstant.order_activate_sts);
			returnPage = "/order/sellOrderView";
		}
		return returnPage;
	}
	
	@RequestMapping("/detailList")
	public String detailList(HttpServletRequest request, String type, String id) {
		request.setAttribute("orderListTypeList",
				JSON.toJSONString(dictionaryService.comboxCodeAndTextByTypeCode("orderListType")));
		request.setAttribute("id", id);
		if (StringUtil.isNotEmpty(type)) {
			request.setAttribute("type", type);
		}
		return "/order/orderList";
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			Order order = orderService.get(request.getParameter("id"));
			String filename = order.getId() + "_rsp";
			String path = orderDownPath + File.separator + filename;
			File file = new File(path);
			fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

			order.setOrderStat(downloadedStatus);
			orderService.edit(order);
		} catch (Exception e) {
			log.log(Level.FINEST, e.getMessage(), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (toClient != null) {
				try {
					toClient.flush();
					toClient.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Order order, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		try {
			order.setUpdateUser(o.getLoginname());
			order.setUpdateTime(new Date());
			order.setNopintTxnAmt(StringUtil.fromYuanToCent(order.getNopintTxnAmt()));
			orderService.edit(order);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/finish")
	@ResponseBody
	public Json finish(String id, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		try {
			Order order = orderService.get(id);
			order.setUpdateTime(new Date());
			order.setUpdateUser(o.getLoginname());
			order.setOrderStat("15");
			orderService.edit(order);
			j.setMsg("操作成功!");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/tree/{type}")
	@ResponseBody
	public List<Tree> tree(HttpSession session, @PathVariable("type") String type) {
		return orderService.tree(type);
	}
	
	private List<Dictionary> convertMakeStatus(List<Dictionary> statusList) {
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (!statusList.isEmpty()) {
			for (Dictionary d : statusList) {
				if (d.getCode().equals("00")
						|| d.getCode().equals("01")
						|| d.getCode().equals("02")
						|| d.getCode().equals("11")
						|| d.getCode().equals("12")
						|| d.getCode().equals("13")
						|| d.getCode().equals("14")
						|| d.getCode().equals("15")) {
					list.add(d);
				}
						
			}
		}
		return list;
	}
	
	private List<Dictionary> convertRechargeStatus(List<Dictionary> statusList) {
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (!statusList.isEmpty()) {
			for (Dictionary d : statusList) {
				if (d.getCode().equals("00")
						|| d.getCode().equals("01")
						|| d.getCode().equals("02")
						|| d.getCode().equals("21")
						|| d.getCode().equals("22")
						|| d.getCode().equals("25")) {
					list.add(d);
				}
						
			}
		}
		return list;
	}
	
	private List<Dictionary> convertActivateStatus(List<Dictionary> statusList) {
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (!statusList.isEmpty()) {
			for (Dictionary d : statusList) {
				if (d.getCode().equals("00")
						|| d.getCode().equals("01")
						|| d.getCode().equals("02")
						|| d.getCode().equals("31")
						|| d.getCode().equals("32")
						|| d.getCode().equals("35")) {
					list.add(d);
				}
						
			}
		}
		return list;
	}
	
	private List<Dictionary> convertSellStatus(List<Dictionary> statusList) {
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (!statusList.isEmpty()) {
			for (Dictionary d : statusList) {
				if (d.getCode().equals("00")
						|| d.getCode().equals("01")
						|| d.getCode().equals("02")
						|| d.getCode().equals("41")
						|| d.getCode().equals("42")
						|| d.getCode().equals("55")) {
					list.add(d);
				}
						
			}
		}
		return list;
	}
}
