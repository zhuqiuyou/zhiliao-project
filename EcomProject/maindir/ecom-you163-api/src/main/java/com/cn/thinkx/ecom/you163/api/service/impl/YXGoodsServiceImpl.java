package com.cn.thinkx.ecom.you163.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.cn.thinkx.ecom.basics.goods.domain.SpecValues;
import com.cn.thinkx.ecom.basics.goods.domain.Specification;
import com.cn.thinkx.ecom.basics.goods.service.CatGoodsRoleService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsCategoryService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsSpecService;
import com.cn.thinkx.ecom.basics.goods.service.SpecValuesService;
import com.cn.thinkx.ecom.basics.goods.service.SpecificationService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXGoodsService;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.vo.ChannelSkuInventoryOutVO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APICategoryPathTO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemSkuSpecValueTO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemTO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APISkuTO;

@Service("yxGoodsService")
public class YXGoodsServiceImpl implements YXGoodsService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GoodsService goodsService;

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	@Autowired
	private CatGoodsRoleService catGoodsRoleService;

	@Autowired
	private GoodsProductService goodsProductService;

	@Autowired
	private GoodsSpecService goodsSpecService;

	@Autowired
	private SpecificationService specificationService;

	@Autowired
	private SpecValuesService specValuesService;

	@Autowired
	private YXOpenApiService yxOpenApiService;
	
	@Override
	public String insertGoods(APIItemTO item) {
		BaseResult result = new BaseResult();
		result.setCode(ExceptionEnum.ERROR_CODE);
		result.setMsg(ExceptionEnum.ERROR_MSG);
		GoodsCategory category = null;
		Goods goods = null;
		GoodsProduct goodsProduct = null;
		GoodsSpec goodsSpec = null;
		Specification specification = null;
		SpecValues specValues = null;
		JsonResult inventory = null;
		JSONArray inventoryResult = null;
		JSONObject resultObj = null;
		ChannelSkuInventoryOutVO inventoryOutVO = null;
		try {
			List<List<APICategoryPathTO>> categoryPathList = item.getCategoryPathList();
			
			List<String> catIds=new ArrayList<>();
			if (categoryPathList != null && categoryPathList.size() > 0) {
				
				for(List<APICategoryPathTO> categoryList :categoryPathList){
					category = new GoodsCategory();
					category.setCatName(categoryList.get(0).getId());
					category.setCatPath("1");
					category.setParentId("0");
					category.setEcomCode(Constants.GoodsEcomCodeType.ECOM01.getCode());
					category = goodsCategoryService.insertGoodsCategory(category); // 添加一级分类
					if (category == null) {
						return JSONObject.toJSONString(result);
					}
					String ParentId=category.getCatId();
					if (categoryList.size() > 1) {
						category = new GoodsCategory();
						category.setCatName(categoryList.get(1).getId());
						category.setCatPath("2");
						category.setParentId(ParentId);
						category.setEcomCode(Constants.GoodsEcomCodeType.ECOM01.getCode());
						category = goodsCategoryService.insertGoodsCategory(category);// 添加二级分类
						if (category == null) {
							return JSONObject.toJSONString(result);
						}
						catIds.add(category.getCatId()); //添加二级分类的
					}
				}
			}

			List<APISkuTO> skuList = item.getSkuList();
			// 查询商品是否存在
			goods = goodsService.getGoodsByEcomCode(String.valueOf(item.getId()), Constants.GoodsEcomCodeType.ECOM01.getCode());
			if (goods == null) {
				goods = new Goods();
				goods.setGoodsName(item.getName());
				goods.setSpuCode(String.valueOf(item.getId()));
				goods.setEcomCode(Constants.GoodsEcomCodeType.ECOM01.getCode());
//				if (categoryPathList != null && categoryPathList.size() > 0) {
//					goods.setCatId(category.getCatId());
//				}
				goods.setGoodsImg(item.getListPicUrl());
				if(!StringUtil.isNullOrEmpty(String.valueOf(item.getPrimarySkuId()))){
					goods.setDefaultSkuCode(String.valueOf(item.getPrimarySkuId()));
				}else{
					goods.setDefaultSkuCode(String.valueOf(skuList.get(0).getId()));
				}
				if (skuList.size() == 1) {
					goods.setHaveSpec("0");
				} else {
					goods.setHaveSpec("1");
				}
				int i = goodsService.insert(goods);// 添加商品
				if (i != 1) {
					return JSONObject.toJSONString(result);
				}
			}
			
			//添加商品分类关联关系
			catGoodsRoleService.saveCatGoodsRole(catIds, goods.getGoodsId());
			

			for (APISkuTO itemSkuVO : skuList) {
				
				goodsProduct  = goodsProductService.getGoodsProductBySkuCode(String.valueOf(item.getId()), String.valueOf(itemSkuVO.getId()), Constants.GoodsEcomCodeType.ECOM01.getCode());
				if(goodsProduct == null){
					goodsProduct = new GoodsProduct();
					inventory = yxOpenApiService.handleGetInventory(String.valueOf(itemSkuVO.getId()));// 同步货品库存
					inventoryResult = JSONArray.parseArray((String) inventory.getResult());
					resultObj = (JSONObject)inventoryResult.get(0);
					inventoryOutVO = JSONObject.parseObject(JSONObject.toJSONString(resultObj),ChannelSkuInventoryOutVO.class);
					goodsProduct.setGoodsId(goods.getGoodsId());
					goodsProduct.setSpuCode(String.valueOf(item.getId()));
					goodsProduct.setSkuCode(String.valueOf(itemSkuVO.getId()));
					goodsProduct.setEcomCode(Constants.GoodsEcomCodeType.ECOM01.getCode());
					goodsProduct.setIsStore(Long.valueOf(inventoryOutVO.getInventory()));
					goodsProduct.setEnableStore(Long.valueOf(inventoryOutVO.getInventory()));
					goodsProduct.setGoodsPrice(NumberUtils.RMBYuanToCent(String.valueOf(itemSkuVO.getChannelPrice())));
					goodsProduct.setGoodsCost(NumberUtils.RMBYuanToCent(String.valueOf(itemSkuVO.getChannelPrice())));
					goodsProduct.setMktprice(NumberUtils.RMBYuanToCent(String.valueOf(itemSkuVO.getChannelPrice())));
//					goodsProduct.setMktprice(NumberUtils
//							.RMBYuanToCent(String.valueOf(itemSkuVO.getChannelPrice().multiply(new BigDecimal(1.2)))));
					goodsProduct.setPageTitle(itemSkuVO.getDisplayString());
					goodsProduct.setMetaDescription(item.getSimpleDesc());
					goodsProduct.setPicurl(itemSkuVO.getPicUrl());

					int g = goodsProductService.insert(goodsProduct);//添加货品

					if (g != 1) {
						return JSONObject.toJSONString(result);
					}
					
					for (APIItemSkuSpecValueTO specInfoVO : itemSkuVO.getItemSkuSpecValueList()) {
						specification = new Specification();
						specification.setSpecName(specInfoVO.getSkuSpec().getName());
						specification.setIsDel("0");
						//添加规格
						specification = specificationService.saveSpecification(specification);
						
						specValues = new SpecValues();
						specValues.setSpecId(specification.getSpecId());
						specValues.setSpecValue(specInfoVO.getSkuSpecValue().getValue());
						specValues.setSpecImage(specInfoVO.getSkuSpecValue().getPicUrl());
						//添加规格值
						specValues = specValuesService.saveSpecValues(specValues);
						
						goodsSpec = new GoodsSpec();
						goodsSpec.setGoodsId(goods.getGoodsId());
						goodsSpec.setProductId(goodsProduct.getProductId());
						goodsSpec.setSpecId(specification.getSpecId());
						goodsSpec.setSpecValueId(specValues.getSpecValueId());
						//添加商品、货品、规格、规格值中间数据
						int s = goodsSpecService.insert(goodsSpec);
						if(s!=1){
							return JSONObject.toJSONString(result);
						}
					}
				}
			}
			result.setCode(ExceptionEnum.SUCCESS_CODE);
			result.setMsg(ExceptionEnum.SUCCESS_MSG);
		} catch (Exception e) {
			logger.error(" ## 同步保存商品信息出错 ",e);
			result.setCode(ExceptionEnum.ERROR_CODE);
			result.setMsg(ExceptionEnum.ERROR_MSG);
		}

		return JSONObject.toJSONString(result);
	}

}
