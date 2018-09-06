package com.cn.thinkx.wecard.centre.module.task;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.common.redis.util.RedisDictProperties;
import com.cn.thinkx.common.wecard.domain.cardkeys.CardKeysOrderInf;
import com.cn.thinkx.common.wecard.domain.cardkeys.CardKeysTransLog;
import com.cn.thinkx.common.wecard.domain.scalper.Scalper;
import com.cn.thinkx.pms.base.domain.BaseResp;
import com.cn.thinkx.pms.base.http.HttpClientUtil;
import com.cn.thinkx.pms.base.utils.BankUtil;
import com.cn.thinkx.pms.base.utils.BaseConstants;
import com.cn.thinkx.pms.base.utils.BaseConstants.DataStatEnum;
import com.cn.thinkx.pms.base.utils.BaseConstants.TransType;
import com.cn.thinkx.pms.base.utils.BaseConstants.UserTypeEnum;
import com.cn.thinkx.pms.base.utils.DateUtil;
import com.cn.thinkx.pms.base.utils.SignUtil;
import com.cn.thinkx.pms.base.utils.StringUtil;
import com.cn.thinkx.wecard.centre.module.biz.service.CardKeysOrderInfService;
import com.cn.thinkx.wecard.centre.module.biz.service.CardKeysTransLogService;
import com.cn.thinkx.wecard.centre.module.biz.util.CardKeysFactory;
import com.cn.thinkx.wecard.centre.module.vo.BatchDataVO;
import com.cn.thinkx.wecard.centre.module.vo.DetailDataVO;
import com.cn.thinkx.wecard.centre.module.vo.WithdrawOrderVO;

/**
 * 卡券转让Task<br><br>
 * 
 * 功能 [定时任务job缓存线程池执行]<br>
 * 描述 [Task完成卡密交易流水更新（更改转入账户）、核销卡密以及调用代付接口]
 * 
 * @author pucker
 *
 */
