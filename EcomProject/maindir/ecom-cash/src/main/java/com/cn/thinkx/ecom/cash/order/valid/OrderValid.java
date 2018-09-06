package com.cn.thinkx.ecom.cash.order.valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.thinkx.ecom.cash.order.domain.OrderBack;
import com.cn.thinkx.ecom.cash.order.domain.OrderRedirect;
import com.cn.thinkx.ecom.cash.order.domain.OrderRefund;
import com.cn.thinkx.ecom.cash.order.domain.OrderSearch;
import com.cn.thinkx.ecom.cash.order.domain.OrderUnified;
import com.cn.thinkx.ecom.common.constants.Constants.refundFalg;
import com.cn.thinkx.pms.base.utils.StringUtil;

/**
 * 对接第三方校验参数类
 * 
 * @author xiaomei
 *
 */
public class OrderValid {
	
	private static Logger logger = LoggerFactory.getLogger(OrderValid.class);
	
	/**
	 * 第三方请求下单接口参数校验
	 * 
	 * @param orderUnified
	 * @param msg
	 * @return
	 */
	public static boolean outOrderUnifiedReqValid(OrderUnified orderUnified) {
		if (StringUtil.isNullOrEmpty(orderUnified.getChannel())) {
			logger.error("## 电商平台下单接口--->参数校验失败：channel为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getUserId())) {
			logger.error("## 电商平台下单接口--->参数校验失败：userId为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getOrderId())) {
			logger.error("## 电商平台下单接口--->参数校验失败：orderId为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getInnerMerchantNo())) {
			logger.error("## 电商平台下单接口--->参数校验失败：innerMerchantNo为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getInnerShopNo())) {
			logger.error("## 电商平台下单接口--->参数校验失败：innerShopNo为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getCommodityName())) {
			logger.error("## 电商平台下单接口--->参数校验失败：commodityName为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getCommodityNum())) {
			logger.error("## 电商平台下单接口--->参数校验失败：commodityNum空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getTxnAmount())) {
			logger.error("## 电商平台下单接口--->参数校验失败：txnAmount空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getNotify_url())) {
			logger.error("## 电商平台下单接口--->参数校验失败：notify_url空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderUnified.getSign())) {
			logger.error("## 电商平台下单接口--->参数校验失败：sign为空");
			return true;
		}
		return false;
	}
	
	/**
	 * 知了企服异步回调参数校验
	 * 
	 * @param orderBack
	 * @param msg
	 * @return
	 */
	public static boolean hkbOrderBackValid(OrderBack orderBack) {
		if (StringUtil.isNullOrEmpty(orderBack.getChannel())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：channel为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getRespResult())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：respResult为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getInnerMerchantNo())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：innerMerchantNo为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getInnerShopNo())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：innerShopNo为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getUserId())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：userId为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getOrderId())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：orderId为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getSettleDate())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：settleDate为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getTxnFlowNo())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：txnFlowNo空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getOriTxnAmount())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：oriTxnAmount为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getTxnAmount())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：txnAmount为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderBack.getSign())) {
			logger.error("## 电商平台异步回调接口--->知了企服参数校验失败：sign为空");
			return true;
		}
		return false;
	}
	
	/**
	 * 知了企服重定向结果参数校验
	 * 
	 * @param orderRedirect
	 * @param msg
	 * @return
	 */
	public static boolean hkbOrderRedirectValid(OrderRedirect orderRedirect) {
		if (StringUtil.isNullOrEmpty(orderRedirect.getChannel())) {
			logger.error("## 电商平台重定向接口--->知了企服参数校验失败：channel为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderRedirect.getRespResult())) {
			logger.error("## 电商平台重定向接口--->知了企服参数校验失败：respResult为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderRedirect.getUserId())) {
			logger.error("## 电商平台重定向接口--->知了企服参数校验失败：userId为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderRedirect.getOrderId())) {
			logger.error("## 电商平台重定向接口--->知了企服参数校验失败：orderId为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderRedirect.getTxnFlowNo())) {
			logger.error("## 电商平台重定向接口--->知了企服参数校验失败：txnFlowNo为空");
			return true;
		}
		if (StringUtil.isNullOrEmpty(orderRedirect.getSign())) {
			logger.error("## 电商平台重定向接口--->知了企服参数校验失败：sign为空");
			return true;
		}
		return false;
	}
	
	/**
	 * 第三方请求查单接口参数校验
	 * 
	 * @param searchOrder
	 * @param msg
	 * @return
	 */
	public static String outSearchReqValid(OrderSearch searchOrder) {
		if (StringUtil.isNullOrEmpty(searchOrder.getChannel())) {
			return "channel为空";
		}
		if (StringUtil.isNullOrEmpty(searchOrder.getUserId())) {
			return "userId为空";
		}
		if (StringUtil.isNullOrEmpty(searchOrder.getOrderId()) && StringUtil.isNullOrEmpty(searchOrder.getTxnFlowNo())) {
			return "orderId 和  txnFlowNo 二者不能全部为空";
		}
		if (StringUtil.isNullOrEmpty(searchOrder.getSign())) {
			return "sign为空";
		}
		return null;
	}
	
	/**
	 * 第三方请求退款接口参数校验
	 * 
	 * @param orderRefund
	 * @param msg
	 * @return
	 */
	public static String outRefundReqValid(OrderRefund orderRefund) {
		if (StringUtil.isNullOrEmpty(orderRefund.getOriOrderId())) {
			return "oriOrderId为空";
		}
		if (StringUtil.isNullOrEmpty(orderRefund.getRefundOrderId())) {
			return "refundOrderId为空";
		}
		if (StringUtil.isNullOrEmpty(orderRefund.getRefundAmount())) {
			return "refundAmount为空";
		}
		if (StringUtil.isNullOrEmpty(orderRefund.getChannel())) {
			return "channel为空";
		}
		if (StringUtil.isNullOrEmpty(orderRefund.getRefundFlag())) {
			return "refundFlag为空";
		}
		if (StringUtil.isNullOrEmpty(orderRefund.getTimestamp())) {
			return "timestamp为空";
		}
		if (StringUtil.isNullOrEmpty(orderRefund.getSign())) {
			return "sign为空";
		}
		if (refundFalg.refundFalg1.getCode().equals(orderRefund.getRefundFlag())) {
			logger.info("系统发起退款");
		} else if (refundFalg.refundFalg2.getCode().equals(orderRefund.getRefundFlag())) {
			logger.info("用户端发起退款");
		} else {
			return "refundFlag不正确";
		}
		return null;
	}
}
