package com.cn.thinkx.wecard.customer.module.wxcms;

import com.cn.thinkx.common.redis.util.RedisDictProperties;

public class WxCmsContents {

	/**手机动态码 session**/
	public static final String SESSION_PHONECODE = "session_phonecode_";
	public static final String SESSION_PHONECODE_TIME = "session_phonecode_time_";
	/** * 短信成功状态*/
	public static final String SUCCESS_SMS_STATUS = "0";
	
	/***商户注册页面跳转**/
	public static final String WX_MERCHANT_REG_NAME="商户注册"; 
	
	public static String getCurrMpAccount(){
//		return RedisDictProperties.getInstance().getdictValueByCode("WX_CUSTOMER_ACCOUNT");
		return RedisDictProperties.getInstance().getdictValueByCode("WX_HUIKABAO_LIFE_ACCOUNT");
	}
}
