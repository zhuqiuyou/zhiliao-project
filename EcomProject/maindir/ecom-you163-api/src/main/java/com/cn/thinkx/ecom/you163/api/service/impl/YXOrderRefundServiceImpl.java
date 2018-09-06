package com.cn.thinkx.ecom.you163.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.ecom.activemq.core.service.WechatMQProducerService;
import com.cn.thinkx.ecom.activemq.core.util.WXTemplateUtil;
import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.service.MemberInfService;
import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.basics.order.domain.EcomRefundAddress;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.basics.order.service.EcomExpressConfirmService;
import com.cn.thinkx.ecom.basics.order.service.EcomRefundAddressService;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.basics.order.service.SellbackGoodslistService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOrderRefundService;
import com.cn.thinkx.ecom.you163.notify.domain.refund.ExpressConfirm;
import com.cn.thinkx.ecom.you163.notify.domain.refund.RefundAddress;
import com.cn.thinkx.ecom.you163.notify.domain.refund.RefundResult;
import com.cn.thinkx.ecom.you163.notify.domain.refund.RefundSku;
import com.cn.thinkx.ecom.you163.notify.domain.refund.RejectInfo;
import com.cn.thinkx.ecom.you163.notify.domain.refund.ReturnAddr;
import com.cn.thinkx.ecom.you163.notify.domain.refund.SystemCancel;

