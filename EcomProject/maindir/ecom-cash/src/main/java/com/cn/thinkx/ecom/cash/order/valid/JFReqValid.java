package com.cn.thinkx.ecom.cash.order.valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.thinkx.ecom.cash.order.domain.jiafu.OrderJFRefund;
import com.cn.thinkx.ecom.cash.order.domain.jiafu.OrderJFUnified;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.pms.base.utils.StringUtil;

/**
 * 对接嘉福校验参数类
 * 
 * @author xiaomei
 *
 */
public class JFReqValid {
	
	private static Logger logger = LoggerFactory.getLogger(JFReqValid.class);
	
	/**
	 * 消费交易接口参数校验
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public static boolean transReqValid(OrderJFUnified jforder) {
		logger.info("嘉福--->下单接口，电商平台下单接口接收的请求参数[{}]", jforder.toString());
		/*long timestamp = Long.valueOf(jforder.getTimestamp()).longValue(); 
		String curTime = String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000);
		long curTimestamp = Long.valueOf(curTime).longValue(); */
		if (StringUtil.isNullOrEmpty(jforder.getE_eid())) {
			logger.error("## 嘉福--->下单接口，参数校验：渠道企业标识为空");
			return true;
		}
		if (!jforder.getE_eid().equals("hkb_001")) {
			logger.error("## 嘉福--->下单接口，参数校验：渠道企业标识不正确");
			return true;
		}
		if (StringUtil.isNullOrEmpty(jforder.getE_uid())) {
			logger.error("## 嘉福--->下单接口，参数校验：渠道用户标识为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(jforder.getExtand_params())) {
			logger.error("## 嘉福--->下单接口，参数校验：拓展参数为空");
			return true;
		}
		/*JFExtandJson extandParams = JSON.parseObject(jforder.getExtand_params(), JFExtandJson.class);
		if (!jforder.getE_uid().equals(extandParams.getUserId())) {
			transBack.setErrmsg("渠道用户标识与拓展参数用户标志不一致");
			return true;
		}
		if (StringUtil.isNullOrEmpty(extandParams.getEcomChnl())) {
			transBack.setErrmsg("电商渠道标识为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(extandParams.getMchntCode())) {
			transBack.setErrmsg("商户号为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(extandParams.getShopCode())) {
			transBack.setErrmsg("门店号为空");
			return true;
		}*/
		if (StringUtil.isNullOrEmpty(jforder.getTrade_no())) {
			logger.error("## 嘉福--->下单接口，参数校验：平台交易号为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(jforder.getTotal_amount())) {
			logger.error("## 嘉福--->下单接口，参数校验：支付金额为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(jforder.getTimestamp())) {
			logger.error("## 嘉福--->下单接口，参数校验：时间戳为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(jforder.getSign_type())) {
			logger.error("## 嘉福--->下单接口，参数校验：签名类型为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(jforder.getSign())) {
			logger.error("## 嘉福--->下单接口，参数校验：签名为空");
			return true;
		}
		/*if (!SignUtil.genSign(jforder, key).equals(jforder.getSign())) {
			transBack.setErrmsg("签名错误");
			return true;
		}*/
		/*if (SignUtil.isSignExpired(curTimestamp, timestamp)) {
			transBack.setErrmsg("签名过期");
			return true;
		}*/
		return false;
	}
	
	/**
	 * 退款交易接口参数校验
	 * 
	 * @param refund
	 * @param refundback
	 * @param key
	 * @return
	 */
	public static boolean refundReqValid(OrderJFRefund refund, String key) {
		logger.info("嘉福--->退款接口，电商平台退款接口接收的请求参数[{}]", refund.toString());
//		long timestamp = Long.valueOf(refund.getTimestamp()).longValue(); 
		if (StringUtil.isNullOrEmpty(refund.getTrade_no())) {
			logger.error("## 嘉福--->退款接口，参数校验：退款流水号为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(refund.getOld_trade_no())) {
			logger.error("## 嘉福--->退款接口，参数校验：原平台支付流水号为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(refund.getTotal_amount())) {
			logger.error("## 嘉福--->退款接口，参数校验：原平台支付金额为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(refund.getRefund_amount())) {
			logger.error("## 嘉福--->退款接口，参数校验：退款金额为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(refund.getTimestamp())) {
			logger.error("## 嘉福--->退款接口，参数校验：时间戳为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(refund.getSign_type())) {
			logger.error("## 嘉福--->退款接口，参数校验：签名类型为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(refund.getSign())) {
			logger.error("## 嘉福--->退款接口，参数校验：签名为空");
			return true;
		}
		if (!SignUtil.genSign(refund, key).equals(refund.getSign())) {
			logger.error("## 嘉福--->退款接口，参数校验：签名错误");
			return true;
		}
		/*if (SignUtil.isSignExpired(System.currentTimeMillis(), timestamp)) {
			refundback.setErrmsg("签名过期");
			return true;
		}*/
		return false;
	}
	
	/**
	 * 查单接口参数校验
	 * 
	 * @param searchOrder
	 * @param searchback
	 * @param key
	 * @return
	 */
	public static boolean searchReqValid(OrderJFRefund search, String key) {
		logger.info("嘉福--->查询接口，电商平台查询接口接收的请求参数[{}]", search.toString());
//		long timestamp = Long.valueOf(search.getTimestamp()).longValue(); 
		if (StringUtil.isNullOrEmpty(search.getTrade_no())) {
			logger.error("## 嘉福--->查询接口，参数校验：平台交易号为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(search.getTimestamp())) {
			logger.error("## 嘉福--->查询接口，参数校验：时间戳为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(search.getSign_type())) {
			logger.error("## 嘉福--->查询接口，参数校验：签名类型为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(search.getSign())) {
			logger.error("## 嘉福--->查询接口，参数校验：签名为空");
			return true;
		}
		if (!SignUtil.genSign(search, key).equals(search.getSign())) {
			logger.error("## 嘉福--->查询接口，参数校验：签名错误");
			return true;
		}
		/*if (SignUtil.isSignExpired(System.currentTimeMillis(), timestamp)) {
			searchback.setErrmsg("签名过期");
			return true;
		}*/
		return false;
	}
	
}
