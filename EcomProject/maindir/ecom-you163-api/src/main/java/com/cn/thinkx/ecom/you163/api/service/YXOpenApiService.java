package com.cn.thinkx.ecom.you163.api.service;

import java.util.List;

import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.vo.ExpressInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplyInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderVO;

public interface YXOpenApiService {

	/**
	 * 获取SPU
	 * @return
	 */
	JsonResult handleGetIds();

	/**
	 * 根据商品SPU查找货品列表
	 * @param itemIds
	 * @return
	 */
	JsonResult handleGetItemsById(String itemIds);

	JsonResult handlePayedOrder(OrderVO orderVO);

	JsonResult handleGetOrder(String orderId);

	JsonResult handleConfirmOrder(String orderId, String packageId, String confirmTime);

	JsonResult handleCancelOrder(String orderId);

	JsonResult handleGetInventory(String skuIds);

	JsonResult handleGetCallBackMethods();

	JsonResult handleRegisterCallbackMethod(String methods);

	JsonResult handleCallbackAuditCancelOrder(String orderId, boolean cancel);

	JsonResult handleCallbackCancelOrder(String orderId);

	JsonResult handleCallbackDeliveryOrder(String orderId);

	JsonResult handleCallbackTransfer(long skuId, int count);
	/**begin**/
	JsonResult handleCallbackRefundAddress(String applyId, int type);
	
	JsonResult handleCallbackRefundReject(String applyId);
	
	JsonResult handleCallbackRefundExpressConfirm(String applyId);
	
	JsonResult handleCallbackRefundSystemCancel(String applyId);
	
	JsonResult handleCallbackRefundResultDirectly(String applyId);
	
	JsonResult handleCallbackRefundResultWithExpress(String applyId, boolean allApproved);
	/**end**/
	JsonResult handleOrderRefundApply(ApplyInfoVO applyInfo);
	
	JsonResult handleOrderRefundCancel(String applyId);

	JsonResult handleGetExpress(String orderId, String packageId, String expressNo, String expressCom);
	
	JsonResult handleOrderRefundOfferExpress(String applyId, List<ExpressInfoVO> expressInfo);
	
	JsonResult handleGetOrderRefundDetail(String applyId);
	
	JsonResult handleGetCategory();
	
	JsonResult handleGetSkuIdBatch(String categoryId);

	JsonResult handleGetItemSku(String skuId);
}
