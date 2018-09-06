package com.cn.thinkx.ecom.you163.notify.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXInventoryNotificationService;
import com.cn.thinkx.ecom.you163.api.service.YXOrderRefundService;
import com.cn.thinkx.ecom.you163.api.service.YXOrderService;
import com.cn.thinkx.ecom.you163.api.utils.YXOpenApiUtils;
import com.cn.thinkx.ecom.you163.config.You163Constants;

@RestController
@RequestMapping(value = "you163")
public class ApiYou163NotifyController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private YXOrderService yxOrderService;
	
	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Autowired
	private YXInventoryNotificationService yxInventoryNotificationService;
	
	@Autowired
	private YXOrderRefundService yxOrderRefundService;

	@PostMapping(value = "/notify")
	public JsonResult notify(@RequestParam(value = "method") final String method,
			@RequestParam(value = "appKey") final String appKey, @RequestParam(value = "sign") final String sign,
			@RequestParam(value = "timestamp") final String timestamp, HttpServletRequest req) {
		logger.info("method={}, appKey={}, sign={}, timestamp={}", method, appKey, sign, timestamp);

		// 回调参数集合
		Map<String, String> paramMap = null;

		// TODO 待优化模块 需要封装 验签，回调逻辑处理

		// 订单取消回调(yanxuan.callback.order.cancel)
		if ("yanxuan.callback.order.cancel".equals(method)) {

			String orderCancelResult = req.getParameter("orderCancelResult");
			logger.info("## 订单取消回调:orderCancelResult={}", orderCancelResult);

			paramMap = new HashMap<>();
			paramMap.put("orderCancelResult", orderCancelResult);

			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey, jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderService.cancelOrder(orderCancelResult);

			// 订单包裹物流绑单回调(yanxuan.notification.order.delivered)
		} else if ("yanxuan.notification.order.delivered".equals(method)) {

			String orderPackage = req.getParameter("orderPackage");
			logger.info("## 订单包裹物流绑单回调:orderPackage={}", orderPackage);
			paramMap = new HashMap<>();
			paramMap.put("orderPackage", orderPackage);

			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderService.deliveredOrder(orderPackage);

			// 订单异常回调(yanxuan.notification.order.exceptional)
		} else if ("yanxuan.notification.order.exceptional".equals(method)) {

			String exceptionInfo = req.getParameter("exceptionInfo");
			logger.info("## 订单异常回调:exceptionInfo={}", exceptionInfo);

			paramMap = new HashMap<>();
			paramMap.put("exceptionInfo", exceptionInfo);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderService.exceptionalOrder(exceptionInfo);

			// SKU库存划拨回调(yanxuan.notification.inventory.count.changed)
		} else if ("yanxuan.notification.inventory.count.changed".equals(method)) {

			String skuTransfer = req.getParameter("skuTransfer");
			logger.info("## SKU库存划拨回调:skuTransfer={}", skuTransfer);

			paramMap = new HashMap<>();
			paramMap.put("skuTransfer", skuTransfer);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxInventoryNotificationService.changedInventoryCount(skuTransfer);
		}

		// SKU库存校准回调(yanxuan.notification.inventory.count.check)
		else if ("yanxuan.notification.inventory.count.check".equals(method)) {

			String skuCheck = req.getParameter("skuCheck");
			logger.info("## SKU库存校准回调:skuCheck={}", skuCheck);

			paramMap = new HashMap<>();
			paramMap.put("skuCheck", skuCheck);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey, jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxInventoryNotificationService.inventoryCountCheck(skuCheck);

			// 渠道SKU低库存预警通知(yanxuan.notification.sku.alarm.close)
		} else if ("yanxuan.notification.sku.alarm.close".equals(method)) {

			String closeAlarmSkus = req.getParameter("closeAlarmSkus");
			logger.info("## 渠道SKU低库存预警通知:closeAlarmSkus={}", closeAlarmSkus);

			paramMap = new HashMap<>();
			paramMap.put("closeAlarmSkus", closeAlarmSkus);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxInventoryNotificationService.skuAlarmClose(closeAlarmSkus);

			// 渠道SKU再次开售通知(yanxuan.notification.sku.reopen)
		} else if ("yanxuan.notification.sku.reopen".equals(method)) {

			String reopenSkus = req.getParameter("reopenSkus");
			logger.info("## 渠道SKU再次开售通知:reopenSkus={}", reopenSkus);

			paramMap = new HashMap<>();
			paramMap.put("reopenSkus", reopenSkus);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxInventoryNotificationService.skuReopen(reopenSkus);
			
			//退货地址回调(yanxuan.notification.order.refund.address)
		}else if ("yanxuan.notification.order.refund.address".equals(method)) {

			String refundAddress = req.getParameter("refundAddress");
			logger.info("## 退货地址回调:refundAddress={}", refundAddress);

			paramMap = new HashMap<>();
			paramMap.put("refundAddress", refundAddress);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderRefundService.doRefundAddress(refundAddress);
			
			//严选拒绝退货回调(yanxuan.notification.order.refund.reject)
		}else if ("yanxuan.notification.order.refund.reject".equals(method)) {

			String rejectInfo = req.getParameter("rejectInfo");
			logger.info("## 严选拒绝退货回调:rejectInfo={}", rejectInfo);

			paramMap = new HashMap<>();
			paramMap.put("rejectInfo", rejectInfo);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderRefundService.doRefundReject(rejectInfo);
			
			//退货包裹确认收货回调(yanxuan.notification.order.refund.express.confirm)
		}else if ("yanxuan.notification.order.refund.express.confirm".equals(method)) {

			String expressConfirm = req.getParameter("expressConfirm");
			logger.info("## 退货包裹确认收货回调:expressConfirm={}", expressConfirm);

			paramMap = new HashMap<>();
			paramMap.put("expressConfirm", expressConfirm);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderRefundService.doRefundExpressConfirm(expressConfirm);
			
			//严选系统取消退货回调(yanxuan.notification.order.refund.system.cancel)
		}else if ("yanxuan.notification.order.refund.system.cancel".equals(method)) {

			String systemCancel = req.getParameter("systemCancel");
			logger.info("## 严选系统取消退货回调:systemCancel={}", systemCancel);

			paramMap = new HashMap<>();
			paramMap.put("systemCancel", systemCancel);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderRefundService.doRefundSystemCancel(systemCancel);
			
			//退款结果回调(yanxuan.notification.order.refund.result)
		}else if ("yanxuan.notification.order.refund.result".equals(method)) {

			String refundResult = req.getParameter("refundResult");
			logger.info("## 退款结果回调:refundResult={}", refundResult);

			paramMap = new HashMap<>();
			paramMap.put("refundResult", refundResult);
			// getSign 签名校验
			YXOpenApiUtils.getSign(appKey,  jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET), timestamp, method, paramMap);

			return yxOrderRefundService.doRefundResultDirectly(refundResult);
		}

		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		return jsonResult;
	}
}
