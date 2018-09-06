package com.cn.thinkx.ecom.cash.order.controller;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.beans.CtrlSystem;
import com.cn.thinkx.beans.TxnPackageBean;
import com.cn.thinkx.ecom.cash.activemq.service.WechatMQProducerService;
import com.cn.thinkx.ecom.cash.order.domain.OrderInf;
import com.cn.thinkx.ecom.cash.order.domain.jiafu.JFExtandJson;
import com.cn.thinkx.ecom.cash.order.domain.jiafu.OrderJFRefund;
import com.cn.thinkx.ecom.cash.order.domain.jiafu.OrderJFUnified;
import com.cn.thinkx.ecom.cash.order.service.OrderInfService;
import com.cn.thinkx.ecom.cash.order.valid.JFReqValid;
import com.cn.thinkx.ecom.cash.redis.utils.RedisDictProperties;
import com.cn.thinkx.ecom.cash.redis.utils.SignatureUtil;
import com.cn.thinkx.ecom.cash.shop.domain.ShopInf;
import com.cn.thinkx.ecom.cash.shop.service.ShopInfService;
import com.cn.thinkx.ecom.cash.wxtrans.domain.IntfaceTransLog;
import com.cn.thinkx.ecom.cash.wxtrans.domain.TransLog;
import com.cn.thinkx.ecom.cash.wxtrans.domain.TxnResp;
import com.cn.thinkx.ecom.cash.wxtrans.domain.UserMerchantAcct;
import com.cn.thinkx.ecom.cash.wxtrans.service.CtrlSystemService;
import com.cn.thinkx.ecom.cash.wxtrans.service.MessageService;
import com.cn.thinkx.ecom.cash.wxtrans.service.TransLogService;
import com.cn.thinkx.ecom.cash.wxtrans.service.UserMerchantAcctService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelCode;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelEcomType;
import com.cn.thinkx.ecom.common.constants.Constants.OrderType;
import com.cn.thinkx.ecom.common.constants.Constants.TransCode;
import com.cn.thinkx.ecom.common.constants.Constants.templateMsgPayment;
import com.cn.thinkx.ecom.common.constants.Constants.templateMsgRefund;
import com.cn.thinkx.ecom.common.constants.Constants.transType;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.service.txn.Java2TxnBusinessFacade;
import com.cn.thinkx.wechat.base.wxapi.util.WXTemplateUtil;

/**
 * 此类针对嘉福京东商城开发的对接接口
 * 
 */
@RestController
@RequestMapping(value = "order")
public class JFOrderInfController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WebApplicationContext context; // 获取上下文

	// 电商调用知了企服收银台加密key
	@Value("${HKB_SIGN_KEY}")
	private String HKB_SIGN_KEY;

	// 嘉福京东调用电商平台加密key
	@Value("${JF_SIGN_KEY}")
	private String JF_SIGN_KEY;

	@Autowired
	private OrderInfService orderInfService;
	
	@Autowired
	private ShopInfService shopInfService;
	
	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private CtrlSystemService ctrlSystemService;
	
	@Autowired
	private UserMerchantAcctService userMerchantAcctService;
	
