package com.cn.thinkx.ecom.cash.order.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.cash.order.domain.OrderBack;
import com.cn.thinkx.ecom.cash.order.domain.OrderInf;
import com.cn.thinkx.ecom.cash.order.domain.OrderRedirect;
import com.cn.thinkx.ecom.cash.order.domain.OrderRefund;
import com.cn.thinkx.ecom.cash.order.domain.OrderSearch;
import com.cn.thinkx.ecom.cash.order.domain.OrderUnified;
import com.cn.thinkx.ecom.cash.order.mapper.OrderInfMapper;
import com.cn.thinkx.ecom.cash.order.service.OrderInfService;
import com.cn.thinkx.ecom.cash.order.valid.OrderValid;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelEcomType;
import com.cn.thinkx.ecom.common.constants.Constants.OrderType;
import com.cn.thinkx.ecom.common.constants.Constants.refundFalg;
import com.cn.thinkx.ecom.common.constants.Constants.transRespCode;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.HttpClientUtil;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;

@Service("orderInfService")
public class OrderInfServiceImpl extends BaseServiceImpl<OrderInf> implements OrderInfService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final ExecutorService pool = Executors.newCachedThreadPool();

	// 海易通调用电商加密key
	@Value("${HYT_SIGN_KEY}")
	private String HYT_SIGN_KEY;

	// 知了企服收银台加密key
	@Value("${HKB_SIGN_KEY}")
	private String HKB_SIGN_KEY;

	// 家乐宝调用电商加密key
	@Value("${JLB_SIGN_KEY}")
	private String JLB_SIGN_KEY;

	// 海豚通通兑调用电商加密key
	@Value("${HTTTD_SIGN_KEY}")
	private String HTTTD_SIGN_KEY;

	// 自建电商调用电商加密key
	@Value("${MYESHOP_SIGN_KEY}")
	private String MYESHOP_SIGN_KEY;
	
	// 知了企服收银台地址
	@Value("${HKB_CASH_URL}")
	private String HKB_CASH_URL;

	// 知了企服查单接口地址
	@Value("${HKB_SEARCH_URL}")
	private String HKB_SEARCH_URL;

	// 知了企服退款接口地址
	@Value("${HKB_REFUND_URL}")
	private String HKB_REFUND_URL;

	//电商通知返回地址
	@Value("${ECOM_NOTIFY_URL}")
	private String ECOM_NOTIFY_URL;

	//电商重定向地址
	@Value("${ECOM_REDIRECT_URL}")
	private String ECOM_REDIRECT_URL;

	@Autowired
	private OrderInfMapper orderInfMapper;
	
	@Override
	public List<OrderInf> getList() {
		return this.orderInfMapper.getList();
	}

	@Override
	public OrderInf selectByRouterOrderNo(String routerOrderNo) {
		return this.orderInfMapper.selectByRouterOrderNo(routerOrderNo);
	}

	@Override
	public int insertOrder(OrderInf order) {
		if (StringUtil.isNullOrEmpty(order.getRedirectUrl()))
			order.setRedirectUrl(order.getNotifyUrl());
		return this.orderInfMapper.insert(order);
	}
	
	@Override
	public int updateOrder(OrderInf order) {
		return this.orderInfMapper.updateByPrimaryKey(order);
	}

	@Override
	public String getInsCodeByMchntCode(String mchntCode) {
		return this.orderInfMapper.getInsCodeByMchntCode(mchntCode);
	}

	@Override
	public OrderInf getOrderInfByOrderInf(OrderInf orderInf) {
		return this.orderInfMapper.getOrderInfByOrderInf(orderInf);
	}
	
	@Override
	public String ecomTransOrderUnified(HttpServletResponse resp, OrderUnified orderUnified) throws Exception {
		if (orderUnified == null) {
			logger.error("## 电商平台下单接口--->请求参数为空");
			return null;
		}
		
		if (OrderValid.outOrderUnifiedReqValid(orderUnified))
			return null;
		
		String checkSign = "";
		if (ChannelEcomType.CEU01.getCode().equals(orderUnified.getChannel()))
			checkSign = SignUtil.genSign(orderUnified, HYT_SIGN_KEY);
		else if (ChannelEcomType.CEU02.getCode().equals(orderUnified.getChannel()))
			checkSign = SignUtil.genSign(orderUnified, JLB_SIGN_KEY);
		else if (ChannelEcomType.CEU06.getCode().equals(orderUnified.getChannel()))
			checkSign = SignUtil.genSign(orderUnified, MYESHOP_SIGN_KEY);
		else if (ChannelEcomType.CEU07.getCode().equals(orderUnified.getChannel()))
			checkSign = SignUtil.genSign(orderUnified, HTTTD_SIGN_KEY);
		else {
			logger.error("## 电商平台下单接口--->外部订单{} channel {} 在电商平台系统中不存在", orderUnified.getChannel(), orderUnified.getChannel());
			return null;//TODO 
		}
			
		if (!checkSign.equals(orderUnified.getSign())) {
			logger.error("## 电商平台下单接口--->验签失败，外部订单号{},电商生成签名{}", orderUnified.getOrderId(), checkSign);
			return null; //TODO 验签失败界面
		}
		
		OrderInf orderAdd = new OrderInf();
		orderAdd.setChannel(orderUnified.getChannel());
		orderAdd.setUserId(orderUnified.getUserId());
		orderAdd.setRouterOrderNo(orderUnified.getOrderId());
		orderAdd.setMerchantNo(orderUnified.getInnerMerchantNo());
		orderAdd.setShopNo(orderUnified.getInnerShopNo());
		orderAdd.setCommodityName(orderUnified.getCommodityName());
		orderAdd.setCommodityNum(orderUnified.getCommodityNum());
		orderAdd.setTxnAmount(NumberUtils.RMBCentToYuan(orderUnified.getTxnAmount()));
		orderAdd.setNotifyUrl(orderUnified.getNotify_url());
		orderAdd.setRedirectUrl(orderUnified.getRedirect_url());
		
		OrderInf searchOrder = new OrderInf();
		searchOrder.setRouterOrderNo(orderUnified.getOrderId());
		searchOrder.setChannel(orderUnified.getChannel());
		searchOrder.setOrderType(OrderType.OT02.getCode());
		searchOrder.setDataStat("0");
		OrderInf orderInf = orderInfMapper.getOrderInfByOrderInf(searchOrder);
		if (orderInf != null) {
			logger.error("## 电商平台下单接口--->外部订单号{}已存在支付成功信息", orderUnified.getOrderId());
			return null;
		}
		orderAdd.setOrderType("0");
		if (insertOrder(orderAdd) < 1) {
			logger.error("## 电商平台下单接口--->新增电商平台订单信息失败，外部订单号{}", orderUnified.getOrderId());
			return null;
		}
		/** 按知了企服收银台要求将参数进行加密 */
		OrderUnified hkbOrderUnifiedReq = new OrderUnified();
		hkbOrderUnifiedReq.setAttach(orderUnified.getAttach());
		hkbOrderUnifiedReq.setChannel("40006001");
		hkbOrderUnifiedReq.setCommodityName(orderUnified.getCommodityName());
		hkbOrderUnifiedReq.setCommodityNum(orderUnified.getCommodityNum());
		hkbOrderUnifiedReq.setInnerMerchantNo(orderUnified.getInnerMerchantNo());
		hkbOrderUnifiedReq.setInnerShopNo(orderUnified.getInnerShopNo());
		hkbOrderUnifiedReq.setNotify_url(ECOM_NOTIFY_URL);
		hkbOrderUnifiedReq.setOrderId(orderAdd.getId());
		hkbOrderUnifiedReq.setRedirect_type(orderUnified.getRedirect_type());
		hkbOrderUnifiedReq.setRedirect_url(ECOM_REDIRECT_URL);
		hkbOrderUnifiedReq.setTxnAmount(orderUnified.getTxnAmount());
		hkbOrderUnifiedReq.setUserId(orderUnified.getUserId());
		
		//按知了企服收银台要求对参数进行加密
		String hkbSign = SignUtil.genSign(hkbOrderUnifiedReq, HKB_SIGN_KEY);
		
		try{
			/** 跳转知了企服收银台页面 */
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("<form name='cashSubmit' method='post'  action='" + HKB_CASH_URL + "' >");
			if (!StringUtil.isNullOrEmpty(orderUnified.getAttach()))
				out.println("<input type='hidden' name='attach' value='" + orderUnified.getAttach() + "'>");
			if (!StringUtil.isNullOrEmpty(orderUnified.getRedirect_type()))
				out.println("<input type='hidden' name='redirect_type' value='" + orderUnified.getRedirect_type() + "'>");
			if (!StringUtil.isNullOrEmpty(orderUnified.getRedirect_url()))
				out.println("<input type='hidden' name='redirect_url' value='" + ECOM_REDIRECT_URL + "'>");
			out.println("<input type='hidden' name='channel' value='" + 40006001 + "'>");
			out.println("<input type='hidden' name='commodityName' value='" + orderUnified.getCommodityName() + "'>");
			out.println("<input type='hidden' name='commodityNum' value='" + orderUnified.getCommodityNum() + "'>");
			out.println("<input type='hidden' name='innerMerchantNo' value='" + orderUnified.getInnerMerchantNo() + "'>");
			out.println("<input type='hidden' name='innerShopNo' value='" + orderUnified.getInnerShopNo() + "'>");
			out.println("<input type='hidden' name='notify_url' value='" + ECOM_NOTIFY_URL + "'>");
			out.println("<input type='hidden' name='orderId' value='" + hkbOrderUnifiedReq.getOrderId() + "'>");
			out.println("<input type='hidden' name='sign' value='" + hkbSign + "'>");
			out.println("<input type='hidden' name='txnAmount' value='" + orderUnified.getTxnAmount() + "'>");
			out.println("<input type='hidden' name='userId' value='" + orderUnified.getUserId() + "'>");
			out.println("</form>");
			out.println("<script type='text/javascript'>");
			out.println("document.cashSubmit.submit()");
			out.println("</script>");
		} catch (IOException e) {
			logger.error("## 电商平台下单接口--->外部订单号{} 调用知了企服收银台发生异常{}", orderUnified.getOrderId(), e);
		}
		return null;  //TODO 统一返回错误页面
	}
	
	@Override
	public String ecomTransRedirect(OrderRedirect orderRedirect) throws Exception {
		if (orderRedirect == null) {
			logger.error("## 电商平台重定向接口--->请求参数为空");
			return null;
		}

		if (OrderValid.hkbOrderRedirectValid(orderRedirect))
			return null;

		OrderInf order = orderInfMapper.selectByPrimaryKey(orderRedirect.getOrderId());
		if (StringUtil.isNullOrEmpty(order)) {
			logger.error("## 电商平台重定向接口--->订单号[{}]在电商平台中不存在", orderRedirect.getOrderId());
			return null;
		}

		// 接收知了企服收银台参数进行验签
		String checkSign = SignUtil.genSign(orderRedirect, HKB_SIGN_KEY);
		if (!checkSign.equals(orderRedirect.getSign())) {
			logger.error("## 电商平台重定向接口--->验签失败，订单号{}，电商平台生成签名{}", orderRedirect.getOrderId(), checkSign);
			return null;
		}

		/** 根据第三方要求对参数进行加密处理并重定向至第三方接口 */
		orderRedirect.setChannel(order.getChannel());

		String sign = null;
		if (ChannelEcomType.CEU01.getCode().equals(order.getChannel()))
			sign = SignUtil.genSign(orderRedirect, HYT_SIGN_KEY);
		else if (ChannelEcomType.CEU02.getCode().equals(order.getChannel()))
			sign = SignUtil.genSign(orderRedirect, JLB_SIGN_KEY);
		else if (ChannelEcomType.CEU06.getCode().equals(order.getChannel()))
			sign = SignUtil.genSign(orderRedirect, MYESHOP_SIGN_KEY);
		else if (ChannelEcomType.CEU07.getCode().equals(order.getChannel()))
			sign = SignUtil.genSign(orderRedirect, HTTTD_SIGN_KEY);
		else {
			logger.error("## 电商平台重定向接口--->channel===>{}不存在", order.getChannel());
			return null;
		}
			
		/** 重定向至第三方 */
		StringBuilder url = new StringBuilder();
		url.append(order.getRedirectUrl()).append("?channel=").append(order.getChannel())
		.append("&respResult=").append(orderRedirect.getRespResult())
		.append("&userId=").append(orderRedirect.getUserId())
		.append("&orderId=").append(order.getRouterOrderNo())          
		.append("&txnFlowNo=").append(orderRedirect.getTxnFlowNo())
		.append("&sign=").append(sign);
		if (!StringUtil.isNullOrEmpty(orderRedirect.getAttach()))
			url.append("&attach=").append(orderRedirect.getAttach());

		return url.toString();
	}

	@Override
	public String ecomTransNotify(OrderBack orderBack) throws Exception {
		if (orderBack == null) {
			logger.error("## 电商平台异步回调接口--->请求参数为空");
			return null;
		}
		
		if (OrderValid.hkbOrderBackValid(orderBack))
			return null;
		
		try {
			OrderInf order = orderInfMapper.selectByPrimaryKey(orderBack.getOrderId());
			if (StringUtil.isNullOrEmpty(order)) {
				logger.error("## 电商平台异步回调接口--->订单号{}在电商平台不存在", orderBack.getOrderId());
				return null;
			}
			
			// 对知了企服收银台请求参数验签
			String checkSign = SignUtil.genSign(orderBack, HKB_SIGN_KEY);
			if (!checkSign.equals(orderBack.getSign())) {
				logger.error("## 电商平台异步回调接口--->验签失败，电商订单号{},电商平台生成签名{}", orderBack.getOrderId(), orderBack.getOrderId(), checkSign);
				return null;
			}
			
			OrderInf orderInf = new OrderInf();
			orderInf.setId(order.getId());
			orderInf.setTxnFlowNo(orderBack.getTxnFlowNo());
			if ("SUCCESS".equals(orderBack.getRespResult()))
				orderInf.setOrderType(Constants.OrderType.OT02.getCode());
			else
				orderInf.setOrderType(Constants.OrderType.OT01.getCode());
			
			if (updateOrder(orderInf) < 0)
				logger.error("## 电商平台异步回调接口--->更新电商平台订单 {} 失败", orderBack.getOrderId());
			
			/** 根据第三方要求对参数进行加密 */
			orderBack.setChannel(order.getChannel());
			orderBack.setOrderId(order.getRouterOrderNo());
			
			String sign = null;
			if (ChannelEcomType.CEU01.getCode().equals(order.getChannel()))
				sign = SignUtil.genSign(orderBack, HYT_SIGN_KEY);
			else if (ChannelEcomType.CEU02.getCode().equals(order.getChannel()))
				sign = SignUtil.genSign(orderBack, JLB_SIGN_KEY);
			else if (ChannelEcomType.CEU06.getCode().equals(order.getChannel()))
				sign = SignUtil.genSign(orderBack, MYESHOP_SIGN_KEY);
			else if (ChannelEcomType.CEU07.getCode().equals(order.getChannel()))
				sign = SignUtil.genSign(orderBack, HTTTD_SIGN_KEY);
			else {
				logger.error("## 电商平台异步回调接口--->channel{}不存在", order.getChannel());
				return null;
			}
			
			/** 电商平台异步回调第三方链接参数 */
			StringBuffer url = new StringBuffer();
			url.append("channel=").append(order.getChannel()).append("&respResult=").append(orderBack.getRespResult())
			.append("&innerMerchantNo=").append(orderBack.getInnerMerchantNo()).append("&innerShopNo=").append(orderBack.getInnerShopNo())
			.append("&userId=").append(orderBack.getUserId()).append("&orderId=").append(order.getRouterOrderNo())
			.append("&settleDate=").append(orderBack.getSettleDate()).append("&txnFlowNo=").append(orderBack.getTxnFlowNo())
			.append("&oriTxnAmount=").append(orderBack.getOriTxnAmount()).append("&txnAmount=").append(orderBack.getTxnAmount())
			.append("&sign=").append(sign);
			if (!StringUtil.isNullOrEmpty(orderBack.getAttach()))
				url.append("&attach=").append(orderBack.getAttach());

			logger.info("电商平台异步回调接口--->通知第三方链接[{}]，参数[{}]", order.getNotifyUrl(), url);
			
			// 调用线程异步回调通知第三方并得到返回结果返回至知了企服收银台
			String backMsg = backExtResult(order.getNotifyUrl(), url.toString());
			logger.info("电商平台异步回调接口--->接收第三方返回参数[{}]", backMsg);
			return backMsg;
		} catch (Exception e) {
			logger.error("## 电商平台异步回调接口--->电商流水[{}]异步回调第三方失败", orderBack.getOrderId(), e);
		}
		return null;
	}
	
	/**
	 * 创建线程异步通知第三方并获取返回标志
	 * 
	 * @param url
	 * @return
	 */
	private String backExtResult(String url, String param) {
		String result = null;
		// 只等待5秒，如线程还未结束(还未获取值),将强行终止
		result = invokeExtTimer(url, param, 5);
		return result;
	}

	/**
	 * 创建子线程方法定时异步通知第三方获取返回值， 返回值为空时每隔五秒发起一次异步通知，直到拿到返回值
	 * 
	 * @param pool
	 * @param url
	 * @param timeOut
	 * @return
	 */
	private String invokeExtTimer(final String url, final String param, int timeOut) {
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String msg = "";
				for (int i = 1; i <= 3; i++) {
					logger.info("## 电商平台异步回调--->第[{}]次回调 url[{}] param[{}]", i, url, param);
					msg = HttpClientUtil.sendPostKeyAndValue(url, param);
					if (!StringUtil.isNullOrEmpty(msg))
						break;
				}
				return msg;
			}
		};
		Future<String> future = pool.submit(task);
		String threadResult = null;
		try {
			// 等待计算结果，最长等待timeout秒，timeout秒后中止任务
			threadResult = future.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("## 电商平台异步回调--->等待返回结果异常", e);
		}
		return threadResult;
	}

	@Override
	public String ecomTransOrderQuery(OrderSearch searchOrder) throws Exception {
		OrderSearch searchBack = new OrderSearch();
		searchBack.setRespResult(Constants.HKB_FAIL);
		searchBack.setInfo(transRespCode.transRespCode2.getValue());
		
		if (searchOrder == null) {
			logger.error("## 电商平台查询接口--->请求参数为空");
			searchBack.setInfo("请求参数为空");
			return JSONArray.toJSONString(searchBack);
		}
		
		logger.info("电商平台查单接口--->请求参数{}", searchOrder.toString());
		
		//对第三方请求参数进行参数校验
		 String msg = OrderValid.outSearchReqValid(searchOrder);
		if (!StringUtil.isNullOrEmpty(msg)) {
			logger.error("## 电商平台查单接口--->参数校验失败===>{}", msg);
			searchBack.setInfo(msg);
			return JSONArray.toJSONString(searchBack);
		}

		/** 对第三方请求参数进行验签 */
		String checkSign = "";
		if (ChannelEcomType.CEU01.getCode().equals(searchOrder.getChannel()))
			checkSign = SignUtil.genSign(searchOrder, HYT_SIGN_KEY);
		else if (ChannelEcomType.CEU02.getCode().equals(searchOrder.getChannel()))
			checkSign = SignUtil.genSign(searchOrder, JLB_SIGN_KEY);
		else if (ChannelEcomType.CEU06.getCode().equals(searchOrder.getChannel()))
			checkSign = SignUtil.genSign(searchOrder, MYESHOP_SIGN_KEY);
		else if (ChannelEcomType.CEU07.getCode().equals(searchOrder.getChannel()))
			checkSign = SignUtil.genSign(searchOrder, HTTTD_SIGN_KEY);
		else {
			logger.error("## 电商平台查单接口--->channel {} 在系统中不存在", searchOrder.getChannel());
			return JSONArray.toJSONString(searchBack);
		}
		if (!checkSign.equals(searchOrder.getSign())) {
			logger.error("## 电商平台查单接口--->参数验签失败，外部订单号[{}]，电商平台生成签名[{}]", searchOrder.getOrderId(), searchOrder.getSign(), checkSign);
			searchBack.setInfo("验签失败");
			return JSONArray.toJSONString(searchBack);
		}

		//查询第三方订单在电商平台流水中是否存在
		OrderInf order = new OrderInf();
		order.setChannel(searchOrder.getChannel());
		order.setRouterOrderNo(searchOrder.getOrderId());
		order.setTxnFlowNo(searchOrder.getTxnFlowNo());
		order.setDataStat("0");
		OrderInf orderInf = orderInfMapper.getOrderInfByOrderInf(order);
		if (orderInf == null) {
			logger.error("## 电商平台查单接口--->外部订单号[{}]在电商平台交易订单中不存在", searchOrder.getOrderId());
			searchBack.setInfo("交易订单不存在");
			return JSONArray.toJSONString(searchBack);
		}
		order.setId(orderInf.getId());

		/** 按知了企服查单接口要求对参数进行加密 */
		OrderSearch hkbSearchReq = new OrderSearch();
		hkbSearchReq.setChannel("40006001");
		hkbSearchReq.setOrderId(order.getId());
		hkbSearchReq.setUserId(searchOrder.getUserId());
		hkbSearchReq.setTxnFlowNo(searchOrder.getTxnFlowNo());
		String hkbSign = SignUtil.genSign(hkbSearchReq, HKB_SIGN_KEY);
		
		//拼接调用知了企服查单接口参数
		StringBuffer hkbReq = new StringBuffer();
		hkbReq.append("channel=").append(hkbSearchReq.getChannel())
			.append("&userId=").append(hkbSearchReq.getUserId())
			.append("&sign=").append(hkbSign);
		if (!StringUtil.isNullOrEmpty(searchOrder.getOrderId())) {
			hkbReq.append("&orderId=").append(hkbSearchReq.getOrderId());
		}
		if (!StringUtil.isNullOrEmpty(searchOrder.getTxnFlowNo())) {
			hkbReq.append("&txnFlowNo=").append(hkbSearchReq.getTxnFlowNo());
		}
		//http post请求知了企服查单接口
		logger.info("电商平台查单接口--->请求知了企服查询接口链接[{}]，参数{}",HKB_SEARCH_URL, hkbReq.toString());
		String json = HttpClientUtil.sendPost(HKB_SEARCH_URL, hkbReq.toString());
		logger.info("电商平台查单接口--->知了企服返回查询结果[{}]", json);
		if (StringUtil.isNullOrEmpty(json)) {
			logger.error("## 电商平台查单接口--->知了企服查询接口返回参数为空,电商订单号{},txnFlowNo{}", hkbSearchReq.getOrderId(), searchOrder.getTxnFlowNo());
			return JSONArray.toJSONString(searchBack);
		}
		
		OrderSearch hkbSearchResp = JSONArray.parseObject(json, OrderSearch.class);
		if (Constants.HKB_FAIL.toString().equalsIgnoreCase(hkbSearchResp.getRespResult()) && StringUtil.isNullOrEmpty(hkbSearchResp.getSign())) {
			searchBack.setInfo(hkbSearchResp.getInfo());
			return JSONArray.toJSONString(searchBack);
		}
		
		//对知了企服查单接口返回参数进行验签
		String hkbCheckSign = SignUtil.genSign(hkbSearchResp, HKB_SIGN_KEY);
		if (!hkbCheckSign.equals(hkbSearchResp.getSign())) {
			logger.error("## 电商平台查单接口--->接收知了企服查单接口返回参数验签失败，电商订单号[{}],电商平台生成签名[{}]", hkbSearchReq.getOrderId(), hkbCheckSign);
			return JSONArray.toJSONString(searchBack);
		}

		/** 对参数进行加密返回至第三方  */
		hkbSearchResp.setChannel(searchOrder.getChannel());
		searchBack.setUserId(hkbSearchResp.getUserId());
		searchBack.setOrderId(searchOrder.getOrderId());
		searchBack.setRespResult(hkbSearchResp.getRespResult());
		searchBack.setInnerMerchantNo(hkbSearchResp.getInnerMerchantNo());
		searchBack.setInnerShopNo(hkbSearchResp.getInnerShopNo());
		searchBack.setSettleDate(hkbSearchResp.getSettleDate());
		searchBack.setTxnFlowNo(hkbSearchResp.getTxnFlowNo());
		searchBack.setOriTxnAmount(hkbSearchResp.getOriTxnAmount());
		searchBack.setTxnAmount(hkbSearchResp.getTxnAmount());
		searchBack.setAttach(hkbSearchResp.getAttach());
		searchBack.setInfo("");
		
		String sign = null;
		if (ChannelEcomType.CEU01.getCode().equals(searchOrder.getChannel()))
			sign = SignUtil.genSign(searchBack, HYT_SIGN_KEY);
		else if (ChannelEcomType.CEU02.getCode().equals(searchOrder.getChannel()))
			sign = SignUtil.genSign(searchBack, JLB_SIGN_KEY);
		else if (ChannelEcomType.CEU06.getCode().equals(searchOrder.getChannel()))
			sign = SignUtil.genSign(searchBack, MYESHOP_SIGN_KEY);
		else if (ChannelEcomType.CEU07.getCode().equals(searchOrder.getChannel()))
			sign = SignUtil.genSign(searchBack, HTTTD_SIGN_KEY);
		else {
			logger.error("## 电商平台查单接口--->外部订单 {} 的 channel {} 在电商平台系统中不存在", searchOrder.getOrderId(), searchOrder.getChannel());
			return JSONArray.toJSONString(searchBack);
		}
		searchBack.setSign(sign);	
		logger.info("电商平台查单接口--->返回参数{}", JSONArray.toJSONString(searchBack));
		return JSONArray.toJSONString(searchBack);
	}

	@Override
	public String ecomTransOrderRefund(OrderRefund orderRefund) throws Exception {
		if (orderRefund == null) {
			logger.error("## 电商平台退款接口--->请求参数为空");
			return null;
		}
		
		OrderRefund refundBack = new OrderRefund();
		refundBack.setCode(transRespCode.transRespCode2.getCode());
		refundBack.setMsg(transRespCode.transRespCode2.getValue());
		
		//参数校验
		String refundMsg = OrderValid.outRefundReqValid(orderRefund);
		if (!StringUtil.isNullOrEmpty(refundMsg)) {
			logger.error("## 电商平台退款接口--->参数校验==>{}", refundMsg);
			refundBack.setMsg(refundMsg);
			return JSONArray.toJSONString(refundBack);
		}
		
		//验签
		String checkSign = "";
		if (ChannelEcomType.CEU01.getCode().equals(orderRefund.getChannel()))
			checkSign = SignUtil.genSign(orderRefund, HYT_SIGN_KEY);
		else if (ChannelEcomType.CEU02.getCode().equals(orderRefund.getChannel()))
			checkSign = SignUtil.genSign(orderRefund, JLB_SIGN_KEY);
		else if (ChannelEcomType.CEU06.getCode().equals(orderRefund.getChannel()))
			checkSign = SignUtil.genSign(orderRefund, MYESHOP_SIGN_KEY);
		else if (ChannelEcomType.CEU07.getCode().equals(orderRefund.getChannel()))
			checkSign = SignUtil.genSign(orderRefund, HTTTD_SIGN_KEY);
		else {
			logger.error("## 电商平台退款接口--->暂不支持 渠道channel===>{}退款", orderRefund.getChannel());
			refundBack.setMsg("channel值不正确");
			return JSONArray.toJSONString(refundBack);
		}
		if (!checkSign.equals(orderRefund.getSign())) {
			logger.error("##电商平台退款接口--->验签失败，外部退款订单号[{}],电商生成签名[{}]", orderRefund.getRefundOrderId(), orderRefund.getSign(), checkSign);
			refundBack.setMsg("验签失败");
			return JSONArray.toJSONString(refundBack);
		}
		
		OrderInf order = new OrderInf();
		order.setRouterOrderNo(orderRefund.getOriOrderId());
		order.setChannel(orderRefund.getChannel());
		order.setDataStat("0");
		//根据原交易流水查询原交易是否存在
		OrderInf old_orderInf = orderInfMapper.getOrderInfByOrderInf(order);
		if (old_orderInf == null) {
			logger.error("## 电商平台退款接口--->外部原交易订单号[{}]在电商平台订单信息中不存在", orderRefund.getOriOrderId());
			refundBack.setMsg("原交易订单不存在");
			return JSONArray.toJSONString(refundBack);
		}
		
		//根据原交易流水查询交易是否成功
		if (!old_orderInf.getOrderType().equals(OrderType.OT02.getCode())) {
			logger.error("## 电商平台退款接口--->外部原交易订单号[{}]在电商平台中交易未成功", orderRefund.getOriOrderId());
			refundBack.setMsg("原交易订单为支付失败");
			return JSONArray.toJSONString(refundBack);
		}
		
		//根据退款流水查询是否存在退款成功的信息
		order.setRouterOrderNo(orderRefund.getRefundOrderId());
		order.setOrderType(OrderType.OT04.getCode());
		OrderInf refund_orderInf = orderInfMapper.getOrderInfByOrderInf(order);
		if (refund_orderInf != null) {
			logger.error("## 电商平台退款接口--->外部退款订单号[{}]在电商平台已有退款成功信息", orderRefund.getRefundOrderId());
			refundBack.setMsg("退款订单号重复");
			return JSONArray.toJSONString(refundBack);
		}
		
		//新增电商平台退款订单信息
		OrderInf o = new OrderInf();
		o.setChannel(old_orderInf.getChannel());
		o.setUserId(old_orderInf.getUserId());
		o.setRouterOrderNo(orderRefund.getRefundOrderId());
		o.setMerchantNo(old_orderInf.getMerchantNo());
		o.setShopNo(old_orderInf.getShopNo());
		o.setCommodityName(old_orderInf.getCommodityName());
		o.setCommodityNum(old_orderInf.getCommodityNum());
		o.setTxnAmount(NumberUtils.RMBCentToYuan(orderRefund.getRefundAmount()));
		o.setOrderType(OrderType.OT03.getCode());
		o.setNotifyUrl(old_orderInf.getNotifyUrl());
		o.setRedirectUrl(old_orderInf.getRedirectUrl());
		o.setOrgId(old_orderInf.getId());
		try {
			if (insertOrder(o) < 0) {
				logger.error("## 电商平台退款接口--->新增电商平台退款订单信息失败，外部退款订单号{}", orderRefund.getRefundOrderId());
				return JSONArray.toJSONString(refundBack);
			}
		} catch (Exception e) {
			logger.error("## 电商平台退款接口--->新增电商平台退款订单信息异常{}，外部退款订单号{}", e, orderRefund.getRefundOrderId());
			return JSONArray.toJSONString(refundBack);
		}
		
		//查询电商平台退款订单信息
		OrderInf orderInf = new OrderInf();
		orderInf.setRouterOrderNo(o.getRouterOrderNo());
		orderInf.setChannel(o.getChannel());
		orderInf.setDataStat("0");
		OrderInf refundOrderInf = orderInfMapper.getOrderInfByOrderInf(orderInf);
		if (refundOrderInf == null) {
			logger.error("## 电商平台退款接口--->查询电商平台退款订单信息为空，外部退款订单号{}", orderRefund.getRefundOrderId());
			return JSONArray.toJSONString(refundBack);
		}
		
		//封装知了企服退款接口所需参数
		OrderRefund hkbOrderRefund = new OrderRefund();
		hkbOrderRefund.setRefundOrderId(refundOrderInf.getId());
		hkbOrderRefund.setOriOrderId(old_orderInf.getId());
		hkbOrderRefund.setChannel("40006001");
		hkbOrderRefund.setRefundAmount(orderRefund.getRefundAmount());
		hkbOrderRefund.setChannelName(ChannelEcomType.findByCode(orderRefund.getChannel()).getValue());
		if (refundFalg.refundFalg1.getCode().equals(orderRefund.getRefundFlag())) {
			hkbOrderRefund.setRefundFlag(refundFalg.refundFalg1.getCode());
		} else {
			hkbOrderRefund.setRefundFlag(refundFalg.refundFalg2.getCode());
		}
		hkbOrderRefund.setTimestamp(System.currentTimeMillis());
		String sign = SignUtil.genSign(hkbOrderRefund, HKB_SIGN_KEY);
		hkbOrderRefund.setSign(sign);
		
		//请求知了企服退款接口
		logger.info("电商平台退款接口--->请求知了企服退款接口地址===>{},请求参数===>{}", HKB_REFUND_URL, JSONObject.toJSONString(hkbOrderRefund));
		String json = HttpClientUtil.sendPost(HKB_REFUND_URL, JSONObject.toJSONString(hkbOrderRefund));
		if (StringUtil.isNullOrEmpty(json)) {
			logger.error("## 电商平台退款接口--->请求知了企服退款接口,返回参数为空，电商退款订单号 {}", hkbOrderRefund.getRefundOrderId());
			return JSONArray.toJSONString(refundBack);
		}
		logger.info("电商平台退款接口--->请求知了企服退款接口,返回参数{}", json);
		
		//转换知了企服退款接口返回参数
		OrderRefund hkbRefundResp = JSONArray.parseObject(json, OrderRefund.class);
		refundBack.setTxnFlowNo(hkbRefundResp.getTxnFlowNo());
		refundOrderInf.setTxnFlowNo(hkbRefundResp.getTxnFlowNo());
		if (transRespCode.transRespCode1.getCode().equals(hkbRefundResp.getCode())) {
			refundBack.setCode(transRespCode.transRespCode1.getCode());
			refundBack.setMsg(transRespCode.transRespCode1.getValue());
			refundOrderInf.setOrderType(OrderType.OT04.getCode());
		} else if (transRespCode.transRespCode2.getCode().equals(hkbRefundResp.getCode())) {
			logger.info("电商平台退款接口--->请求知了企服退款接口,外部退款订单号 {} 退款失败", orderRefund.getRefundOrderId());
			refundOrderInf.setOrderType(OrderType.OT03.getCode());
		} else {
			logger.info("电商平台退款接口--->请求知了企服退款接口,外部退款订单号 {} 退款失败", orderRefund.getRefundOrderId());
			refundBack.setCode(transRespCode.transRespCode3.getCode());
			refundBack.setMsg(hkbRefundResp.getMsg());
			refundOrderInf.setOrderType(OrderType.OT03.getCode());
		}
		
		//根据知了企服退款接口返回参数更新电商平台订单信息
		if (updateOrder(refundOrderInf) < 1) {
			logger.error("## 电商平台退款接口--->更新电商平台退款订单信息失败，外部退款订单号{}", orderRefund.getRefundOrderId());
		}
		logger.info("电商平台退款接口--->返回参数{}", JSONArray.toJSONString(refundBack));
		return JSONArray.toJSONString(refundBack);
	}

}
