package com.cn.thinkx.ecom.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cn.thinkx.ecom.cash.order.domain.OrderUnified;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;

public class CashTest {

	// 海易通调用电商加密key
	@Value("${HYT_SIGN_KEY}")
	private String HYT_SIGN_KEY;
	// 电商调用知了企服收银台加密key
	@Value("${HKB_SIGN_KEY}")
	private String HKB_SIGN_KEY;
	// 家乐宝调用电商加密key
	@Value("${JLB_SIGN_KEY}")
	private String JLB_SIGN_KEY;

	// 电商通知返回地址
	@Value("${ECOM_NOTIFY_URL}")
	private String ECOM_NOTIFY_URL;
	// 电商重定向地址
	@Value("${ECOM_REDIRECT_URL}")
	private String ECOM_REDIRECT_URL;
	// 调用知了企服收银台地址
	@Value("${HKB_CASH_URL}")
	private String HKB_CASH_URL;

	/**
	 * 模拟测试方法 （调用知了企服收银台）
	 * 
	 * @param resp
	 */
	@RequestMapping(value = "/a")
	public void a(HttpServletResponse resp) {
		try {
			OrderUnified or = new OrderUnified();
			// String channel="40006002";
			// String orderId="1509072171906";
			// String userId="2017090711093600000843";
			// String commodityName="家乐宝订单";
			// String commodityNum="3";
			// String txnAmount="650";
			// String innerMerchantNo="101000000211715";
			// String innerShopNo="00000045";
			// String notify_url="https://cs.jialebao.me/huiCardBaoPayNotify";
			// String redirect_type="1";
			// String
			// redirect_url="https://cs.jialebao.me/weichat/activity/ac150915/empty_jump.html?reward_type=f_g_t_d";
			// String attach="23076_23068_1534844";
			// String sign="CF2F63B3E66987D0D3E12ABAB1AA71DB";
			or.setAttach("23076_23068_1534844");
			or.setChannel("40006002");
			or.setCommodityName("家乐宝订单");
			or.setCommodityNum("3");
			or.setInnerMerchantNo("101000000211715");
			or.setInnerShopNo("00000045");
			or.setNotify_url("https://cs.jialebao.me/huiCardBaoPayNotify");
			or.setOrderId("1509072171906");
			or.setRedirect_type("1");
			or.setRedirect_url("https://cs.jialebao.me/weichat/activity/ac150915/empty_jump.html?reward_type=f_g_t_d");
			or.setSign("CF2F63B3E66987D0D3E12ABAB1AA71DB");
			or.setTxnAmount("650");
			or.setUserId("2017090711093600000843");
			String checkSign = "";
			switch (or.getChannel()) {
			case "40006002":
				checkSign = SignUtil.genSign(or, JLB_SIGN_KEY);
				break;
			case "40006001":
				checkSign = SignUtil.genSign(or, HYT_SIGN_KEY);
				break;
			default:
				break;
			}
			if (checkSign.equals(or.getSign())) {
				// 把参数按照收银台的要求添加至Map中
				SortedMap<String, String> itemsMap = new TreeMap<String, String>();
				if (!StringUtil.isNullOrEmpty(or.getAttach())) {
					itemsMap.put("attach", or.getAttach());
				}
				itemsMap.put("channel", "40006001");
				itemsMap.put("commodityName", or.getCommodityName());
				itemsMap.put("commodityNum", or.getCommodityNum());
				itemsMap.put("innerMerchantNo", or.getInnerMerchantNo());
				itemsMap.put("innerShopNo", or.getInnerShopNo());
				itemsMap.put("notify_url", ECOM_NOTIFY_URL);
				itemsMap.put("orderId", "");// 订单表中新增或修改数据库生成的主键
				if (!StringUtil.isNullOrEmpty(or.getRedirect_type()))
					itemsMap.put("redirect_type", or.getRedirect_type());
				if (!StringUtil.isNullOrEmpty(or.getRedirect_url()))
					itemsMap.put("redirect_url", ECOM_REDIRECT_URL);
				itemsMap.put("txnAmount", or.getTxnAmount());
				itemsMap.put("userId", or.getUserId());
				// SortedMap<String, String> itemsMap = CRorderInf(or);
				// orderSkip(resp, or, itemsMap);
				// 根据知了企服的要求对参数进行加密
				StringBuilder forSign = new StringBuilder();
				for (String key : itemsMap.keySet()) {
					forSign.append(key).append("=").append(itemsMap.get(key)).append("&");
				}
				forSign.append("key=").append(HKB_SIGN_KEY);
				String signs = SignUtil.MD5Encode(forSign.toString());
				// 跳转知了企服收银台
				resp.setContentType("text/html;charset=utf-8");
				PrintWriter out = resp.getWriter();
				out.println("<form name='cashSubmit' method='post'  action='" + HKB_CASH_URL + "' >");
				if (!StringUtil.isNullOrEmpty(or.getAttach()))
					out.println("<input type='hidden' name='attach' value='" + or.getAttach() + "'>");
				if (!StringUtil.isNullOrEmpty(or.getRedirect_type()))
					out.println("<input type='hidden' name='redirect_type' value='" + or.getRedirect_type() + "'>");
				if (!StringUtil.isNullOrEmpty(or.getRedirect_url()))
					out.println("<input type='hidden' name='redirect_url' value='" + ECOM_REDIRECT_URL + "'>");
				out.println("<input type='hidden' name='channel' value='" + 40006001 + "'>");
				out.println("<input type='hidden' name='commodityName' value='" + or.getCommodityName() + "'>");
				out.println("<input type='hidden' name='commodityNum' value='" + or.getCommodityNum() + "'>");
				out.println("<input type='hidden' name='innerMerchantNo' value='" + or.getInnerMerchantNo() + "'>");
				out.println("<input type='hidden' name='innerShopNo' value='" + or.getInnerShopNo() + "'>");
				out.println("<input type='hidden' name='notify_url' value='" + ECOM_NOTIFY_URL + "'>");
				out.println("<input type='hidden' name='orderId' value='" + itemsMap.get("orderId") + "'>");
				out.println("<input type='hidden' name='sign' value='" + signs + "'>");
				out.println("<input type='hidden' name='txnAmount' value='" + or.getTxnAmount() + "'>");
				out.println("<input type='hidden' name='userId' value='" + or.getUserId() + "'>");
				out.println("</form>");
				out.println("<script type='text/javascript'>");
				out.println("document.cashSubmit.submit()");
				out.println("</script>");
			} else {
				System.out.println("知了企服电商平台调用知了企服收银台失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