//	@Autowired
//	private HKBTxnFacade hkbTxnFacade;
	
	@Autowired
	private Java2TxnBusinessFacade java2TxnBusinessFacade;

	@Autowired
	private WechatMQProducerService wechatMQProducerService;
	
	@Autowired
	private  MessageService messageService;

	/**
	 * 知了企服电商平台下单接口
	 * 
	 * @param req
	 * @param resp
	 * @param jforder
	 * @return
	 */
	@RequestMapping(value = "/transOrder")
	public String transOrder(HttpServletRequest req, HttpServletResponse resp, OrderJFUnified jforder) {
		OrderJFUnified jfback = new OrderJFUnified();
		jfback.setCode(transType.T99.getCode());
		jfback.setErrmsg(transType.T99.getValue());
		
		/****参数校验**/
		if (JFReqValid.transReqValid(jforder))
			return JSONArray.toJSONString(jfback);
		
		//将extand_params拓展参数 URLDecoder
		String extandParams = "";
		try {
			extandParams = URLDecoder.decode(jforder.getExtand_params(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("## 嘉福--->下单接口，参数extand_params进行urldecode出错，urldecode前为[{}]", jforder.getExtand_params());
			return JSONArray.toJSONString(jfback);
		}
		//将拓展参数值转成对象
		JFExtandJson jfExtParams = JSON.parseObject(extandParams, JFExtandJson.class);
		jforder.setExtand_params(JSONArray.toJSONString(jfExtParams));
		//校验userID是否一致
		if (!jforder.getE_uid().equals(jfExtParams.getUserId())) {
			logger.error("## 嘉福--->下单接口，参数e_uid与拓展参数中userID不一致");
			return JSONArray.toJSONString(jfback);
		}
		//校验签名
		if (!SignUtil.genSign(jforder, JF_SIGN_KEY).equals(jforder.getSign())) {
			logger.error("## 嘉福--->下单接口，参数校验失败[签名错误]");
			return JSONArray.toJSONString(jfback);
		}
		
		jfback.setTrade_no(jforder.getTrade_no());
		jfback.setTotal_amount(jforder.getTotal_amount());
		
		//根据userID查询出externalId
		UserMerchantAcct userExternalId = new UserMerchantAcct();
		userExternalId.setUserId(jforder.getE_uid());
		userExternalId.setChannelCode(Constants.ChannelCode.CHANNEL1.toString());
		String externalId = userMerchantAcctService.getExternalId(userExternalId);
		if (StringUtil.isNullOrEmpty(externalId)) {
			logger.error("## 嘉福--->下单接口，用户[{}]在知了企服账户系统中不存在", jforder.getE_uid());
			return JSONArray.toJSONString(jfback); 
		}
		
		OrderInf order = new OrderInf();
		if ((jfExtParams.getEcomChnl()).equals(Constants.ChannelEcomType.CEU03.getCode())) {
			order.setChannel(Constants.ChannelEcomType.CEU03.getCode());
			order.setCommodityName("嘉福京东订单");
			order.setNotifyUrl("jfNotifyUrl");
			order.setRedirectUrl("jfRedirectUrl");
		} else if ((jfExtParams.getEcomChnl()).equals(Constants.ChannelEcomType.CEU04.getCode())) {
			order.setChannel(Constants.ChannelEcomType.CEU04.getCode());
			order.setCommodityName("美团订单");
			order.setNotifyUrl("MeiTuanNotifyUrl");
			order.setRedirectUrl("MeiTuanRedirectUrl");
		} else if ((jfExtParams.getEcomChnl()).equals(Constants.ChannelEcomType.CEU05.getCode())) {
			order.setChannel(Constants.ChannelEcomType.CEU05.getCode());
			order.setCommodityName("大众点评订单");
			order.setNotifyUrl("DianPingNotifyUrl");
			order.setRedirectUrl("DianPingRedirectUrl");
		} else {
			logger.error("## 嘉福--->下单接口，扩展参数extand_params[{}]中电商标识在知了企服系统中不存在", jforder.getExtand_params());
			return JSONArray.toJSONString(jfback); 
		}
		order.setUserId(jforder.getE_uid());
		order.setRouterOrderNo(jforder.getTrade_no());
		order.setCommodityNum("1");
		order.setMerchantNo(jfExtParams.getMchntCode());
		order.setShopNo(jfExtParams.getShopCode());
		order.setTxnAmount(NumberUtils.RMBCentToYuan(jforder.getTotal_amount()));
		OrderInf o = orderInfService.selectByRouterOrderNo(jforder.getTrade_no());
		if (o != null) {
			jfback.setCode(transType.T10.getCode());
			jfback.setErrmsg(transType.T10.getValue());
			logger.error("## 嘉福--->下单接口，支付重复，嘉福流水号[{}]", jforder.getTrade_no());
			return JSONArray.toJSONString(jfback);
		} else {
			order.setOrderType("0");
			if (orderInfService.insertOrder(order) < 0) {
				logger.error("## 嘉福--->下单接口，电商平台新增订单信息失败，嘉福流水号[{}]", jforder.getTrade_no());
				return JSONArray.toJSONString(jfback);
			}
		}
		//调用知了企服收银台接口进行会员卡扣款（接收返回值并返回给嘉福京东）
		CtrlSystem cs = null;
		try{
			cs = ctrlSystemService.getCtrlSystem();//得到日切信息
		} catch (Exception e) {
			logger.error("## 嘉福--->下单接口，日切异常{}", e);
		} 
		if (cs != null) {
			if (!Constants.TRANS_FLAG_YES.equals(cs.getTransFlag())) {
				logger.error("## 嘉福--->下单接口，日切信息交易允许状态：日切中");
				return JSONArray.toJSONString(jfback);
			}
		} else {
			logger.error("## 嘉福--->下单接口，日切信息为空");
			return JSONArray.toJSONString(jfback);
		}
		String insCode = orderInfService.getInsCodeByMchntCode(jfExtParams.getMchntCode());
		//将信息按照知了企服收银台接口进行封装
		TxnPackageBean txnBean = new TxnPackageBean();
		txnBean.setTxnType("W10" + "0");// 交易类型，发送报文时补0
		txnBean.setSwtTxnDate(DateUtil.getCurrentDateStr());// 交易日期
		txnBean.setSwtTxnTime(DateUtil.getCurrentTimeStr());// 交易时间
		txnBean.setSwtSettleDate(cs.getSettleDate());// 清算日期
		txnBean.setSwtFlowNo(jforder.getTrade_no());
		txnBean.setIssChannel(insCode);// 机构号
		txnBean.setInnerMerchantNo(jfExtParams.getMchntCode());// 商户号
		txnBean.setInnerShopNo(jfExtParams.getShopCode());// 门店号
		txnBean.setTxnAmount(jforder.getTotal_amount());// 交易金额
		txnBean.setOriTxnAmount(jforder.getTotal_amount());// 原交易金额
		txnBean.setChannel("40006001");// 渠道号
		txnBean.setCardNo("U" + externalId);// 卡号 U开头为客户端交易，C开头则为刷卡交易
		txnBean.setPinTxn("");// 交易密码
		txnBean.setUserId(jforder.getE_uid());// 用户id
		txnBean.setTimestamp(System.currentTimeMillis());// 时间戳
		txnBean.setResv6("1"); // 表示不需要输入密码
		String signature = SignUtil.genSign(txnBean, HKB_SIGN_KEY); //生成的签名
		txnBean.setSignature(signature);
		String json = new String();
//		logger.info("电商平台--->调用知了企服会员卡消费接口请求参数为[{}]", JSONArray.toJSONString(txnBean));
		try {
			//调用知了企服接口（对用户的会员卡进行扣款），得到返回值
			json = java2TxnBusinessFacade.consumeTransactionITF(txnBean);
		} catch (Exception e) {
			logger.error("## 嘉福--->下单接口，调用知了企服消费接口异常，嘉福流水号为[{}]", txnBean.getSwtFlowNo(), e);
		}
		logger.info("嘉福--->下单接口，接收知了企服消费接口返回参数[{}]", json);
		
		//接收知了企服消费接口返回值，判断是否为空,并调用查询接口判断
		TxnResp re = JSONArray.parseObject(json, TxnResp.class);
		if (StringUtil.isNullOrEmpty(re)) {
			try {
				json = java2TxnBusinessFacade.transExceptionQueryITF(jforder.getTrade_no());
			} catch (Exception e) {
				logger.error("## 嘉福--->下单接口，调用知了企服查询接口失败，嘉福流水号[{}]", jforder.getTrade_no(), e);
			}
			logger.info("嘉福--->下单接口，接收知了企服查询接口返回参数为[{}]", json);
			re = JSONArray.parseObject(json, TxnResp.class);
		}
		jfback.setOut_trade_no(re.getInterfacePrimaryKey());
		
		//根据知了企服返回值修改数据库数据，并将值返回给嘉福京东
		OrderInf or = orderInfService.selectByRouterOrderNo(jforder.getTrade_no());
		or.setTxnFlowNo(re.getInterfacePrimaryKey());
		if (Constants.MiddlewareType.MT00.getCode().equals(re.getCode())) {
			jfback.setCode(transType.T00.getCode());
			jfback.setErrmsg(transType.T00.getValue());
			or.setOrderType(Constants.OrderType.OT02.getCode());
		} else if (Constants.MiddlewareType.MT51.getCode().equals(re.getCode())) {
			jfback.setCode(transType.T08.getCode());
			jfback.setErrmsg(transType.T08.getValue());
			or.setOrderType(Constants.OrderType.OT01.getCode());
		} else {
			jfback.setCode(transType.T99.getCode());
			jfback.setErrmsg(transType.T99.getValue());
			or.setOrderType(Constants.OrderType.OT01.getCode());
		}
		
		if (orderInfService.updateOrder(or) < 0) {
			logger.error("## 嘉福--->下单接口，电商平台修改订单信息失败，电商平台订单号[{}]", or.getId());
			return JSONArray.toJSONString(jfback);
		}
		logger.info("嘉福--->下单接口，修改电商平台订单[{}]信息状态[{}]", or.getId(), or.getOrderType());
		
		//交易成功，发送模板消息
		if (Constants.MiddlewareType.MT00.getCode().equals(re.getCode())) {
			try{
				String payAmt = NumberUtils.RMBCentToYuan(jforder.getTotal_amount());
				ShopInf shopInf = shopInfService.getShopInfByCode(jfExtParams.getShopCode());
				String txnDate = null;
				try {
					txnDate = DateUtil.getChineseDateFormStr(txnBean.getSwtTxnDate() + txnBean.getSwtTxnTime());
				} catch (Exception e) {
					logger.error("## 嘉福--->下单接口，调用知了企服消费模板消息传参出错（时间计算异常） ", e);
				}
				UserMerchantAcct accBal = new UserMerchantAcct();
				accBal.setExternalId(externalId);
				accBal.setMchntCode(jfExtParams.getMchntCode());
				accBal.setChannelCode(ChannelCode.CHANNEL1.toString());
				String userBal = userMerchantAcctService.getAccBalance(accBal); //TODO 暂时从数据库直接查，后面改成调用接口查询卡余额
				//发送模板消息
				wechatMQProducerService.sendTemplateMsg(RedisDictProperties.getInstance(context).getdictValueByCode("WX_CUSTOMER_ACCOUNT", context), externalId, "WX_TEMPLATE_ID_0", null, 
						WXTemplateUtil.setHKBPayData(txnDate, shopInf.getMchntName(), shopInf.getShopName(), payAmt, userBal, ChannelEcomType.findByCode(or.getChannel()).getValue()));
			}catch(Exception e){
				logger.error("## 嘉福--->下单接口，发送客服消息接口异常: ITF流水号：", re.getInterfacePrimaryKey(), e);
			}
		}
		logger.info("嘉福--->下单接口，电商平台下单接口返回参数[{}]", JSONArray.toJSONString(jfback));
		return JSONArray.toJSONString(jfback);
	}
	
	/**
	 * 知了企服电商平台查单接口
	 * 
	 * @param req
	 * @param resp
	 * @param jforder
	 * @return
	 */
	@RequestMapping(value = "/searchOrder")
	public String searchOrder(HttpServletRequest req, HttpServletResponse resp, OrderJFRefund searchOrder){
		OrderJFRefund searchback = new OrderJFRefund();
		searchback.setCode(transType.T99.getCode());
		searchback.setErrmsg(transType.T99.getValue());
		
		/****参数校验**/
		if (JFReqValid.searchReqValid(searchOrder, JF_SIGN_KEY))
			return JSONArray.toJSONString(searchback);
		
		searchback.setTrade_no(searchOrder.getTrade_no());
		
		//远程调用查单接口
		String json = new String();
		try {
			json = java2TxnBusinessFacade.transExceptionQueryITF(searchOrder.getTrade_no());
		} catch (Exception e) {
			logger.error("## 嘉福--->查询接口，调用知了企服查单接口失败：嘉福流水号[{}]", searchOrder.getTrade_no());
		}
		if (StringUtil.isNullOrEmpty(json)) {
			logger.error("## 嘉福--->查询接口，调用知了企服查单接口，接收返回参数为空");
			return JSONArray.toJSONString(searchback);
		}
		logger.info("嘉福--->查询接口，调用知了企服查单接口，接收返回参数[{}]", json.toString());
		TxnResp txnResp = JSONArray.parseObject(json, TxnResp.class);
		if (!StringUtil.isNullOrEmpty(txnResp.getSwtFlowNo())) {
			searchback.setRefund_amount(txnResp.getTransAmt());
			searchback.setRefund_falg("1");
		} else {
			searchback.setRefund_falg("0");
		}
		searchback.setOut_trade_no(txnResp.getSwtFlowNo());
		searchback.setTotal_amount(txnResp.getTransAmt());
		searchback.setCode(txnResp.getCode());
		searchback.setErrmsg(txnResp.getInfo());
		logger.info("嘉福--->查询接口，电商平台查单接口返回参数[{}]", JSONArray.toJSONString(searchback));
		return JSONArray.toJSONString(searchback);
	}

	/**
	 * 退款接口
	 * 
	 * @param req
	 * @param resp
	 * @param refund
	 * @return
	 */
	@RequestMapping("/refundOrder")
	public String refundOrder(HttpServletRequest req, HttpServletResponse resp, OrderJFRefund refund){
		OrderJFRefund refundback = new OrderJFRefund();
		refundback.setCode(transType.T99.getCode());
		refundback.setErrmsg(transType.T99.getValue());
		
		/****参数校验**/
		if (JFReqValid.refundReqValid(refund, JF_SIGN_KEY))
			return JSONArray.toJSONString(refundback);
		
		refundback.setTrade_no(refund.getTrade_no());
		
		//根据嘉福流水号查询电商平台订单号原交易订单信息（主键）
		OrderInf oriOrder = orderInfService.selectByRouterOrderNo(refund.getOld_trade_no());
		if (oriOrder == null) {
			logger.error("## 嘉福--->退款接口，嘉福原交易流水号[{}]在电商平台交易订单信息中不存在", refund.getOld_trade_no());
			refundback.setCode(transType.T09.getCode());
			refundback.setErrmsg(transType.T09.getValue());
			return JSONArray.toJSONString(refundback);
		}
		
		if (!oriOrder.getOrderType().equals(OrderType.OT02.getCode())) {
			logger.error("## 嘉福--->退款接口，原交易订单号[{}]在电商平台中交易未成功", refund.getOld_trade_no());
			return JSONArray.toJSONString(refundback);
		}
		
		//查询嘉福退款流水在电商订单信息中是否已存在
		OrderInf order = orderInfService.selectByRouterOrderNo(refund.getTrade_no());
		if (StringUtil.isNullOrEmpty(order)) {
			OrderInf o = new OrderInf();
			o.setChannel(oriOrder.getChannel());
			o.setUserId(oriOrder.getUserId());
			o.setRouterOrderNo(refund.getTrade_no());
			o.setMerchantNo(oriOrder.getMerchantNo());
			o.setShopNo(oriOrder.getShopNo());
			o.setCommodityName(oriOrder.getCommodityName());
			o.setCommodityNum(oriOrder.getCommodityNum());
			o.setTxnAmount(NumberUtils.RMBCentToYuan(refund.getRefund_amount()));
			o.setOrderType(OrderType.OT03.getCode());
			o.setNotifyUrl(oriOrder.getNotifyUrl());
			o.setRedirectUrl(oriOrder.getRedirectUrl());
			o.setOrgId(oriOrder.getId());
			try {
				if (orderInfService.insertOrder(o) < 0) {
					logger.error("## 嘉福--->退款接口，电商平台生成退款订单信息失败，嘉福退款流水[{}]", refund.getTrade_no());
					return JSONArray.toJSONString(refundback);
				}
			} catch (Exception e) {
				logger.error("## 嘉福--->退款接口，电商平台生成退款订单信息异常，嘉福退款流水[{}]", refund.getTrade_no(), e);
			}
		} else {
			if (order.getOrderType().equals(OrderType.OT04.getCode())) {
				logger.error("## 嘉福--->退款接口，退款重复，嘉福退款流水[{}]", refund.getTrade_no());
				refundback.setCode(transType.T10.getCode());
				refundback.setErrmsg(transType.T10.getValue());
				return JSONArray.toJSONString(refundback);
			}
		}
		
		//根据嘉福流水号查询itf流水主键
		IntfaceTransLog itf = transLogService.getIntfaceTransLogByDmsId(refund.getOld_trade_no());
		if (itf == null) {
			logger.error("## 嘉福--->退款接口，退款失败，嘉福原交易流水[{}]在InterfaceTransLog中不存在", refund.getOld_trade_no());
			refundback.setCode(transType.T09.getCode());
			refundback.setErrmsg(transType.T09.getValue());
			return JSONArray.toJSONString(refundback);
		}
		
		//根据itf主键查询txn流水表信息
		TransLog transLog = transLogService.getTransLogByDmsId(itf.getInterfacePrimaryKey());
		if (transLog == null) {
			logger.error("## 嘉福--->退款接口，退款失败，嘉福原交易流水[{}]在TransLog中不存在", refund.getOld_trade_no());
			refundback.setCode(transType.T09.getCode());
			refundback.setErrmsg(transType.T09.getValue());
			return JSONArray.toJSONString(refundback);
		}
		
		// 得到日切信息
		CtrlSystem cs = ctrlSystemService.getCtrlSystem();
		if (cs == null || !Constants.TRANS_FLAG_YES.equals(cs.getTransFlag())) {
			logger.error("## 嘉福--->退款接口，退款失败，日切为空或日切不允许[{}]", cs.getTransFlag());
			return JSONArray.toJSONString(refundback);
		}
		
		//请求middleware 进行内部系统退款
		TxnPackageBean txnBean = new TxnPackageBean();
		TxnResp txnResp = new TxnResp();
		try{
			Date currDate = DateUtil.getCurrDate();
			txnBean.setSwtFlowNo(refund.getTrade_no()); //接口平台交易流水号
			// 交易类型（0: 同步 1:异步）
			if (TransCode.CW71.getCode().equals(itf.getTransId()))
				txnBean.setTxnType(TransCode.CW74.getCode() + "0");
			else
				txnBean.setTxnType(TransCode.CW11.getCode() + "0");
			
			txnBean.setSwtTxnDate(DateUtil.getStringFromDate(currDate, DateUtil.FORMAT_YYYYMMDD));
			txnBean.setSwtTxnTime(DateUtil.getStringFromDate(currDate, DateUtil.FORMAT_HHMMSS));
			txnBean.setSwtSettleDate(cs.getSettleDate());// 交易日期
			txnBean.setChannel(ChannelCode.CHANNEL6.toString());   //请求渠道
			txnBean.setIssChannel(transLog.getInsCode()); // 机构渠道号
			txnBean.setInnerMerchantNo(transLog.getMchntCode()); // 商户号
			txnBean.setInnerShopNo(transLog.getShopCode());
			txnBean.setCardNo("U" + transLog.getUserName()); // U+UserName
			txnBean.setTxnAmount(refund.getRefund_amount()); // 交易金额
			txnBean.setOriTxnAmount(refund.getTotal_amount());
			txnBean.setTimestamp(System.currentTimeMillis());// 时间戳
			txnBean.setOriSwtFlowNo(itf.getInterfacePrimaryKey());//原交易流水号
			String sign = SignatureUtil.genB2CTransMsgSignature(txnBean.getSwtSettleDate(),
					txnBean.getSwtFlowNo(), txnBean.getIssChannel(), txnBean.getInnerMerchantNo(),
					txnBean.getTxnAmount(), txnBean.getOriSwtFlowNo(), txnBean.getOriTxnAmount(),
					txnBean.getCardNo(), txnBean.getPinTxn(), txnBean.getTimestamp(), context);
			txnBean.setMac(sign);
			
			//远程调用支持隔日退款接口
			TxnResp txnRespQuery = new TxnResp();
			String json = null;
			try {
				json = java2TxnBusinessFacade.transRefundITF(txnBean); //退款接口
			} catch (Exception e) {
				logger.error("## 嘉福--->退款接口，调用知了企服退款接口异常，嘉福退款流水号[{}]", refund.getTrade_no(), e);
			}
			logger.info("## 嘉福--->退款接口，接收知了企服退款接口返回参数[{}]", json);
			
			txnResp = JSONArray.parseObject(json, TxnResp.class);
			//接收知了企服退款接口返回值，判断是否为空,并调用查询接口判断
			try {
				json = java2TxnBusinessFacade.transExceptionQueryITF(refund.getTrade_no());
			} catch (Exception e) {
				logger.error("## 嘉福--->退款接口，调用知了企服订单查询接口失败，嘉福退款流水号[{}]", refund.getTrade_no(), e);
			}
			logger.info("嘉福--->退款接口，接收知了企服订单查询接口返回参数[{}]", json);
			txnRespQuery = JSONArray.parseObject(json, TxnResp.class);
			//设置知了企服流水，返回至嘉福
			if (Constants.MiddlewareType.MT96.getCode().equals(txnResp.getCode())) {
				if (!StringUtil.isNullOrEmpty(txnRespQuery))
					txnResp.setInterfacePrimaryKey(txnRespQuery.getInterfacePrimaryKey());
			} else {
				txnResp = txnRespQuery;
			}

			OrderInf refundOrder = orderInfService.selectByRouterOrderNo(refund.getTrade_no());
			if (StringUtil.isNullOrEmpty(refundOrder)) {
				logger.error("## 嘉福--->退款接口，嘉福退款流水号[{}]在电商平台交易订单信息中不存在", refund.getTrade_no());
			} else {
				refundOrder.setTxnFlowNo(txnResp.getInterfacePrimaryKey());
			}
			
			if (Constants.MiddlewareType.MT00.getCode().equals(txnResp.getCode())) {
				refundOrder.setOrderType(OrderType.OT04.getCode());
				refundback.setCode(transType.T00.getCode());
				refundback.setErrmsg(transType.T00.getValue());
			} else if (Constants.MiddlewareType.MT96.getCode().equals(txnResp.getCode())) {
				refundback.setCode(transType.T10.getCode());
				refundback.setErrmsg(transType.T10.getValue());
				return JSONArray.toJSONString(refundback);
			} else {
				return JSONArray.toJSONString(refundback);
			}
			refundback.setOut_trade_no(txnResp.getInterfacePrimaryKey());
			
			if (orderInfService.updateOrder(refundOrder) < 0) {
				refundback.setCode(transType.T99.getCode());
				refundback.setErrmsg(transType.T99.getValue());
				logger.error("## 嘉福--->退款接口，更新电商平台退款订单信息失败，电商流水[{}]", refundOrder.getId());
			}
		} catch(Exception e) {
			logger.error("## 嘉福--->退款接口，申请退款【系统故障】", e);
			return JSONArray.toJSONString(refundback);
		} finally {
			try{
				if (!Constants.MiddlewareType.MT00.getCode().equals(txnResp.getCode())) { //退款不成功后发短信给开发人员
					String phonesStr = RedisDictProperties.getInstance(context).getdictValueByCode("SYSTEM_MONITORING_USER_PHONES", context);
					String[] users = phonesStr.split(",");
					if(users != null){
						for(int i=0; i<users.length; i++){
							messageService.sendMessage(users[i],"【知了企服】在退款时出现故障,嘉福原交易流水号:" + oriOrder.getRouterOrderNo() + ",请及时处理!");
						}
					}
				}
			} catch (Exception e) {
				logger.error("## 嘉福--->退款接口，申请退款【系统故障】", e);
			}
		}
		//退款成功，发送模板消息
		if (Constants.MiddlewareType.MT00.getCode().equals(txnResp.getCode())) {
			String payAmt = NumberUtils.RMBCentToYuan(refund.getRefund_amount());
			IntfaceTransLog itfLog = transLogService.getIntfaceTransLogByDmsId(refund.getTrade_no());
			ShopInf shopInf = shopInfService.getShopInfByCode(itfLog.getShopCode());
			String txnDate = null;
			try {
				txnDate = DateUtil.getChineseDateFormStr(txnBean.getSwtTxnDate() + txnBean.getSwtTxnTime());
			} catch (Exception e) {
				logger.error("## 嘉福--->退款接口，调用电商平台退款模板消息出错（时间计算异常） ", e);
			}
			wechatMQProducerService.sendTemplateMsg(RedisDictProperties.getInstance(context).getdictValueByCode("WX_CUSTOMER_ACCOUNT", context), itfLog.getUserInfUserName(), "WX_TEMPLATE_ID_4", null, 
					WXTemplateUtil.setRefundData(txnResp.getInterfacePrimaryKey(), shopInf.getMchntName(), shopInf.getShopName(), payAmt, txnDate, templateMsgRefund.findByCode(oriOrder.getChannel()).getValue(), templateMsgPayment.findByCode(oriOrder.getChannel()).getValue()));
		}
		logger.info("嘉福--->退款接口，电商平台退款接口返回参数[{}]", JSONArray.toJSONString(refundback));
		return JSONArray.toJSONString(refundback);
	}
	
//	public static void main(String[] args) {
		//退款
		/*OrderJFRefund refund = new OrderJFRefund();
		refund.setTrade_no("29029185467");
		refund.setOld_trade_no("19029185467");
		refund.setTotal_amount("200");
		refund.setRefund_amount("200");
		refund.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
		refund.setSign_type("MD5");
		String refundSign = SignUtil.genSign(refund, "kec437d5o3956d1023k20ec9w08a9d27");
		refund.setSign(refundSign);
		String str = "http://cas.tao-lue.com/ecom-cash/order/refundOrder.html?trade_no="+refund.getTrade_no()+"&old_trade_no="+refund.getOld_trade_no()+"&total_amount="+refund.getTotal_amount()+"&refund_amount="+refund.getRefund_amount()+"&timestamp="+refund.getTimestamp()+"&sign_type="+refund.getSign_type()+"&sign="+refund.getSign();
		System.out.println(str);*/
		
		//下单   2018060416525400001058   18221059963
		/*JFExtandJson jfExtParams = new JFExtandJson();
		jfExtParams.setChannel("40004001");
		jfExtParams.setEcomChnl("40006005");
		jfExtParams.setMchntCode("101000000290324");
		jfExtParams.setPhoneNo("18221059963");
		jfExtParams.setShopCode("00000093");
		jfExtParams.setUserId("2018060416525400001058");
		OrderJFUnified order = new OrderJFUnified();
		String params = null;
		try {
			params = URLEncoder.encode(JSONArray.toJSONString(jfExtParams), "utf-8");
			System.out.println(params);
		} catch (UnsupportedEncodingException e) {
		}
		order.setExtand_params(JSONArray.toJSONString(jfExtParams));
		order.setE_eid("hkb_001");
		order.setE_uid("2018060416525400001058");
		order.setTrade_no("19029185467");
		order.setTotal_amount("200");
		order.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
		order.setSign_type("MD5");
		String orderSign = SignUtil.genSign(order, "kec437d5o3956d1023k20ec9w08a9d27");
		order.setSign(orderSign);
		String orderStr = "http://cas.tao-lue.com/ecom-cash/order/transOrder.html?e_eid="+order.getE_eid()+"&e_uid="+order.getE_uid()+"&trade_no="+order.getTrade_no()+"&total_amount="+order.getTotal_amount()+"&extand_params="+params+"&timestamp="+order.getTimestamp()+"&sign_type="+order.getSign_type()+"&sign="+order.getSign();
		System.out.println(orderStr);*/
//	}
	
}
