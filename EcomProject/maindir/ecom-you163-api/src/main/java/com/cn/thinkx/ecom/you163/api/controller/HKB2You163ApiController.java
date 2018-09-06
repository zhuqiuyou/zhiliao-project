package com.cn.thinkx.ecom.you163.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.vo.ExpressInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.apply.ApplyInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APICategoryTO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemTO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderVO;

@RestController
@RequestMapping(value = "you163/api")
public class HKB2You163ApiController {

	@Autowired
	private YXOpenApiService yxOpenApiService;

	/**
	 * 商品ID查询接口(yanxuan.item.id.batch.get)
	 * 
	 * @return
	 */
	@GetMapping(value = "/getIds")
	public JsonResult getItemIds() {
		return yxOpenApiService.handleGetIds();
	}

	/**
	 * 商品信息查询接口(yanxuan.item.batch.get)
	 * 
	 * @param itemIds 
	 * <I>请求商品（SPU）ID列表</I><br><br>
	 * (用英文逗号分隔，例如：1000884,1000885；一次最多请求30个商品)
	 * @return
	 */
//	@GetMapping(value = "/getItems")
//	public JsonResult getItemsById(@RequestParam(value = "itemIds") final String itemIds) {
//		return yxOpenApiService.handleGetItemsById(itemIds);
//	}
	
	@GetMapping(value = "/getItems")
	public String getItemsById(@RequestParam(value = "itemIds") final String itemIds) {
		JsonResult result = yxOpenApiService.handleGetItemsById(itemIds);
		List<APIItemTO> list = JSONObject.parseArray(result.getResult().toString(),APIItemTO.class);
		return JSONObject.toJSONString(list);
	}
	
	/**
	 * 商品SKU库存查询接口(yanxuan.inventory.count.get)
	 * 
	 * @param skuIds
	 * 要查询的skuId列表
	 * 示例：10019005,10038001，一次最多请求100个
	 * @return
	 */
	@GetMapping("/getInventory")
    public JsonResult getInventory(@RequestParam(value = "skuIds") String skuIds){
        return yxOpenApiService.handleGetInventory(skuIds);
    }
	
	/**
	 * 渠道下单接口(yanxuan.order.paid.create)
	 * 
	 * @param orderVO
	 * 订单 JSON字符串
	 * @return
	 */
	@GetMapping("/payedOrder")
    public JsonResult payedOrder(OrderVO orderVO) {
        return yxOpenApiService.handlePayedOrder(orderVO);
    }
	
	/**
	 * 渠道取消订单接口(yanxuan.order.paid.cancel)
	 * 
	 * @param orderId 订单号
	 * @return
	 */
	@GetMapping("/cancelOrder")
    public JsonResult cancelOrder(@RequestParam(value = "orderId") String orderId) {
        return yxOpenApiService.handleCancelOrder(orderId);
    }
	
