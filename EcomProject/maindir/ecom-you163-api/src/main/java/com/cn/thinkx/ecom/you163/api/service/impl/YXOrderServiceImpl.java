package com.cn.thinkx.ecom.you163.api.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.basics.order.domain.OrderExpressPlatf;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.basics.order.service.ExceptionInfService;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.basics.order.service.OrderShipService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.basics.order.service.SellbackGoodslistService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.exceptionType;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.service.YXOrderService;
import com.cn.thinkx.ecom.you163.api.utils.DateUtils;
import com.cn.thinkx.ecom.you163.api.utils.YXConstUtils;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderOutVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderPackageVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderSkuVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderVO;
import com.cn.thinkx.ecom.you163.notify.domain.order.ExceptionInfo;
import com.cn.thinkx.ecom.you163.notify.domain.order.ExpressDetailInfo;
import com.cn.thinkx.ecom.you163.notify.domain.order.OrderCancelResult;
import com.cn.thinkx.ecom.you163.notify.domain.order.OrderPackage;

@Service("yxOrderService")
public class YXOrderServiceImpl implements YXOrderService {
	private Logger logger = LoggerFactory.getLogger(YXOrderServiceImpl.class);

	@Autowired
	private YXOpenApiService yxOpenApiService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private OrderProductItemService orderProductItemService;

	@Autowired
	private ReturnsOrderService returnsOrderService;

	@Autowired
	private OrderRefundService orderRefundService;

	@Autowired
	private YXOrderService yxOrderService;

	/**
	 * 订单收货地址service
	 */
	@Autowired
	private OrderShipService orderShipService;

	@Autowired
	private ExpressPlatfService expressPlatfService;

	@Autowired
	private OrderExpressPlatfService orderExpressPlatfService;

	@Autowired
	private SellbackGoodslistService sellbackGoodslistService;
	
	@Autowired
	private ExceptionInfService exceptionInfService;
	
	@Autowired
	private PlatfOrderService platfOrderService;

	/**
	 * 创建网易优选订单
	 * 
	 * @param sOrderId
	 *            商城二级订单号
	 * @return
	 * @throws Exception
	 */
	public JsonResult doCreateOrder(PlatfShopOrder platfShopOrder) throws Exception {

		// PlatfShopOrder
		// platfShopOrder=platfShopOrderService.selectByPrimaryKey(sOrderId);

		/**
		 * 查找订单的收货地址
		 */
		OrderShip orderShip = orderShipService.getOrderShipByOrderId(platfShopOrder.getOrderId());
		OrderVO orderVO = new OrderVO();
		orderVO.setOrderId(platfShopOrder.getsOrderId()); // 二级订单号
		orderVO.setSubmitTime(DateUtils.parseDateToString(DateUtils.addMinute(new Date(), -2), null)); // 用户下单时间
		orderVO.setPayTime(DateUtils.parseDateToString(DateUtils.addMinute(new Date(), -1), null)); // 用户的支付时间
		orderVO.setReceiverName(orderShip.getShipName()); // 收件人姓名
		orderVO.setReceiverMobile(orderShip.getShipMobile()); // 收件人手机号
		orderVO.setReceiverPhone(orderShip.getShipMobile());
		orderVO.setReceiverProvinceName(orderShip.getShipProvinceName()); // 省
		orderVO.setReceiverCityName(orderShip.getShipCityName());// 市
		orderVO.setReceiverDistrictName(orderShip.getShipRegionName());// 区（县）
		orderVO.setReceiverAddressDetail(orderShip.getShipAddr()); // 详细地址
		BigDecimal realPrice = NumberUtils.RMBCentToYuanForDecimal(platfShopOrder.getPayAmt()); // 订单金额
		BigDecimal expFee = NumberUtils.RMBCentToYuanForDecimal(platfShopOrder.getShippingFreightPrice()); // 配送费用
		orderVO.setRealPrice(NumberUtils.RMBaddMoneyToDecimal(realPrice, expFee)); // 订单金额
																					// 包含邮费
		orderVO.setExpFee(expFee); // 运费金额

		orderVO.setPayMethod("HKBPAY");
		// 订单SKU列表
		List<OrderSkuVO> orderSkus = new ArrayList<OrderSkuVO>();

		// 查询订单的货品明细
		OrderProductItem respItems = new OrderProductItem();
		respItems.setsOrderId(platfShopOrder.getsOrderId());
		List<OrderProductItem> orderSkuItmes = orderProductItemService.getOrderProductItemList(respItems);

		OrderSkuVO orderSku = null;
		// 遍历订单明细
		for (OrderProductItem productItem : orderSkuItmes) {
			orderSku = new OrderSkuVO();
			orderSku.setSkuId(productItem.getSkuCode());
			orderSku.setProductName(productItem.getGoodsName());
			orderSku.setSaleCount(Integer.parseInt(productItem.getProductNum()));// 货品数量
			orderSku.setOriginPrice(new BigDecimal(NumberUtils.RMBCentToYuan(productItem.getProductPrice()))); // 货品售价
			orderSku.setSubtotalAmount(
					NumberUtils.getSubtotalAmountYUAN(productItem.getProductPrice(), productItem.getProductNum())); // 小计
			orderSkus.add(orderSku);
		}
		orderVO.setOrderSkus(orderSkus);

		/**
		 * 网易优选下单操作
		 */
		JsonResult jsonResult = yxOpenApiService.handlePayedOrder(orderVO);
		if ("200".equals(jsonResult.getCode())) {
			OrderOutVO orderOut = JSONArray.parseObject(jsonResult.getResult().toString(), OrderOutVO.class);
			yxOrderService.doOrderPackageStates(orderOut);
			//修改渠道二级订单
			platfShopOrder.setSubOrderStatus(Constants.SubOrderStatus.SOS10.getCode()); // 等待严选发货
			try{
				platfShopOrder.setChnlOrderPrice(NumberUtils.RMBYuanToCent(orderVO.getRealPrice().toString()));
				platfShopOrder.setChnlOrderPostage(NumberUtils.RMBYuanToCent(orderVO.getExpFee().toString()));
			}catch(Exception ex){
				logger.error("#渠道订单返回成功，格式转换失败-->",ex);
			}
		}else{
			platfShopOrder.setSubOrderStatus(Constants.SubOrderStatus.SOS44.getCode()); // 严选发货失败
			exceptionInfService.saveExceptionInf(orderVO.getOrderId(), orderVO.getOrderId(), exceptionType.ET1000.getCode(), jsonResult.getMessage());
		}

		platfShopOrder.setRemarks(jsonResult.getMessage());
		platfShopOrderService.updateByPrimaryKey(platfShopOrder);
		
		return jsonResult;
	}