@Service("yxOrderRefundService")
public class YXOrderRefundServiceImpl implements YXOrderRefundService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReturnsOrderService returnsOrderService;

	@Autowired
	private OrderRefundService orderRefundService;

	@Autowired
	private OrderProductItemService orderProductItemService;

	@Autowired
	private SellbackGoodslistService sellbackGoodslistService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private EcomRefundAddressService ecomRefundAddressService;

	@Autowired
	private EcomExpressConfirmService ecomExpressConfirmService;

	@Autowired
	private WechatMQProducerService wechatMQProducerService;

	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Autowired
	private MemberInfService memberInfService;

	/**
	 * 退货地址回调
	 */
	@Override
	public JsonResult doRefundAddress(String refundAddress) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(refundAddress)) {
			return null;
		}
		try {
			RefundAddress refund = JSONArray.parseObject(refundAddress, RefundAddress.class);
			ReturnAddr returnAddr = refund.getReturnAddr();
			logger.info("## 退货地址回调,传入参数refundAddress：[{}]", JSON.toJSONString(refundAddress));

			/** 查询退货申请信息（通过申请单id和订单号） */
			ReturnsOrder ro = new ReturnsOrder();
			ro.setApplyId(refund.getApplyId());
			ro.setsOrderId(refund.getOrderId());
			ro = returnsOrderService.getReturnsOrderByApplyId(ro);
			if (!StringUtil.isNullOrEmpty(ro)) {
				if (!Constants.ReturnsStatus.RS2.getCode().equals(ro.getReturnsStatus())) {
					ro.setReturnsStatus(Constants.ReturnsStatus.RS2.getCode()); // 审核状态：已同意
					logger.info("## 退货申请信息ReturnsOrder：[{}]", JSON.toJSONString(ro));
					returnsOrderService.updateByPrimaryKey(ro); // 更改退货申请信息

					/** 修改退货商品表 */
					SellbackGoodslist sellGoods = new SellbackGoodslist();
					sellGoods.setsOrderId(refund.getOrderId());
					sellGoods.setReturnsId(ro.getReturnsId());
					List<SellbackGoodslist> sellGoosdList = sellbackGoodslistService.getSellbackGoodslistBy(sellGoods);
					sellGoods = sellGoosdList.get(0);
					sellGoods.setApplyReturnState(Constants.ApplyReturnState.ARS1.getCode()); // 退货状态标识:待发货
					sellbackGoodslistService.updateByPrimaryKey(sellGoods);

					EcomRefundAddress ecomRefAddre = new EcomRefundAddress();
					ecomRefAddre.setReturnsId(ro.getReturnsId());
					ecomRefAddre.setProvinceName(returnAddr.getProvinceName());
					ecomRefAddre.setCityName(returnAddr.getCityName());
					ecomRefAddre.setDistrictName(returnAddr.getDistrictName());
					ecomRefAddre.setAddress(returnAddr.getAddress());
					ecomRefAddre.setFullAddress(returnAddr.getFullAddress());
					ecomRefAddre.setZipCode(returnAddr.getZipCode());
					ecomRefAddre.setName(returnAddr.getName());
					ecomRefAddre.setMobile(returnAddr.getMobile());
					// 插入退货地址信息
					ecomRefundAddressService.saveEcomRefundAddress(ecomRefAddre);

					/** 给用户发送模板消息 start */
					if (!StringUtil.isNullOrEmpty(ro.getMemberId())) {
						MemberInf memberInf = memberInfService.selectByPrimaryKey(ro.getMemberId());
						logger.info("## 会员信息memberInf：[{}]", JSON.toJSONString(memberInf));
						if (!StringUtil.isNullOrEmpty(memberInf) && !StringUtil.isNullOrEmpty(memberInf.getOpenId())) {
							String acountName = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
									"WX_CUSTOMER_ACCOUNT");
							OrderProductItem item = null;
							if (!StringUtil.isNullOrEmpty(sellGoods)
									&& !StringUtil.isNullOrEmpty(sellGoods.getoItemId())) {
								item = orderProductItemService.selectByPrimaryKey(sellGoods.getoItemId());
							}
							TreeMap<String, TreeMap<String, String>> templateMsg = new TreeMap<String, TreeMap<String, String>>();
							templateMsg = WXTemplateUtil.setRefundAddressData(item.getProductName(),
									returnAddr.getName(), returnAddr.getMobile(), returnAddr.getFullAddress());

							logger.info("## 发送消息内容templateMsg：[{}]", JSON.toJSONString(templateMsg));
							wechatMQProducerService.sendTemplateMsg(acountName, memberInf.getOpenId(),
									"WX_TEMPLATE_ID_7", null, templateMsg);
						}
					}
					/** 给用户发送模板消息 end */
				}
			}
		} catch (Exception e) {
			logger.error("## 退货地址回调异常", e);
		}
		return jsonResult;
	}

	/**
	 * 严选拒绝退货回调
	 */
	@Override
	public JsonResult doRefundReject(String rejectInfo) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(rejectInfo)) {
			return null;
		}
		try {
			RejectInfo reject = JSONArray.parseObject(rejectInfo, RejectInfo.class);
			logger.info("## 严选拒绝退货回调,传入参数rejectInfo：[{}]", JSON.toJSONString(reject));

			/** 查询退货申请信息（通过申请单id和订单号） */
			ReturnsOrder returnsOrder = new ReturnsOrder();
			returnsOrder.setApplyId(reject.getApplyId());
			returnsOrder.setsOrderId(reject.getOrderId());
			returnsOrder = returnsOrderService.getReturnsOrderByApplyId(returnsOrder);

			if (!StringUtil.isNullOrEmpty(returnsOrder)) {
				if (!Constants.ReturnsStatus.RS1.getCode().equals(returnsOrder.getReturnsStatus())) {
					returnsOrder.setRefuseReason(reject.getRejectReason());
					returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS1.getCode()); // 审核状态-已拒绝
					returnsOrderService.updateByPrimaryKey(returnsOrder);

					/** 修改退货商品表 */
					SellbackGoodslist sellGoods = new SellbackGoodslist();
					sellGoods.setsOrderId(returnsOrder.getsOrderId());
					sellGoods.setReturnsId(returnsOrder.getReturnsId());
					List<SellbackGoodslist> sellGoosdList = sellbackGoodslistService.getSellbackGoodslistBy(sellGoods);
					sellGoods = sellGoosdList.get(0);
					sellGoods.setApplyReturnState(Constants.ApplyReturnState.ARS9.getCode()); // 退货状态标识:
																								// 拒接退货
					sellbackGoodslistService.updateByPrimaryKey(sellGoods);

					/** 给用户发送模板消息 start */
					if (!StringUtil.isNullOrEmpty(returnsOrder.getMemberId())) {
						MemberInf memberInf = memberInfService.selectByPrimaryKey(returnsOrder.getMemberId());
						logger.info("## 会员信息memberInf：[{}]", JSON.toJSONString(memberInf));
						if (!StringUtil.isNullOrEmpty(memberInf) && !StringUtil.isNullOrEmpty(memberInf.getOpenId())) {
							String acountName = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
									"WX_CUSTOMER_ACCOUNT");
							OrderProductItem item = null;
							if (!StringUtil.isNullOrEmpty(sellGoods)
									&& !StringUtil.isNullOrEmpty(sellGoods.getoItemId())) {
								item = orderProductItemService.selectByPrimaryKey(sellGoods.getoItemId());
							}
							TreeMap<String, TreeMap<String, String>> templateMsg = new TreeMap<String, TreeMap<String, String>>();
							templateMsg = WXTemplateUtil.setRefundAddressData(returnsOrder.getReturnsId(),
									returnsOrder.getsOrderId(), reject.getRejectReason(), item.getProductName());

							logger.info("## 发送消息内容templateMsg：[{}]", JSON.toJSONString(templateMsg));
							wechatMQProducerService.sendTemplateMsg(acountName, memberInf.getOpenId(),
									"WX_TEMPLATE_ID_8", null, templateMsg);
						}
					}
					/** 给用户发送模板消息 end */
				}
			}

		} catch (Exception e) {
			logger.error("## 严选拒绝退货回调异常", e);
			return null;
		}
		return jsonResult;
	}

	/**
	 * 退货包裹确认收货回调
	 */
	@Override
	public JsonResult doRefundExpressConfirm(String expressConfirm) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(expressConfirm)) {
			return null;
		}
		try {
			ExpressConfirm express = JSONArray.parseObject(expressConfirm, ExpressConfirm.class);
			logger.info("## 退货包裹确认收货回调,传入参数expressConfirm：[{}]", JSON.toJSONString(express));

			/** 查询退货申请信息（通过申请单id） */
			ReturnsOrder ro = new ReturnsOrder();
			ro.setApplyId(express.getApplyId());
			ro = returnsOrderService.getReturnsOrderByApplyId(ro);

			if (!StringUtil.isNullOrEmpty(ro)) {
				/** 修改退货商品表 */
				SellbackGoodslist sellGoods = new SellbackGoodslist();
				sellGoods.setsOrderId(ro.getsOrderId());
				sellGoods.setReturnsId(ro.getReturnsId());
				List<SellbackGoodslist> sellGoosdList = sellbackGoodslistService.getSellbackGoodslistBy(sellGoods);
				sellGoods = sellGoosdList.get(0);
				sellGoods.setApplyReturnState(Constants.ApplyReturnState.ARS3.getCode()); // 退货状态标识:
																							// 已收货
				sellbackGoodslistService.updateByPrimaryKey(sellGoods);

				EcomExpressConfirm ecomExp = ecomExpressConfirmService.selectByReturnsId(ro.getReturnsId());
				if (!StringUtil.isNullOrEmpty(ecomExp)) {
					if (!Constants.ConfirmReturnsStatus.RS20.getCode().equals(ecomExp.getReturnsState())) {
						ecomExp.setTrackingCompany(express.getTrackingCompany());
						ecomExp.setTrackingNum(express.getTrackingNum());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						ecomExp.setTrackingTime(sdf.format(Long.parseLong(String.valueOf(express.getTrackingTime()))));
						ecomExp.setReturnsState(Constants.ConfirmReturnsStatus.RS20.getCode()); // 退货包裹状态:已签收
						// 保存退货包裹确认收货信息
						ecomExpressConfirmService.updateByPrimaryKey(ecomExp);
					}
				}
			}
		} catch (Exception e) {
			logger.error("## 退货包裹确认收货回调异常", e);
			return null;
		}
		return jsonResult;
	}

	/**
	 * 严选系统取消退货回调
	 */
	@Override
	public JsonResult doRefundSystemCancel(String systemCancel) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(systemCancel)) {
			return null;
		}
		try {
			SystemCancel sys = JSONArray.parseObject(systemCancel, SystemCancel.class);
			logger.info("## 严选系统取消退货回调,传入参数systemCancel：[{}]", JSON.toJSONString(sys));

			/** 通过申请单id和订单号，查询退货申请信息 */
			ReturnsOrder returnsOrder = new ReturnsOrder();
			returnsOrder.setApplyId(sys.getApplyId());
			returnsOrder.setsOrderId(sys.getOrderId());
			returnsOrder = returnsOrderService.getReturnsOrderByApplyId(returnsOrder);

			if (!StringUtil.isNullOrEmpty(returnsOrder)) {
				if (!Constants.ReturnsStatus.RS1.getCode().equals(returnsOrder.getReturnsStatus())) {
					returnsOrder.setRefuseReason(sys.getErrorMsg());
					returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS1.getCode()); // 审核状态-已拒绝
					returnsOrderService.updateByPrimaryKey(returnsOrder);

					/** 修改退货商品表 */
					SellbackGoodslist sellGoods = new SellbackGoodslist();
					sellGoods.setsOrderId(returnsOrder.getsOrderId());
					sellGoods.setReturnsId(returnsOrder.getReturnsId());
					List<SellbackGoodslist> sellGoosdList = sellbackGoodslistService.getSellbackGoodslistBy(sellGoods);
					sellGoods = sellGoosdList.get(0);
					sellGoods.setApplyReturnState(Constants.ApplyReturnState.ARS6.getCode()); // 退货状态标识:
																								// 取消退货（(渠道取消）
					sellbackGoodslistService.updateByPrimaryKey(sellGoods);
				}
			}
		} catch (Exception e) {
			logger.error("## 严选系统取消退货回调异常", e);
			return null;
		}
		return jsonResult;
	}

	/**
	 * 退款结果回调
	 */
	@Override
	public JsonResult doRefundResultDirectly(String refundResult) {
		logger.info("## 退款结果回调结果refundResult:[{}]", refundResult);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		jsonResult.setMessage("");
		if (StringUtil.isNullOrEmpty(refundResult)) {
			return null;
		}
		try {
			RefundResult result = JSONArray.parseObject(refundResult, RefundResult.class);

			/** 通过申请单id和订单号，查询退货申请信息 */
			ReturnsOrder returnsOrder = new ReturnsOrder();
			returnsOrder.setApplyId(result.getApplyId());
			returnsOrder.setsOrderId(result.getOrderId());
			returnsOrder = returnsOrderService.getReturnsOrderByApplyId(returnsOrder);
			logger.info("## 退货申请信息信息returnsOrder:[{}]", JSON.toJSONString(returnsOrder));

			if (!Constants.ReturnsStatus.RS9.getCode().equals(returnsOrder.getReturnsStatus())) {
				List<RefundSku> refundSkuList = result.getRefundSkuList();
				int refundAmt = 0;
				for (RefundSku refundSku : refundSkuList) {
					refundAmt += Integer
							.parseInt(NumberUtils.RMBYuanToCent(String.valueOf(refundSku.getSubtotalPrice())))
							* refundSku.getCount();

					/** 申请退货时：查询退货商品表中的信息 */
					SellbackGoodslist sellGoods = new SellbackGoodslist();
					sellGoods.setsOrderId(result.getOrderId());
					sellGoods.setReturnsId(returnsOrder.getReturnsId());
					List<SellbackGoodslist> sellGoosdList = sellbackGoodslistService.getSellbackGoodslistBy(sellGoods);
					sellGoods = sellGoosdList.get(0);

					/** 申请退货时：更新退货商品表中的退货状态 */
					// sellGoods.setApplyReturnState(Constants.ApplyReturnState.ARS3.getCode());
					// // 需要用枚举
					sellGoods.setStorageNum(String.valueOf(refundSku.getCount())); // 入库数量
					sellbackGoodslistService.updateByPrimaryKey(sellGoods);
				}

				OrderRefund or = orderRefundService.getOrderRefundByReturnsId(returnsOrder.getReturnsId());
				if (StringUtil.isNullOrEmpty(or)) {
					/** 退款表 */
					OrderRefund orderRefund = new OrderRefund();
					orderRefund.setReturnsId(returnsOrder.getReturnsId());
					orderRefund.setsOrderId(result.getOrderId());
					orderRefund.setRefundAmt(String.valueOf(refundAmt)); // 退款金额
					orderRefund.setRefundStatus(Constants.RefundStatus.RFS0.getCode());
					orderRefund.setMemberId(returnsOrder.getMemberId());
					orderRefundService.insert(orderRefund);
				}
				logger.info("## 退款表OrderRefund:[{}]", JSON.toJSONString(or));
				if (!StringUtil.isNullOrEmpty(or.getRefundStatus())
						|| !Constants.RefundStatus.RFS1.getCode().equals(or.getRefundStatus())) {
					/** 调用退款 */
					PlatfShopOrder platfShopOrder = platfShopOrderService.selectByPrimaryKey(result.getOrderId());
					if (!platfShopOrderService.doHkbStoreRefund(platfShopOrder)) { // 退款失败
						return null;
					}
				}
			}
		} catch (Exception e) {
			logger.error("## 退款结果回调异常", e);
			return null;
		}
		return jsonResult;
	}

}
