package com.cn.thinkx.ecom.you163.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YXApiProperies {

    @Value("${yx.openApi.getIds.method}")
    private String getIdsMethod;

    @Value("${yx.openApi.getItems.method}")
    private String getItemsMethod;

    @Value("${yx.openApi.payedOrder.method}")
    private String payedOrderMethod;

    @Value("${yx.openApi.getOrder.method}")
    private String getOrderMethod;

    @Value("${yx.openApi.confirmOrder.method}")
    private String confirmOrderMethod;

    @Value("${yx.openApi.cancelOrder.method}")
    private String cancelOrderMethod;

    @Value("${yx.openApi.getInventory.method}")
    private String getInventoryMethod;

    @Value("${yx.openApi.getCallBackMethods.method}")
    private String getCallBackMethods;

    @Value("${yx.openApi.registerCallbackMethod.method}")
    private String registerCallbackMethod;
    
    @Value("${yx.openApi.orderRefundApply.method}")
    private String orderRefundApplyMethod;
    
    @Value("${yx.openApi.orderRefundCancel.method}")
    private String orderRefundCancelMethod;

    @Value("${yx.openApi.mockapi.path.prefix}")
    private String mockAPIPathPrefix;

    @Value("${yx.openApi.mockapi.callbackAuditCancelOrder.method}")
    private String callbackAuditCancelOrder;

    @Value("${yx.openApi.mockapi.callbackCancelOrder.method}")
    private String callbackCancelOrder;

    @Value("${yx.openApi.mockapi.callbackDeliveryOrder.method}")
    private String callbackDeliveryOrder;

    @Value("${yx.openApi.mockapi.callbackTransfer.method}")
    private String callbackTransfer;
    
    /**begin**/
    @Value("${yx.openApi.mockapi.callbackRefundAddress.method}")
    private String callbackRefundAddress;
    
    @Value("${yx.openApi.mockapi.callbackRefundReject.method}")
    private String callbackRefundReject;
    
    @Value("${yx.openApi.mockapi.callbackRefundExpressConfirm.method}")
    private String callbackRefundExpressConfirm;
    
    @Value("${yx.openApi.mockapi.callbackRefundSystemCancel.method}")
    private String callbackRefundSystemCancel;
    
    @Value("${yx.openApi.mockapi.callbackRefundResultDirectly.method}")
    private String callbackRefundResultDirectly;
    
    @Value("${yx.openApi.mockapi.callbackRefundResultWithExpress.method}")
    private String callbackRefundResultWithExpress;
    /**end**/
    
    @Value("${yx.openApi.getExpress.method}")
    private String getExpressMethod;
    
    @Value("${yx.openApi.orderRefundOfferExpress.method}")
    private String orderRefundOfferExpress;
    
    @Value("${yx.openApi.getOrderRefundDetail.method}")
    private String getOrderRefundDetail;
    
    @Value("${yx.openApi.getCategory.method}")
    private String getCategory;
    
    @Value("${yx.openApi.getSkuIdBatch.method}")
    private String getSkuIdBatch;
    
    @Value("${yx.openApi.getItemSku.method}")
    private String getItemSku;
    
//    @Value("${yx.openApi.path}")
//    private  String path;
//    
//    @Value("${yx.openApi.host}")
//    private  String yxOpenApiHost;
//    
//    @Value("${yx.openApi.appKey}")
//    private  String appKey;
//    
//    @Value("${yx.openApi.appSecret}")
//    private  String appSecret;

	public String getGetIdsMethod() {
		return getIdsMethod;
	}

	public void setGetIdsMethod(String getIdsMethod) {
		this.getIdsMethod = getIdsMethod;
	}

	public String getGetItemsMethod() {
		return getItemsMethod;
	}

	public void setGetItemsMethod(String getItemsMethod) {
		this.getItemsMethod = getItemsMethod;
	}

	public String getPayedOrderMethod() {
		return payedOrderMethod;
	}

	public void setPayedOrderMethod(String payedOrderMethod) {
		this.payedOrderMethod = payedOrderMethod;
	}

	public String getGetOrderMethod() {
		return getOrderMethod;
	}

	public void setGetOrderMethod(String getOrderMethod) {
		this.getOrderMethod = getOrderMethod;
	}

	public String getConfirmOrderMethod() {
		return confirmOrderMethod;
	}

	public void setConfirmOrderMethod(String confirmOrderMethod) {
		this.confirmOrderMethod = confirmOrderMethod;
	}

	public String getCancelOrderMethod() {
		return cancelOrderMethod;
	}

	public void setCancelOrderMethod(String cancelOrderMethod) {
		this.cancelOrderMethod = cancelOrderMethod;
	}

	public String getGetInventoryMethod() {
		return getInventoryMethod;
	}

	public void setGetInventoryMethod(String getInventoryMethod) {
		this.getInventoryMethod = getInventoryMethod;
	}

	public String getGetCallBackMethods() {
		return getCallBackMethods;
	}

	public void setGetCallBackMethods(String getCallBackMethods) {
		this.getCallBackMethods = getCallBackMethods;
	}

	public String getRegisterCallbackMethod() {
		return registerCallbackMethod;
	}

	public void setRegisterCallbackMethod(String registerCallbackMethod) {
		this.registerCallbackMethod = registerCallbackMethod;
	}

	public String getOrderRefundApplyMethod() {
		return orderRefundApplyMethod;
	}

	public void setOrderRefundApplyMethod(String orderRefundApplyMethod) {
		this.orderRefundApplyMethod = orderRefundApplyMethod;
	}

	public String getOrderRefundCancelMethod() {
		return orderRefundCancelMethod;
	}

	public void setOrderRefundCancelMethod(String orderRefundCancelMethod) {
		this.orderRefundCancelMethod = orderRefundCancelMethod;
	}

	public String getMockAPIPathPrefix() {
		return mockAPIPathPrefix;
	}

	public void setMockAPIPathPrefix(String mockAPIPathPrefix) {
		this.mockAPIPathPrefix = mockAPIPathPrefix;
	}

	public String getCallbackAuditCancelOrder() {
		return callbackAuditCancelOrder;
	}

	public void setCallbackAuditCancelOrder(String callbackAuditCancelOrder) {
		this.callbackAuditCancelOrder = callbackAuditCancelOrder;
	}

	public String getCallbackCancelOrder() {
		return callbackCancelOrder;
	}

	public void setCallbackCancelOrder(String callbackCancelOrder) {
		this.callbackCancelOrder = callbackCancelOrder;
	}

	public String getCallbackDeliveryOrder() {
		return callbackDeliveryOrder;
	}

	public void setCallbackDeliveryOrder(String callbackDeliveryOrder) {
		this.callbackDeliveryOrder = callbackDeliveryOrder;
	}

	public String getCallbackTransfer() {
		return callbackTransfer;
	}

	public void setCallbackTransfer(String callbackTransfer) {
		this.callbackTransfer = callbackTransfer;
	}

	public String getCallbackRefundAddress() {
		return callbackRefundAddress;
	}

	public void setCallbackRefundAddress(String callbackRefundAddress) {
		this.callbackRefundAddress = callbackRefundAddress;
	}

	public String getCallbackRefundReject() {
		return callbackRefundReject;
	}

	public void setCallbackRefundReject(String callbackRefundReject) {
		this.callbackRefundReject = callbackRefundReject;
	}

	public String getCallbackRefundExpressConfirm() {
		return callbackRefundExpressConfirm;
	}

	public void setCallbackRefundExpressConfirm(String callbackRefundExpressConfirm) {
		this.callbackRefundExpressConfirm = callbackRefundExpressConfirm;
	}

	public String getCallbackRefundSystemCancel() {
		return callbackRefundSystemCancel;
	}

	public void setCallbackRefundSystemCancel(String callbackRefundSystemCancel) {
		this.callbackRefundSystemCancel = callbackRefundSystemCancel;
	}

	public String getCallbackRefundResultDirectly() {
		return callbackRefundResultDirectly;
	}

	public void setCallbackRefundResultDirectly(String callbackRefundResultDirectly) {
		this.callbackRefundResultDirectly = callbackRefundResultDirectly;
	}

	public String getCallbackRefundResultWithExpress() {
		return callbackRefundResultWithExpress;
	}

	public void setCallbackRefundResultWithExpress(String callbackRefundResultWithExpress) {
		this.callbackRefundResultWithExpress = callbackRefundResultWithExpress;
	}

	public String getGetExpressMethod() {
		return getExpressMethod;
	}

	public void setGetExpressMethod(String getExpressMethod) {
		this.getExpressMethod = getExpressMethod;
	}

	public String getOrderRefundOfferExpress() {
		return orderRefundOfferExpress;
	}

	public void setOrderRefundOfferExpress(String orderRefundOfferExpress) {
		this.orderRefundOfferExpress = orderRefundOfferExpress;
	}

	public String getGetOrderRefundDetail() {
		return getOrderRefundDetail;
	}

	public void setGetOrderRefundDetail(String getOrderRefundDetail) {
		this.getOrderRefundDetail = getOrderRefundDetail;
	}

	public String getGetCategory() {
		return getCategory;
	}

	public void setGetCategory(String getCategory) {
		this.getCategory = getCategory;
	}

	public String getGetSkuIdBatch() {
		return getSkuIdBatch;
	}

	public void setGetSkuIdBatch(String getSkuIdBatch) {
		this.getSkuIdBatch = getSkuIdBatch;
	}

	public String getGetItemSku() {
		return getItemSku;
	}

	public void setGetItemSku(String getItemSku) {
		this.getItemSku = getItemSku;
	}
	
}