	/**
	 * 严选接到取消通知，发起取消订单回调
	 * 
	 */
	@Override
	public JsonResult cancelOrder(String orderCancelResult) {
		if (StringUtil.isNullOrEmpty(orderCancelResult))
			return null;
		OrderCancelResult orderResult = JSONArray.parseObject(orderCancelResult, OrderCancelResult.class);
		try {
		/** 门店订单表 */
		PlatfShopOrder pso = platfShopOrderService.selectByPrimaryKey(orderResult.getOrderId());

		/** 订单退换货申请表 */
		List<ReturnsOrder> returnsOrderList = returnsOrderService.getReturnsOrderBySorderId(orderResult.getOrderId());
		ReturnsOrder returnsOrder = returnsOrderList.get(0);
		logger.info("## 严选取消订单回调----->订单退换货申请returnsOrder:[{}]",JSON.toJSONString(returnsOrder));
		
		switch (orderResult.getCancelStatus()) {
		case 0: // 0:不允许取消
			pso.setSubOrderStatus(Constants.SubOrderStatus.SOS28.getCode()); // 取消被拒
			if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
				returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS1.getCode()); // 审核状态：已拒绝
				returnsOrderService.updateByPrimaryKey(returnsOrder);
			}
			break;
		case 1: // 允许取消
			/** 门店订单表 */
			pso.setSubOrderStatus(Constants.SubOrderStatus.SOS27.getCode()); // 已取消
			if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
				// 判断一级订单下的所有二级订单是否都是已取消状态
				List<PlatfShopOrder> psoList = platfShopOrderService.getPlatfShopOrderByOIdAndNotStatus(pso.getOrderId(),Constants.SubOrderStatus.SOS27.getCode());
				if (psoList.size() == 0) {
					PlatfOrder po = new PlatfOrder();
					po.setOrderId(pso.getOrderId());
					po.setPayStatus(Constants.PlatfOrderPayStat.PayStat08.getCode()); // 已取消
					platfOrderService.updateByPrimaryKey(po);
				}
				/** 订单退换货申请表 */
				returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS2.getCode()); // 审核状态：已同意
				if (returnsOrderService.updateByPrimaryKey(returnsOrder) > 0) {
					/** 退款表 */
					orderRefundService.saveOrderRefundInf(returnsOrder.getReturnsId(), pso);
					/** 调用退款 */
					platfShopOrderService.doHkbStoreRefund(pso);
					
				}
			}
			break;
		case 2: // 待审核
			pso.setSubOrderStatus(Constants.SubOrderStatus.SOS26.getCode()); // 申请取消待审核
			platfShopOrderService.updateByPrimaryKey(pso);
			break;
		default:
			break;
		}
		} catch (Exception e) {
			logger.error("## 严选接到取消通知，发起取消订单回调---> 修改数据库订单相关表异常,网易严选  ”取消订单“ 返回值[{}]", orderCancelResult, e);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		return jsonResult;
	}

	/**
	 * 订单包裹物流绑单回调
	 */
	@Override
	public JsonResult deliveredOrder(String orderPackage) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(orderPackage)) {
			return null;
		}
		OrderPackage orderPack = JSONArray.parseObject(orderPackage, OrderPackage.class);
		/** 门店订单表 */
		PlatfShopOrder pso = new PlatfShopOrder();
		pso.setsOrderId(orderPack.getOrderId());

		// 订单包裹物流信息
		ExpressPlatf ep = new ExpressPlatf();
		ep.setsOrderId(orderPack.getOrderId());
		ep.setPackageNo(String.valueOf(orderPack.getPackageId()));
		ep.setPackageStat(Constants.PackageStatus.PACK_STAT10.getCode());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ep.setDeliveryTime(sdf.format(Long.parseLong(String.valueOf(orderPack.getExpCreateTime()))));
		ep.setEcomCode(Constants.GoodsEcomCodeType.ECOM01.getCode());
		List<ExpressDetailInfo> expressDetailInfos = orderPack.getExpressDetailInfos();
