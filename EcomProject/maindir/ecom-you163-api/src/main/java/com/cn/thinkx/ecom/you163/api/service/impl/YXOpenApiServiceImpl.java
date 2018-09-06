package com.cn.thinkx.ecom.you163.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.time.StopWatch;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.HttpService;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.utils.DateUtils;
import com.cn.thinkx.ecom.you163.api.utils.ResponseCode;
import com.cn.thinkx.ecom.you163.api.utils.YXOpenApiUtils;
import com.cn.thinkx.ecom.you163.api.vo.ExpressInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplyInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderVO;
import com.cn.thinkx.ecom.you163.config.YXApiProperies;
import com.cn.thinkx.ecom.you163.config.You163Constants;

@Service("yxOpenApiService")
public class YXOpenApiServiceImpl implements YXOpenApiService {
    private Logger logger = LoggerFactory.getLogger(YXOpenApiServiceImpl.class);

    @Autowired
    private YXApiProperies yxApiProperies;

    @Autowired
    private HttpService commonHttpService;
    
	@Autowired
	private JedisClusterUtils jedisClusterUtils;

    @Override
    public JsonResult handleGetIds() {
        HashMap<String, String> data = new HashMap<>();
        return handle( jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetIdsMethod(), ".handleGetIds()");
    }

    @Override
    public JsonResult handleGetItemsById(String itemIds) {
        if (StringUtils.isEmpty(itemIds)) {
            logger.error(".handleGetItemsById() fail, itemIds is null");
            return initFailureResult(ResponseCode.FAIL, "itemIds is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("itemIds", itemIds);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetItemsMethod(), ".handleGetItemsById()");
    }

    @Override
    public JsonResult handlePayedOrder(OrderVO orderVO) {
        HashMap<String, String> data = new HashMap<>();
        data.put("order", JSON.toJSONString(orderVO));
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getPayedOrderMethod(), ".handlePayedOrder()");
    }

    @Override
    public JsonResult handleGetOrder(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleGetOrder() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetOrderMethod(), ".handleGetOrder()");
    }

    @Override
    public JsonResult handleConfirmOrder(String orderId, String packageId, String confirmTime) {
        if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleGetOrder() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        if (StringUtils.isEmpty(packageId)) {
            packageId = "0";
        }
        if (StringUtils.isEmpty(confirmTime)) {
            confirmTime = DateUtils.parseLongToString(System.currentTimeMillis(),
                    "yyyy-MM-dd HH:mm:ss");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("packageId", packageId);
        data.put("confirmTime", confirmTime);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getConfirmOrderMethod(), ".handleConfirmOrder()");
    }

