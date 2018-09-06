package com.cn.thinkx.ecom.bm001.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.bm001.api.constants.BMConstants;
import com.cn.thinkx.ecom.bm001.api.req.PayBillReq;
import com.cn.thinkx.ecom.bm001.api.service.BMOpenApiService;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.qianmi.open.api.ApiException;
import com.qianmi.open.api.DefaultOpenClient;
import com.qianmi.open.api.OpenClient;
import com.qianmi.open.api.request.BmOrderCustomGetRequest;
import com.qianmi.open.api.request.BmRechargeMobileGetItemInfoRequest;
import com.qianmi.open.api.request.BmRechargeMobileGetPhoneInfoRequest;
import com.qianmi.open.api.request.BmRechargeMobilePayBillRequest;
import com.qianmi.open.api.request.BmRechargeOrderInfoRequest;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeMobileGetItemInfoResponse;
import com.qianmi.open.api.response.BmRechargeMobileGetPhoneInfoResponse;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import com.qianmi.open.api.response.BmRechargeOrderInfoResponse;

@Service("bmOpenApiService")
public class BMOpenApiServiceImpl implements BMOpenApiService {
	
	 private Logger logger = LoggerFactory.getLogger(BMOpenApiServiceImpl.class);
	 
	 @Autowired
	 private JedisClusterUtils jedisClusterUtils;
	 
	@Override
	public BmRechargeMobileGetItemInfoResponse handleGetItemInfo(String mobileNo, String rechargeAmount, String accessToken) {
		OpenClient client = getOpenClient();
		BmRechargeMobileGetItemInfoRequest req = new BmRechargeMobileGetItemInfoRequest();
		req.setMobileNo(mobileNo);
		req.setRechargeAmount(rechargeAmount);
		
		logger.info("查询话费充商请求参数  [{}]",JSONObject.toJSONString(req));
		
		BmRechargeMobileGetItemInfoResponse response = null;
		try {
			response = client.execute(req, accessToken);
		} catch (ApiException e) {
			logger.error(" ## 调用力方查询单个话费直充商品出错",e);
		}
		return response;
	}

	@Override
	public BmRechargeMobilePayBillResponse handlePayBill(PayBillReq payBillReq, String accessToken) {
		OpenClient client = getOpenClient();
		BmRechargeMobilePayBillRequest req = new BmRechargeMobilePayBillRequest();
		req.setMobileNo(payBillReq.getMobileNo());
		req.setRechargeAmount(payBillReq.getRechargeAmount());
		if(!StringUtil.isNullOrEmpty(payBillReq.getOuterTid())){
			req.setOuterTid(payBillReq.getOuterTid());
		}
		if(!StringUtil.isNullOrEmpty(payBillReq.getCallback())){
			req.setCallback(payBillReq.getCallback());
		}
		if(!StringUtil.isNullOrEmpty(payBillReq.getItemId())){
			req.setItemId(payBillReq.getItemId());
		}
		
		logger.info("话费充值请求参数  [{}]",JSONObject.toJSONString(req));
		
		BmRechargeMobilePayBillResponse response = null;
		try {
			response = client.execute(req, accessToken);
		} catch (ApiException e) {
			logger.error(" ## 调用力方话费订单充值出错",e);
		}
		return response;
	}

	@Override
	public BmRechargeOrderInfoResponse handleGetOrderInfo(String billId, String accessToken) {
		OpenClient client = getOpenClient();
		BmRechargeOrderInfoRequest req = new BmRechargeOrderInfoRequest();
		req.setBillId(billId);
		
		logger.info("话费查询请求参数  [{}]",JSONObject.toJSONString(req));
		
		BmRechargeOrderInfoResponse response = null;
		try {
			response = client.execute(req, accessToken);
		} catch (ApiException e) {
			logger.error(" ## 调用力方获取订单详情出错",e);
		}
		return response;
	}
	

	@Override
	public BmOrderCustomGetResponse handleGetCustomOrder(String outerTid, String accessToken) {
		OpenClient client = getOpenClient();
		BmOrderCustomGetRequest req = new BmOrderCustomGetRequest();
		req.setOuterTid(outerTid);
		
		logger.info("话费查询请求参数  [{}]",JSONObject.toJSONString(req));
		
		BmOrderCustomGetResponse response = null;
		try {
			response = client.execute(req, accessToken);
		} catch (ApiException e) {
			logger.error(" ## 调用力方根据外部订单号获取订单详情出错",e);
		}
		return response;
	}

	@Override
	public BmRechargeMobileGetPhoneInfoResponse handleGetPhoneInfo(String mobileNo, String accessToken) {
		OpenClient client = getOpenClient();
		BmRechargeMobileGetPhoneInfoRequest  req = new BmRechargeMobileGetPhoneInfoRequest ();
		req.setPhoneNo(mobileNo);
		
		logger.info("查询手机信息请求参数  [{}]",JSONObject.toJSONString(req));
		
		BmRechargeMobileGetPhoneInfoResponse response = null;
		try {
			response = client.execute(req, accessToken);
		} catch (ApiException e) {
			logger.error(" ## 查询手机信息出错",e);
		}
		return response;
	}
	
	private OpenClient getOpenClient(){
		try {
			String serverUrl = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, BMConstants.BM_SERVER_URL);
			String appKey = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, BMConstants.BM_APP_KEY);
			String appSecret = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, BMConstants.BM_APP_SECRET);
			OpenClient client = new DefaultOpenClient(serverUrl, appKey, appSecret);
			return client;
		} catch (Exception e) {
			logger.error(" ## 获取请求地址信息出错",e);
			return null;
		}
	}

	 
}