//		logger.info("## 订单包裹物流绑单回调，修改物流信息传入参数expressDetailInfos：[{}]",JSON.toJSONString(expressDetailInfos));
		try {
			for (ExpressDetailInfo expressDetailInfoVO : expressDetailInfos) {
				ep.setExpressCompanyName(expressDetailInfoVO.getExpressCompany());
				ep.setExpressNo(expressDetailInfoVO.getExpressNo());
//				logger.info("## 订单包裹物流绑单回调，修改物流信息传入参数ep：[{}]",JSON.toJSONString(ep));
				ep = expressPlatfService.saveExpressPlatfForPackageNo(ep);
//				logger.info("## 订单包裹物流绑单回调，修改物流信息后数据ep：[{}]",JSON.toJSONString(ep));
				if (!StringUtil.isNullOrEmpty(ep.getPackId())) {
					pso.setSubOrderStatus(Constants.SubOrderStatus.SOS12.getCode()); // 已发货
					platfShopOrderService.updateByPrimaryKey(pso);
				}
			}
		} catch (Exception e) {
			logger.error("##订单包裹物流绑单回调 ，网易严选  ”yanxuan.notification.order.delivered“ 返回值[{}],[{}]", orderPackage, e);
			return null;
		}
		
		return jsonResult;
	}

	/**
	 * 订单异常回调(渠道发起的取消订单)
	 */
	@Override
	public JsonResult exceptionalOrder(String exceptionInfo) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(exceptionInfo))
			return null;
		try {
			ExceptionInfo exceptionResult = JSONArray.parseObject(exceptionInfo, ExceptionInfo.class);
			/** 门店订单表 */
			PlatfShopOrder pso = platfShopOrderService.selectByPrimaryKey(exceptionResult.getOrderId());

			pso.setSubOrderStatus(Constants.SubOrderStatus.SOS27.getCode()); // 取消
			if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {

				/** 订单退换货申请表 */
				ReturnsOrder returnsOrder = new ReturnsOrder();
				returnsOrder.setsOrderId(pso.getsOrderId());
				returnsOrder.setMemberId(pso.getMemberId());
				returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS2.getCode()); // 审核状态：已同意
				returnsOrder.setReturnsType(Constants.ReturnsType.RT2.getCode()); // 用户申请取消
				returnsOrder.setAddTime("");
				returnsOrderService.insert(returnsOrder);

				/** 订单货品明细表 */
				OrderProductItem opi = new OrderProductItem();
				opi.setsOrderId(exceptionResult.getOrderId());
				List<OrderProductItem> orderProductItemList = orderProductItemService.getOrderProductItemList(opi);
				/** 退货商品表 */
				for (OrderProductItem orderProductItem : orderProductItemList) {
					SellbackGoodslist sg = new SellbackGoodslist();
					sg.setsOrderId(pso.getsOrderId());
					sg.setReturnsId(returnsOrder.getReturnsId());
					sg.setoItemId(orderProductItem.getoItemId());
					sg.setShipNum(orderProductItem.getProductNum());
					sg.setReturnNum(orderProductItem.getProductNum());
					sellbackGoodslistService.insert(sg);
				}

				/** 退款表 */
				orderRefundService.saveOrderRefundInf(returnsOrder.getReturnsId(), pso);
				
				// 判断一级订单下的所有二级订单是否都是已取消状态
				List<PlatfShopOrder> psoList = platfShopOrderService.getPlatfShopOrderByOIdAndNotStatus(pso.getOrderId(),Constants.SubOrderStatus.SOS27.getCode());
				if (psoList.size() == 0) {
					PlatfOrder po = new PlatfOrder();
					po.setOrderId(pso.getOrderId());
					po.setPayStatus(Constants.PlatfOrderPayStat.PayStat08.getCode()); // 已取消
					platfOrderService.updateByPrimaryKey(po);
				}
				
				/** 调用退款 */
				platfShopOrderService.doHkbStoreRefund(pso);
				
			}
		} catch (Exception e) {
			logger.error("##订单异常回调 ---> 修改数据库订单相关表异常[{}],网易严选  ”yanxuan.notification.order.exceptional“ 返回值[{}]", e,
					exceptionInfo);
			return null;
		}
		return jsonResult;
	}

	/**
	 * 严选创建订单回调保存包裹信息
	 * 
	 * @param orderOut
	 */
	public void doOrderPackageStates(OrderOutVO orderOut) {
		if (orderOut != null) {

			// 获取当前的订单包裹信息
			List<OrderPackageVO> orderPackages = orderOut.getOrderPackages();

			/** 订单货品明细表 */
			OrderProductItem opi = new OrderProductItem();
			opi.setsOrderId(orderOut.getOrderId());
			List<OrderProductItem> orderProductItemList = orderProductItemService.getOrderProductItemList(opi);

			// 订单包裹物流信息
			ExpressPlatf eplatf = null;

			OrderExpressPlatf orderExpressPlatf = null; // 订单包裹物流 订单明细关联中间表
			for (OrderPackageVO orderPacks : orderPackages) {
				eplatf = new ExpressPlatf();
				eplatf.setsOrderId(orderOut.getOrderId()); // 订单ID
				eplatf.setPackageNo(orderPacks.getPackageId()); // 包裹Id
				eplatf.setExpressCompanyName(orderPacks.getExpressCompany()); // 物流公司
				eplatf.setExpressNo(orderPacks.getExpressNo()); // 物流单号
				eplatf.setEcomCode(Constants.GoodsEcomCodeType.ECOM01.getCode());

				// 待出库
				eplatf.setPackageStat(Constants.PackageStatus.PACK_STAT00.getCode());

				// 已发货
				if (YXConstUtils.PackageStatus.START_DELIVERY.getCode().equals(orderPacks.getPackageStatus())) {
					eplatf.setPackageStat(Constants.PackageStatus.PACK_STAT10.getCode());
				}

				eplatf = expressPlatfService.saveExpressPlatfForPackageNo(eplatf);// 保存订单包裹信息
																					// 并返回数据

				// 遍历包裹中所有的 sku信息
				for (OrderSkuVO orderSku : orderPacks.getOrderSkus()) {
					for (OrderProductItem orderProductItem : orderProductItemList) {

						// 如果订单中SKU信息一样
						if (orderSku.getSkuId().equals(orderProductItem.getSkuCode())) {
							orderExpressPlatf = new OrderExpressPlatf();
							orderExpressPlatf.setoItemId(orderProductItem.getoItemId());
							orderExpressPlatf.setSkuCode(orderSku.getSkuId());
							orderExpressPlatf.setSaleCount(String.valueOf(orderSku.getSaleCount()));
							orderExpressPlatf.setPackId(eplatf.getPackId());
							try {
								// 保存订单包裹的SKU信息
								orderExpressPlatfService.saveOrderExpressPlatf(orderExpressPlatf);
							} catch (Exception e) {
								logger.error("##用户下单:save orde Express platf the error[{}]", e);
							}
						}
					}
				}
			}
		}
	}
}
