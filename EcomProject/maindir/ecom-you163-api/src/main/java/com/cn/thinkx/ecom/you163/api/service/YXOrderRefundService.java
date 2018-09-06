package com.cn.thinkx.ecom.you163.api.service;

import com.cn.thinkx.ecom.you163.api.meta.JsonResult;

/**
 * 订单退货回调
 * 
 * @author zhuqiuyou
 *
 */
public interface YXOrderRefundService {

	/**
	 * 退货地址回调(yanxuan.notification.order.refund.address)
	 * 
	 * @return
	 */
	JsonResult doRefundAddress(String refundAddress);
	
	/**
	 * 严选拒绝退货回调(yanxuan.notification.order.refund.reject)
	 * 
	 * @param rejectInfo
	 * @return
	 */
	JsonResult doRefundReject(String rejectInfo);
	
	/**
	 * 退货包裹确认收货回调(yanxuan.notification.order.refund.express.confirm)
	 * 
	 * @param expressConfirm
	 * @return
	 */
	JsonResult doRefundExpressConfirm(String expressConfirm);
	
	/**
	 * 严选系统取消退货回调(yanxuan.notification.order.refund.system.cancel)
	 * 
	 * @param systemCancel
	 * @return
	 */
	JsonResult doRefundSystemCancel(String systemCancel);
	
	/**
	 * 退款结果回调(yanxuan.notification.order.refund.result)
	 * 
	 * @param refundResult
	 * @return
	 */
	JsonResult doRefundResultDirectly(String refundResult);
	
}