public class NegotiationTask implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 卡密交易订单
	 */
	private CardKeysOrderInf cardKeysOrderInf;
	
	/**
	 * 知了企服代付接口请求地址
	 */
	private String BW_URL = RedisDictProperties.getInstance().getdictValueByCode(BaseConstants.WITHDRAW_REQUEST_URL);
	
	/**
	 * 知了企服请求代付加密KEY
	 * 
	 */
	private String WELFAREMART_WITHDRAW_KEY = RedisDictProperties.getInstance().getdictValueByCode(BaseConstants.WELFAREMART_WITHDRAW_KEY);

	/**
	 * 黄牛机器人1
	 */
	private static final Scalper SCALPER_1 = new Scalper("scalper1", "SCALPER-1", UserTypeEnum.HN_TYPE.getCode());
	/**
	 * 黄牛机器人2
	 */
	private static final Scalper SCALPER_2 = new Scalper("scalper2", "SCALPER-2", UserTypeEnum.HN_TYPE.getCode());
	/**
	 * 黄牛机器人3
	 */
	private static final Scalper SCALPER_3 = new Scalper("scalper3", "SCALPER-3", UserTypeEnum.HN_TYPE.getCode());

	/**
	 * 初始化卡券转让Task<br>
	 * 
	 * @param cko 卡券交易订单
	 * @param url 批量代付知了企服内部请求地址
	 * @see NegotiationTask
	 */
	public NegotiationTask(CardKeysOrderInf cko) {
		this.cardKeysOrderInf = cko;
	}
	
	private static final List<Scalper> SCALPER_LIST = Arrays.asList(SCALPER_1, SCALPER_2, SCALPER_3);
	private CardKeysOrderInfService cardKeysOrderInfService = CardKeysFactory.getCardKeysOrderInfService();

	@Override
	public void run() {
		try {
			int index = (int) (Math.random() * SCALPER_LIST.size());// 随机选择黄牛号
			logger.info("黄牛[{}]开始回收卡密交易订单{}", SCALPER_LIST.get(index).getUserName(), cardKeysOrderInf.toString());
			
			if (cardKeysOrderInf != null) {
				CardKeysTransLogService cardKeysTransLogService = CardKeysFactory.getCardKeysTransLogService();
				
				CardKeysTransLog vo = new CardKeysTransLog();
				vo.setOrderId(cardKeysOrderInf.getOrderId());
				vo.setTransId(TransType.W30.getCode());
				vo.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				List<CardKeysTransLog> list = cardKeysTransLogService.getCardKeysTransLogList(vo);// 根据订单号查找所有卡密交易流水
				
				if (list != null && list.size() > 0) {// 卡密交易流水不为空
					list.stream().forEach(item -> {
						item.setTfrInAcctNo(SCALPER_LIST.get(index).getUserId());// 设置转入账户(随机黄牛号)
						item.setTfrOutAcctNo(cardKeysOrderInf.getUserId());//设置转出账户（转让的用户userID）
						item.setTransResult(BaseConstants.RESPONSE_SUCCESS_CODE);// 设置卡密流水交易结果
						// 更新卡密交易流水失败时，出款扣除失败卡密流水出款金额
						if (cardKeysTransLogService.updateCardKeysTransLog(item) < 1) {
							int amount = Integer.parseInt(cardKeysOrderInf.getPaidAmount()) - Integer.parseInt(item.getTransAmt() + Integer.parseInt(item.getTransFee()));
							cardKeysOrderInf.setPaidAmount("" + amount);
							logger.error("## 定时任务Task--->回收待转让卡密失败，卡密交易订单号[{}] 卡密交易流水号[{}]", vo.getOrderId(), item.getTxnPrimaryKey());
						}
					});
					
					// 组装调用知了企服内部代付接口批次明细参数
					DetailDataVO detail = new DetailDataVO();
					detail.setSerialNo(cardKeysOrderInf.getOrderId());
					detail.setReceiverCardNo(cardKeysOrderInf.getBankNo());
					detail.setReceiverName(cardKeysOrderInf.getUserName());
					detail.setReceiverType("PERSON");
					detail.setBankName(BankUtil.getBankNameByCode(cardKeysOrderInf.getAccountBank()));
					detail.setBankCode(BankUtil.getYFBCodeByCode(cardKeysOrderInf.getAccountBank()));
					// 解析开户行省市
					if (cardKeysOrderInf.getAccountBankAddr().contains(",")) {
						String[] bankAddr = cardKeysOrderInf.getAccountBankAddr().split(",");
						detail.setBankProvince(bankAddr[0]);
						detail.setBankCity(bankAddr[1]);
					}
					detail.setAmount(String.valueOf(cardKeysOrderInf.getPaidAmount()));
					detail.setOrderName(cardKeysOrderInf.getUserId());
					List<DetailDataVO> detailData = Arrays.asList(detail);
					
					// 组装调用知了企服内部代付接口批次参数
					BatchDataVO batch = new BatchDataVO();// 批次
					batch.setTotalNum("1");
					batch.setTotalAmount(cardKeysOrderInf.getPaidAmount());
					batch.setPayDate(DateUtil.getCurrentDateStr());
					batch.setDetailData(detailData);
					batch.setBatchOrderName(new StringBuffer().append("卡密交易订单[").append(cardKeysOrderInf.getOrderId()).append("]批量代付").toString());
					
					//对参数进行加密
					WithdrawOrderVO withOrder = new WithdrawOrderVO();
					withOrder.setSerialNo(detail.getSerialNo());
					withOrder.setReceiverType(detail.getReceiverType());
					withOrder.setReceiverName(detail.getReceiverName());
					withOrder.setReceiverCardNo(detail.getReceiverCardNo());
					withOrder.setBankName(detail.getBankName());
					withOrder.setBankCode(detail.getBankCode());
					withOrder.setBankProvince(detail.getBankProvince());
					withOrder.setBankCity(detail.getBankCity());
					withOrder.setAmount(detail.getAmount());
					withOrder.setOrderName(detail.getOrderName());
					withOrder.setTotalNum(batch.getTotalNum());
					withOrder.setTotalAmount(batch.getTotalAmount());
					withOrder.setPayDate(batch.getPayDate());
					withOrder.setBatchOrderName(batch.getBatchOrderName());
					String sign = SignUtil.genSign(withOrder, WELFAREMART_WITHDRAW_KEY);
					batch.setSign(sign);
					List<BatchDataVO> batchData = Arrays.asList(batch);
					
					// 调用代付接口,根据代付接口返回信息更新卡密订单
					JSONObject paramData = new JSONObject();
					paramData.put("paramData", JSONArray.toJSONString(batchData));
					logger.info("代付传参=========>{}", paramData.toString());
					String rtnStr = HttpClientUtil.sendPost(BW_URL, paramData.toString());
					logger.info("代付返回=========>{}", rtnStr);
					
					// 代付成功后更新卡密交易订单：出款金额置0、订单状态为已转让或者转让失败
					cardKeysOrderInf.setPaidAmount(null);
					cardKeysOrderInf.setDataStat(DataStatEnum.FALSE_STATUS.getCode());// 代付后将卡密订单设置为不可继续代付
					if (StringUtil.isNullOrEmpty(rtnStr)) {// 代付返回为空
						cardKeysOrderInf.setStat(BaseConstants.orderStat.OS31.getCode());// 转让受理失败
					} else {
						BaseResp resp = JSONObject.parseObject(rtnStr, BaseResp.class);
						if (BaseConstants.RESPONSE_SUCCESS_CODE.equals(resp.getRespCode())) {
							cardKeysOrderInf.setStat(BaseConstants.orderStat.OS33.getCode());// 转让受理成功
						} else {
							cardKeysOrderInf.setStat(BaseConstants.orderStat.OS31.getCode());// 转让受理失败
						}
					}
					cardKeysOrderInfService.updateCardKeysOrderInf(cardKeysOrderInf);
				} else {
					logger.error("## 定时任务执行线程异常：订单号[{}]卡密交易流水为空", cardKeysOrderInf.getOrderId());
				}
			} else {
				logger.error("## 定时任务执行线程异常：cardKeysOrderInf对象为空");
			}
		} catch (Exception e) {
			cardKeysOrderInf.setStat(BaseConstants.orderStat.OS31.getCode());// 转让失败
			cardKeysOrderInfService.updateCardKeysOrderInf(cardKeysOrderInf);
			logger.error("## 定时任务执行线程异常", e);
		}
	}
	
}
