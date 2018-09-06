package com.cn.thinkx.ecom.you163.api.service;

import com.cn.thinkx.ecom.you163.api.meta.JsonResult;

/**
 * 库存回调
 * 
 * @author kpplg
 *
 */
public interface YXInventoryNotificationService {

	/**
	 * SKU库存划拨回调(yanxuan.notification.inventory.count.changed)
	 * 
	 * @param skuTransfer
	 * @return
	 */
	JsonResult changedInventoryCount(String skuTransfer);

	/**
	 * SKU库存校准回调(yanxuan.notification.inventory.count.check)
	 * 
	 * @param skuCheck
	 * @return
	 */
	JsonResult inventoryCountCheck(String skuCheck);

	/**
	 * 渠道SKU低库存预警通知(yanxuan.notification.sku.alarm.close)
	 * 
	 * @param SkuCloseAlarmVO
	 * @return
	 */
	JsonResult skuAlarmClose(String closeAlarmSkus);

	/**
	 * 渠道SKU再次开售通知(yanxuan.notification.sku.reopen)
	 * 
	 * @param reopenSkus
	 * @return
	 */
	JsonResult skuReopen(String reopenSkus);
}
