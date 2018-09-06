package com.cn.thinkx.ecom.cash.order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.cash.order.domain.OrderBack;
import com.cn.thinkx.ecom.cash.order.domain.OrderRedirect;
import com.cn.thinkx.ecom.cash.order.domain.OrderRefund;
import com.cn.thinkx.ecom.cash.order.domain.OrderSearch;
import com.cn.thinkx.ecom.cash.order.domain.OrderUnified;
import com.cn.thinkx.ecom.cash.order.service.OrderInfService;
import com.cn.thinkx.ecom.common.util.StringUtil;

@RestController
@RequestMapping(value = "trans")
public class OrderInfController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	

	@Autowired
	private OrderInfService orderInfService;
	
	/**
	 * 知了企服电商平台统一下单，第三方订单下单调用接口
	 * 
	 * @param resp
	 * @param or
	 */
	@RequestMapping(value = "/order/unifiedOrder")
	public String unifiedOrder(HttpServletRequest req, HttpServletResponse resp, OrderUnified or) {
		logger.info("电商平台下单接口--->请求参数{}", JSONArray.toJSONString(or));
		try {
			String orderPayMent = orderInfService.ecomTransOrderUnified(resp, or);
		} catch (Exception e) {
			logger.error("## 电商平台下单接口--->异常{}", e);
		}
		return null;
	}

	/**
	 * 外部调用支付接口完成后页面重定向方法
	 * 
	 * @param or
	 * @return
	 */
	@RequestMapping(value = "/order/ecomRedirectResult")
	public ModelAndView ecomRedirectResult(OrderRedirect orderRedirect) {
		logger.info("电商平台重定向接口--->请求参数{}", JSONArray.toJSONString(orderRedirect));
		String redirectResp = null;
		try {
			redirectResp = orderInfService.ecomTransRedirect(orderRedirect);
		} catch (Exception e) {
			logger.error("## 电商平台重定向接口--->异常{}", e);
		}
		if (!StringUtil.isNullOrEmpty(redirectResp)) {
			return new ModelAndView("redirect:" + redirectResp);
		}
		return null;// TODO 根据返回信息返回错误页
	}
	
	/**
	 * 外部调用支付接口完成后异步通知方法
	 * 
	 * @param ob
	 * @return
	 */
	@RequestMapping(value = "/order/ecomBackResult")
	public String ecomBackResult(OrderBack orderBack) {
		logger.info("电商平台重定向接口--->请求参数{}", JSONArray.toJSONString(orderBack));
		String backResult = null;
		try {
			backResult = orderInfService.ecomTransNotify(orderBack);
		} catch (Exception e) {
			logger.error("## 电商平台重定向接口--->异常{}", e);
		}
		if (!StringUtil.isNullOrEmpty(backResult)) {
			return backResult;
		}
		return null;
	}

	/**
	 * 电商平台查单接口
	 * xiaomei
	 * 2018/5/4
	 * @param req
	 * @param resp
	 * @param searchOrder
	 * @return
	 */
	@RequestMapping(value = "/transOrderQuery", method = RequestMethod.POST)
	public String transOrderQuery(HttpServletRequest req, HttpServletResponse resp, @RequestBody JSONObject obj){
		logger.info("电商平台查询接口--->请求参数{}", obj);
		OrderSearch searchOrder = JSONObject.parseObject(obj.toString(), OrderSearch.class);
		String searchResp = null;
		try {
			searchResp = orderInfService.ecomTransOrderQuery(searchOrder);
		} catch (Exception e) {
			logger.error("## 电商平台查询接口--->异常{}", e);
		}
		return searchResp;
	}

	/**
	 * 电商平台退款接口
	 * xiaomei
	 * 2018/7/26
	 * @param req
	 * @param resp
	 * @param refund
	 * @return
	 */
	@RequestMapping(value = "/order/ecomOrderRefund", method = RequestMethod.POST)
	@ResponseBody
	public String orderRefund(HttpServletRequest req, HttpServletResponse resp, @RequestBody JSONObject obj){
		logger.info("电商平台退款接口--->请求参数{}", obj);
		OrderRefund orderRefund = JSONObject.parseObject(obj.toString(), OrderRefund.class);
		String refundResp = null;
		try {
			refundResp = orderInfService.ecomTransOrderRefund(orderRefund);
		} catch (Exception e) {
			logger.error("## 电商平台退款接口--->异常{}", e);
		} 
		return refundResp;
	}
	
	/*public static void main(String[] args) {
		OrderRefund refund = new OrderRefund();
		refund.setChannel("40006006");
		refund.setOriOrderId("116600000444");// 2018073102375900139574 116600000435
		refund.setRefundAmount("100");
		refund.setRefundFlag("2");
		refund.setRefundOrderId("216600000444");
		refund.setChannelName(ChannelEcomType.findByCode("40006006").getValue());
		refund.setTimestamp(System.currentTimeMillis());
		String sign = SignUtil.genSign(refund, "YPEgSbuyRcoDV49yHzx4wS4ZeKHFVQA84Hv4IunjH10");
		refund.setSign(sign);
		System.out.println(JSONArray.toJSONString(refund));
	}*/
	
}
