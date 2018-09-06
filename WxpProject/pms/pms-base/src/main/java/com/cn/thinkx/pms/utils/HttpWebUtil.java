package com.cn.thinkx.pms.utils;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http工具类
 */
public class HttpWebUtil{
	
	private static Logger logger = LoggerFactory.getLogger(HttpWebUtil.class);
	
	/**
	 * 获取商户服务系统的路径
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public static String getMerchantDomainUrl(){
		return ReadPropertiesFile.getInstance().getProperty("MERCHANT_DOMAIN_URL", null);
	}
	
	/**
	 * 获取客户系统的域名
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public static String getCustomerDomainUrl(){
		return ReadPropertiesFile.getInstance().getProperty("CUSTOMER_DOMAIN_URL", null);
	}
	
	/**
	 * websocket 连接域名解析
	 * @return
	 */
	public static String getMerchantWsUrl(){
		String wsurl=getMerchantDomainUrl();
		if(wsurl.contains("https://")){
			return org.apache.commons.lang.StringUtils.substringAfterLast(wsurl,"https://");
		}else{
			return org.apache.commons.lang.StringUtils.substringAfterLast(wsurl,"http://");
		}
	}
	
	/**
	 * 微信扫码支付路径
	 * @param mchntCode
	 * @param shopCode
	 * @return
	 */
	public static String getCustomerScanPayUrl(String mchntCode,String shopCode){
		String httpDomain = HttpWebUtil.getCustomerDomainUrl();  //获取客户端域名
		StringBuffer payUrl = new StringBuffer();

		payUrl.append(httpDomain).append("om.html?p=");
		String encryptStr =mchntCode + "|" + shopCode;
		try {
			payUrl.append(URLEncoder.encode(DES3Util.Encrypt3DES(encryptStr, BaseKeyUtil.getEncodingAesKey()), "UTF-8"));
		} catch (Exception e) {
			logger.error("商户收银台-->收款码二维码加密失败", e);
		}
		
		return payUrl.toString();
	}
}
