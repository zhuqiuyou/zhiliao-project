package com.cn.thinkx.ecom.you163.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXInventoryNotificationService;
import com.cn.thinkx.ecom.you163.notify.domain.inventory.SkuCheck;
import com.cn.thinkx.ecom.you163.notify.domain.inventory.SkuCheckVO;
import com.cn.thinkx.ecom.you163.notify.domain.inventory.SkuCloseAlarmVO;
import com.cn.thinkx.ecom.you163.notify.domain.inventory.SkuReopenVO;
import com.cn.thinkx.ecom.you163.notify.domain.inventory.SkuTransfer;
import com.cn.thinkx.ecom.you163.notify.domain.inventory.SkuTransferVO;

@Service("yxInventoryNotificationService")
public class YXInventoryNotificationServiceImpl implements YXInventoryNotificationService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoodsProductService goodsProductService;

	@Override
	public JsonResult changedInventoryCount(String skuTransfer) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(skuTransfer)) {
			jsonResult.setCode(201);
			jsonResult.setMessage("输入参数为空");
			logger.error("## 输入参数为空，skuTransfer:[{}]", skuTransfer);
			return jsonResult;
		}
		try {
			SkuTransfer sku = JSONArray.parseObject(skuTransfer, SkuTransfer.class);
			GoodsProduct gp = new GoodsProduct();
			List<SkuTransferVO> skuVOList = sku.getSkuTransfers();
			for (SkuTransferVO skuTransferVO : skuVOList) {
				gp.setSkuCode(String.valueOf(skuTransferVO.getSkuId()));
				gp.setIsStore(Long.valueOf(skuTransferVO.getTransferCount()));
				gp.setEnableStore(Long.valueOf(skuTransferVO.getTransferCount()));
				int i = goodsProductService.updateBySkuCode(gp); // 更新库存
				if (i < 0) {
					jsonResult.setCode(201);
					jsonResult.setMessage("SKU库存划拨回调出错");
					logger.error("## SKU库存回调,更改库存异常，skuCode:[{}]", skuTransferVO.getSkuId());
					return jsonResult;
				}
			}
		} catch (Exception e) {
			jsonResult.setCode(201);
			jsonResult.setMessage("SKU库存划拨回调出错");
			logger.error("## SKU库存回调异常,严选传入参数是：", skuTransfer);
			return jsonResult;
		}
		return jsonResult;
	}

	@Override
	public JsonResult inventoryCountCheck(String skuCheck) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(skuCheck)) {
			jsonResult.setCode(201);
			jsonResult.setMessage("输入参数为空");
			logger.error("## 输入参数为空，skuCheck:[{}]", skuCheck);
			return jsonResult;
		}
		try {
			SkuCheck sku = JSONArray.parseObject(skuCheck, SkuCheck.class);
			List<SkuCheckVO> skuVOList = sku.getSkuChecks();
			GoodsProduct gp = new GoodsProduct();
			for (SkuCheckVO skuCheckVO : skuVOList) {
				gp.setSkuCode(String.valueOf(skuCheckVO.getSkuId()));
				gp.setIsStore(Long.valueOf(skuCheckVO.getCount()));
				gp.setEnableStore(Long.valueOf(skuCheckVO.getCount()));
				int i = goodsProductService.updateBySkuCode(gp);
				if (i < 0) {
					jsonResult.setCode(201);
					jsonResult.setMessage("SKU库存校准回调出错");
					logger.error("## SKU库存校准回调,更改库存异常，skuCode:[{}]", skuCheckVO.getSkuId());
					return jsonResult;
				}
			}
		} catch (Exception e) {
			jsonResult.setCode(201);
			jsonResult.setMessage("SKU库存校准回调,出错");
			logger.error("## SKU库存校准回调,异常,严选传入参数是：", skuCheck, e);
			return jsonResult;
		}
		return jsonResult;
	}

	@Override
	public JsonResult skuAlarmClose(String closeAlarmSkus) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(closeAlarmSkus)) {
			jsonResult.setCode(201);
			jsonResult.setMessage("输入参数为空");
			logger.error("## 输入参数为空，closeAlarmSkus:[{}]", closeAlarmSkus);
			return jsonResult;
		}
		try {
			List<SkuCloseAlarmVO> skuCloseAlarmList = new ArrayList<>();
			JSONArray alarmSkusResult = JSON.parseArray(closeAlarmSkus);
			skuCloseAlarmList = JSONObject.parseArray(alarmSkusResult.toString(), SkuCloseAlarmVO.class);
			if (skuCloseAlarmList.size() > 0) {
				GoodsProduct gp = new GoodsProduct();
				for (SkuCloseAlarmVO skuCloseAlarmVO : skuCloseAlarmList) {
					gp.setSkuCode(String.valueOf(skuCloseAlarmVO.getSkuId()));
					gp.setIsStore(Long.valueOf(skuCloseAlarmVO.getInventory()));
					gp.setEnableStore(Long.valueOf(skuCloseAlarmVO.getInventory()));
					int i = goodsProductService.updateBySkuCode(gp);
					if (i < 0) {
						jsonResult.setCode(201);
						jsonResult.setMessage("渠道SKU低库存预警通知出错");
						logger.error("## 渠道SKU低库存预警通知,更改库存异常，skuCode:[{}]", skuCloseAlarmVO.getSkuId());
						return jsonResult;
					}
				}
			}
		} catch (Exception e) {
			jsonResult.setCode(201);
			jsonResult.setMessage("渠道SKU低库存预警通知异常");
			logger.error("## 渠道SKU低库存预警通知异常,严选传入参数是：", closeAlarmSkus, e);
			return jsonResult;
		}
		return jsonResult;
	}

	@Override
	public JsonResult skuReopen(String reopenSkus) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(reopenSkus)) {
			jsonResult.setCode(201);
			jsonResult.setMessage("输入参数为空");
			logger.error("## 输入参数为空，reopenSkus:[{}]", reopenSkus);
			return jsonResult;
		}
		try {
			List<SkuReopenVO> skuCloseAlaList = new ArrayList<>();
			JSONArray alarmSkusResult = JSON.parseArray(reopenSkus);
			skuCloseAlaList = JSONObject.parseArray(alarmSkusResult.toString(), SkuReopenVO.class);
			if (skuCloseAlaList.size() > 0) {
				GoodsProduct gp = new GoodsProduct();
				for (SkuReopenVO skuReopenVO : skuCloseAlaList) {
					gp.setSkuCode(String.valueOf(skuReopenVO.getSkuId()));
					gp.setIsStore(Long.valueOf(skuReopenVO.getInventory()));
					gp.setEnableStore(Long.valueOf(skuReopenVO.getInventory()));
					int i = goodsProductService.updateBySkuCode(gp);
					if (i < 0) {
						jsonResult.setCode(201);
						jsonResult.setMessage("渠道SKU再次开售通知异常");
						logger.error("## 渠道SKU再次开售通知,更改库存异常，skuCode:[{}]", skuReopenVO.getSkuId());
						return jsonResult;
					}
				}
			}
		} catch (Exception e) {
			jsonResult.setCode(201);
			jsonResult.setMessage("渠道SKU再次开售通知异常");
			logger.error("## 渠道SKU再次开售通知异常,严选传入参数是reopenSkus：[{}]", reopenSkus, e);
			return jsonResult;
		}
		return jsonResult;
	}

}