    @Override
    public JsonResult handleCancelOrder(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleCancelOrder() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getCancelOrderMethod(), ".handleCancelOrder()");
    }

    @Override
    public JsonResult handleGetInventory(String skuIds) {
        if (StringUtils.isEmpty(skuIds)) {
            logger.error(".handleGetInventory() fail, skuIds is null");
            return initFailureResult(ResponseCode.FAIL, "skuIds is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("skuIds", JSON.toJSONString(skuIds.split(",")));
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetInventoryMethod(), ".handleGetInventory()");
    }

    @Override
    public JsonResult handleGetCallBackMethods() {
        HashMap<String, String> data = new HashMap<>();
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetCallBackMethods(), ".handleGetCallBackMethods()");
    }

    @Override
    public JsonResult handleRegisterCallbackMethod(String methods) {
        if (StringUtils.isEmpty(methods)) {
            logger.error(".handleRegisterCallbackMethod() fail, methods is null");
            return initFailureResult(ResponseCode.FAIL, "methods is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("methods", methods);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getRegisterCallbackMethod(), ".handleRegisterCallbackMethod()");
    }

    @Override
    public JsonResult handleCallbackAuditCancelOrder(String orderId, boolean cancel) {
        if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleCallbackAuditCancelOrder() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("cancel", String.valueOf(cancel));
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackAuditCancelOrder(), data, yxApiProperies.getCallbackAuditCancelOrder(),
            ".handleCallbackAuditCancelOrder()");
    }

    @Override
    public JsonResult handleCallbackCancelOrder(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleCallbackCancelOrder() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackCancelOrder(), data, yxApiProperies.getCallbackCancelOrder(),
            ".handleCallbackCancelOrder()");
    }

    @Override
    public JsonResult handleCallbackDeliveryOrder(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleCallbackDeliveryOrder() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackDeliveryOrder(), data, yxApiProperies.getCallbackDeliveryOrder(),
            ".handleCallbackDeliveryOrder()");
    }

    @Override
    public JsonResult handleCallbackTransfer(long skuId, int count) {
        if (skuId <= 0 || count <= 0) {
            logger.error(".handleCallbackTransfer() fail, skuId or count is wrong");
            return initFailureResult(ResponseCode.FAIL, "skuId or count is wrong");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("skuId", String.valueOf(skuId));
        data.put("count", String.valueOf(count));
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackTransfer(), data, yxApiProperies.getCallbackTransfer(), ".handleCallbackTransfer()");
    }
    
    /**begin**/
    @Override
	public JsonResult handleCallbackRefundAddress(String applyId, int type){
    	if (StringUtils.isEmpty(applyId) || type <= 0) {
            logger.error(".handleCallbackRefundAddress() fail, applyId is null or type is wrong");
            return initFailureResult(ResponseCode.FAIL, "applyId is null or type is wrong");
        }
    	HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        data.put("type", String.valueOf(type));
        
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackRefundAddress(), data, yxApiProperies.getCallbackRefundAddress(), ".handleCallbackRefundAddress()");
    }
    
    @Override
    public JsonResult handleCallbackRefundReject(String applyId){
    	if (StringUtils.isEmpty(applyId)) {
            logger.error(".handleCallbackRefundReject() fail, applyId is null");
            return initFailureResult(ResponseCode.FAIL, "applyId is null");
        }
    	HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackRefundReject(), data, yxApiProperies.getCallbackRefundReject(), ".handleCallbackRefundReject()");
    }
	
    @Override
    public JsonResult handleCallbackRefundExpressConfirm(String applyId){
    	if (StringUtils.isEmpty(applyId)) {
            logger.error(".handleCallbackRefundExpressConfirm() fail, applyId is null");
            return initFailureResult(ResponseCode.FAIL, "applyId is null");
        }
    	HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackRefundExpressConfirm(), data, yxApiProperies.getCallbackRefundExpressConfirm(), ".handleCallbackRefundExpressConfirm()");
    }
	
    @Override
    public JsonResult handleCallbackRefundSystemCancel(String applyId){
    	if (StringUtils.isEmpty(applyId)) {
            logger.error(".handleCallbackRefundSystemCancel() fail, applyId is null");
            return initFailureResult(ResponseCode.FAIL, "applyId is null");
        }
    	HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackRefundSystemCancel(), data, yxApiProperies.getCallbackRefundSystemCancel(), ".handleCallbackRefundSystemCancel()");
    }
	
    @Override
    public JsonResult handleCallbackRefundResultDirectly(String applyId){
    	if (StringUtils.isEmpty(applyId)) {
            logger.error(".handleCallbackRefundResultDirectly() fail, applyId is null");
            return initFailureResult(ResponseCode.FAIL, "applyId is null");
        }
    	HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackRefundResultDirectly(), data, yxApiProperies.getCallbackRefundResultDirectly(), ".handleCallbackRefundResultDirectly()");
    }
	
    @Override
    public JsonResult handleCallbackRefundResultWithExpress(String applyId, boolean allApproved){
    	if (StringUtils.isEmpty(applyId)) {
            logger.error(".handleCallbackRefundResultWithExpress() fail, applyId is null");
            return initFailureResult(ResponseCode.FAIL, "applyId is null");
        }
    	HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        
        return handle(yxApiProperies.getMockAPIPathPrefix() + yxApiProperies.getCallbackRefundResultWithExpress(), data, yxApiProperies.getCallbackRefundResultWithExpress(), ".handleCallbackRefundResultWithExpress()");
    }
    /**end**/
    
    private JsonResult handle(String path, HashMap<String, String> data, String method, String logTag) {
        // 构建时间戳
        long currentTime = System.currentTimeMillis();
        String timestamp = DateUtils.parseLongToString(currentTime, "yyyy-MM-dd HH:mm:ss");

        TreeMap<String, String> paramsMap = YXOpenApiUtils.packageParams(
        		jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPKEY), 
        		jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_APPSECRET),
        		timestamp, method, data);

        // 组装参数
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String url =jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_HOST) + path;
        try {
        	StopWatch sw = new StopWatch();
        	sw.start();
        	
            // 调用严选open api
            String response = commonHttpService.doPost(url, params, "utf-8");
            
            sw.stop();
            logger.info("{} method={}, params={}, response={}, 耗时={}", logTag, method, JSON.toJSONString(params), 
            		response, sw.getTime());

            return handleResponse(response);
        } catch (Exception e) {
            logger.error("{} Exception={}, method={}, params={}", logTag, e.getMessage(), method, JSON.toJSONString(params));
            return initServerError();
        }
    }

    private JsonResult handleResponse(String response) {

        if (StringUtils.isEmpty(response)) {
            if (null != logger) {
                logger.error(".handleResponse() response is empty");
            }
            return initServerError();
        }

        JSONObject responseObj = JSON.parseObject(response);

        String code = responseObj.getString("code");

        if (code.equals("200")) {
            return initSuccessResult(responseObj.get("result"));
        } else {
            if (null != logger) {
                logger.error(".handleResponse() fail. response={}", response);
            }
            return initServerError();
        }
    }

    private JsonResult initServerError() {
        return initFailureResult(ResponseCode.SERVER_ERROR, "server error");
    }

    private JsonResult initFailureResult(int code, String msg) {
        JsonResult JsonResult = new JsonResult(code);
        JsonResult.setMessage(msg);
        return JsonResult;
    }

    private JsonResult initSuccessResult(Object result) {
        JsonResult JsonResult = new JsonResult(ResponseCode.SUCCESS);
        if (result != null)
            JsonResult.setResult(result);
        return JsonResult;
    }

	@Override
	public JsonResult handleOrderRefundApply(ApplyInfoVO applyInfo) {
		HashMap<String, String> data = new HashMap<>();
        data.put("applyInfo", JSON.toJSONString(applyInfo));
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getOrderRefundApplyMethod(), ".handleOrderRefundApply()");
	}

	@Override
	public JsonResult handleOrderRefundCancel(String applyId) {
		if (StringUtils.isEmpty(applyId)) {
            logger.error(".handleOrderRefundCancel() fail, applyId is null");
            return initFailureResult(ResponseCode.FAIL, "applyId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("applyId", applyId);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getOrderRefundCancelMethod(), ".handleOrderRefundCancel()");
	}

	@Override
	public JsonResult handleGetExpress(String orderId, String packageId, String expressNo, String expressCom) {
		if (StringUtils.isEmpty(orderId)) {
            logger.error(".handleGetExpress() fail, orderId is null");
            return initFailureResult(ResponseCode.FAIL, "orderId is null");
        }
        if (StringUtils.isEmpty(packageId)) {
            packageId = "0";
        }
        if (StringUtils.isEmpty(expressNo)) {
        	logger.error(".handleGetExpress() fail, expressNo is null");
            return initFailureResult(ResponseCode.FAIL, "expressNo is null");
        }
        if (StringUtils.isEmpty(expressCom)) {
        	logger.error(".handleGetExpress() fail, expressCom is null");
            return initFailureResult(ResponseCode.FAIL, "expressCom is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("packageId", packageId);
        data.put("expressNo", expressNo);
        data.put("expressCom", expressCom);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetExpressMethod(), ".handleGetExpress()");
	}
	
	@Override
	public JsonResult handleOrderRefundOfferExpress(String applyId, List<ExpressInfoVO> expressInfo) {
		if (StringUtils.isEmpty(applyId)) {
			logger.error(".handleGetExpressOffer() fail, applyId is null");
			return initFailureResult(ResponseCode.FAIL, "applyId is null");
		}
		if (StringUtils.isEmpty(expressInfo)) {
			logger.error(".handleGetExpressOffer() fail, expressInfo is null");
			return initFailureResult(ResponseCode.FAIL, "expressInfo is null");
		}
		HashMap<String, String> data = new HashMap<>();
		data.put("applyId", applyId);
		data.put("expressInfo", JSONArray.toJSONString(expressInfo));
		return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getOrderRefundOfferExpress(), ".handleOrderRefundOfferExpress()");
	}

	@Override
	public JsonResult handleGetOrderRefundDetail(String applyId) {
		if (StringUtils.isEmpty(applyId)) {
			logger.error(".handleGetDetailOrder() fail, applyId is null");
			return initFailureResult(ResponseCode.FAIL, "applyId is null");
		}
		HashMap<String, String> data = new HashMap<>();
		data.put("applyId", applyId);
		return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetOrderRefundDetail(), ".handleGetOrderRefundDetail()");
	}

	@Override
	public JsonResult handleGetCategory() {
		HashMap<String, String> data = new HashMap<>();
        return handle( jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetCategory(), ".handleGetCategory()");
	}

	@Override
	public JsonResult handleGetSkuIdBatch(String categoryId) {
		if (StringUtils.isEmpty(categoryId)) {
            logger.error(".handleGetSkuIdBatch() fail, categoryId is null");
            return initFailureResult(ResponseCode.FAIL, "categoryId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("categoryId", categoryId);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetSkuIdBatch(), ".handleGetSkuIdBatch()");
	}

	@Override
	public JsonResult handleGetItemSku(String skuId) {
		if (StringUtils.isEmpty(skuId)) {
            logger.error(".handleGetItemSku() fail, skuId is null");
            return initFailureResult(ResponseCode.FAIL, "skuId is null");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("skuId", skuId);
        return handle(jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, You163Constants.ECOM_YOU163_PATH), data, yxApiProperies.getGetItemSku(), ".handleGetItemSku()");
	}
}
