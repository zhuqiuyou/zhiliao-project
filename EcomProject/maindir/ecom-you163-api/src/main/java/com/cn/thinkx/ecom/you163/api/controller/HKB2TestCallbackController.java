package com.cn.thinkx.ecom.you163.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;

@RestController
@RequestMapping(value = "you163/test")
public class HKB2TestCallbackController {
//
//	@Autowired
//	private YXOpenApiService yxOpenApiService;
//
//
//		/**
//		 * 1. 触发审核取消订单回调
//		 * @param orderId
//		 * @param cancel
//		 * @return
//		 */
//	    @RequestMapping("/callbackAuditCancelOrder")
//	    @ResponseBody
//	    public JsonResult callbackAuditCancelOrder(String orderId, boolean cancel) {
//	        return yxOpenApiService.handleCallbackAuditCancelOrder(orderId, cancel);
//	    }
//
//	    /**
//	     * 2. 触发异常取消订单回调
//	     * @param orderId
//	     * @return
//	     */
//	    @RequestMapping("/callbackCancelOrder")
//	    @ResponseBody
//	    public JsonResult callbackCancelOrder(String orderId) {
//	        return yxOpenApiService.handleCallbackCancelOrder(orderId);
//	    }
//
//	    /**
//	     * 3. 触发包裹物流绑单回调
//	     * @param orderId
//	     * @return
//	     */
//	    @RequestMapping("/callbackDeliveryOrder")
//	    @ResponseBody
//	    public JsonResult callbackDeliveryOrder(String orderId) {
//	        return yxOpenApiService.handleCallbackDeliveryOrder(orderId);
//	    }
//
//	    /**
//	     * 4. 触发SKU库存划拨回调
//	     * @param skuId
//	     * @param count
//	     * @return
//	     */
//	    @RequestMapping("/callbackTransfer")
//	    @ResponseBody
//	    public JsonResult callbackTransfer(long skuId, int count) {
//	        return yxOpenApiService.handleCallbackTransfer(skuId, count);
//	    }
//	    
//	    /**
//	     * 5. 触发退货地址回调
//	     * @param applyId
//	     * @param type
//	     * @return
//	     */
//	    @RequestMapping("/callbackRefundAddress")
//	    @ResponseBody
//	    public JsonResult callbackRefundAddress(String applyId, int type) {
//	        return yxOpenApiService.handleCallbackRefundAddress(applyId, type);
//	    }
//	    
//	    /**
//	     * 6. 触发严选拒绝退货回调
//	     * @param applyId
//	     * @return
//	     */
//	    @RequestMapping("/callbackRefundReject")
//	    @ResponseBody
//	    public JsonResult callbackRefundReject(String applyId) {
//	        return yxOpenApiService.handleCallbackRefundReject(applyId);
//	    }
//	    
//	    /**
//	     * 7. 触发退货包裹确认收货回调
//	     * @param applyId
//	     * @return
//	     */
//	    @RequestMapping("/callbackRefundExpressConfirm")
//	    @ResponseBody
//	    public JsonResult callbackRefundExpressConfirm(String applyId) {
//	        return yxOpenApiService.handleCallbackRefundExpressConfirm(applyId);
//	    }
//	    
//	    /**
//	     * 8. 触发严选系统取消退货回调
//	     * @param applyId
//	     * @return
//	     */
//	    @RequestMapping("/callbackRefundSystemCancel")
//	    @ResponseBody
//	    public JsonResult callbackRefundSystemCancel(String applyId) {
//	        return yxOpenApiService.handleCallbackRefundSystemCancel(applyId);
//	    }
//	    
//	    /**
//	     * 9. 触发直接退款结果回调
//	     * @param applyId
//	     * @return
//	     */
//	    @RequestMapping("/callbackRefundResultDirectly")
//	    @ResponseBody
//	    public JsonResult callbackRefundResultDirectly(String applyId) {
//	        return yxOpenApiService.handleCallbackRefundResultDirectly(applyId);
//	    }
//	    
//	    /**
//	     * 10. 触发退货包裹确认收货后退款结果回调
//	     * @param applyId
//	     * @param allApproved
//	     * @return
//	     */
//	    @RequestMapping("/callbackRefundResultWithExpress")
//	    @ResponseBody
//	    public JsonResult callbackRefundResultWithExpress(String applyId, boolean allApproved) {
//	        return yxOpenApiService.handleCallbackRefundResultWithExpress(applyId, allApproved);
//	    }
//
//	
}