	/**
	 * 渠道订单确认收货接口(yanxuan.order.received.confirm)
	 * 
	 * @param orderId 订单号
	 * @param packageId 包裹号
	 * @param confirmTime 签收时间
	 * @return
	 */
	@GetMapping("/confirmedOrder")
    public JsonResult confirmOrder(
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "packageId", required = false) String packageId,
            @RequestParam(value = "confirmTime", required = false) String confirmTime) {
        return yxOpenApiService.handleConfirmOrder(orderId, packageId, confirmTime);
    }
	
	/**
	 * 渠道订单信息查询接口(yanxuan.order.paid.get)
	 * 
	 * @param orderId 订单号
	 * @return
	 */
	@GetMapping("/getOrder")
    public JsonResult getOrder(
            @RequestParam(value = "orderId") String orderId) {
        return yxOpenApiService.handleGetOrder(orderId);
    }
	
	/**
	 * 获取物流轨迹信息接口(yanxuan.order.express.get)
	 * 
	 * @param orderId 订单号 
	 * @param packageId 包裹号
	 * @param expressNo 物流单号
	 * @param expressCom 物流公司名称
	 * @return
	 */
	@GetMapping("/getExpress")
	public JsonResult getExpress(
			@RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "packageId", required = false) String packageId,
            @RequestParam(value = "expressNo") String expressNo,
            @RequestParam(value = "expressCom") String expressCom){
		return yxOpenApiService.handleGetExpress(orderId, packageId, expressNo,expressCom);
	}

	/**
	 * 渠道发起售后服务请求(yanxuan.order.refund.apply)
	 * 
	 * @param applyInfo
	 * @return
	 */
	@GetMapping(value = "orderRefundApply")
	public JsonResult handleOrderRefundApply(ApplyInfoVO applyInfo){
		return yxOpenApiService.handleOrderRefundApply(applyInfo);
	}
	
	/**
	 * 渠道取消售后服务请求(yanxuan.order.refund.cancel)
	 * 
	 * @param applyId
	 * @return
	 */
	@GetMapping(value = "orderRefundCancel")
	public JsonResult handleOrderRefundCancel(@RequestParam(value = "applyId") final String applyId){
		return yxOpenApiService.handleOrderRefundCancel(applyId);
	}

	/**
	 * 渠道自助注册回调(yanxuan.callback.method.register)
	 * 渠道通过这个请求告知严选需要通知的回调方法
	 * @param methods
	 * <I>需注册的回调方法名</I><br><br>
	 * <I>多个方法名用英文逗号分隔，覆盖原来所有方法(包括默认的方法)</I><br><br>
	 * @return
	 */
    @RequestMapping("/registerCallbackMethod")
    public JsonResult registerCallbackMethod(@RequestParam(value = "methods") final String methods) {
        return yxOpenApiService.handleRegisterCallbackMethod(methods);
    }
    
    
	/**
	 * 获取渠道已注册的回调方法名(yanxuan.callback.method.list)
	 * 获取渠道已注册的回调方法名，严选只会回调注册了的方法名
	 * @return
	 */
    @RequestMapping("/getCallBackMethods")
    public ModelAndView getCallBackMethods(HttpServletRequest req) {
    	ModelAndView mv = new ModelAndView("you163/manage/regmethods");
        JsonResult jsonResult= yxOpenApiService.handleGetCallBackMethods();
        req.setAttribute("methods", jsonResult.getResult().toString());
        return mv;
    }
    
    /**
     * 渠道绑定售后寄回物流单号(yanxuan.order.refund.offer.express)
     * 可覆盖旧的物流单号
     * 
     * @param applyId	售后申请id
     * @param expressInfo	物流信息
     * @return
     */
    @GetMapping("/orderRefundOfferExpress")
    public JsonResult OrderRefundOfferExpress(
    		@RequestParam(value = "applyId") String applyId,
    		@RequestParam(value = "expressInfo") List<ExpressInfoVO> expressInfo){
    	return yxOpenApiService.handleOrderRefundOfferExpress(applyId, expressInfo);
    }
    
    /**
     * 渠道查询售后申请详情(yanxuan.order.refund.detail.get)
     * 查询售后申请详情
     * 
     * @param applyId 售后申请id
     * @return
     */
    @GetMapping("/getOrderRefundDetail")
    public JsonResult getOrderRefundDetail(@RequestParam(value = "applyId") String applyId){
    	return yxOpenApiService.handleGetOrderRefundDetail(applyId);
    }
    
    @GetMapping("/getCategory")
    public JsonResult getCategory(){
    	JsonResult result = yxOpenApiService.handleGetCategory();
    	List<APICategoryTO> list = JSONObject.parseArray(result.getResult().toString(), APICategoryTO.class); 
    	System.out.println("***************************"+list.size());
    	return result;
    }
	
    @GetMapping(value = "/getSkuIdBatch")
	public JsonResult getSkuIdBatch(@RequestParam(value = "categoryId") final String categoryId) {
		return yxOpenApiService.handleGetSkuIdBatch(categoryId);
	}
    
    @GetMapping(value = "/getItemSku")
	public JsonResult getItemSku(@RequestParam(value = "skuId") final String skuId) {
		return yxOpenApiService.handleGetItemSku(skuId);
	}
}
