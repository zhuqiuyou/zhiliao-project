package com.cn.thinkx.ecom.cash.redis.utils;

import org.springframework.web.context.WebApplicationContext;

/**
 *  加密key 统一管理
 * @author zqy
 *
 */
public class BaseKeyUtil {
	
	/**
	 * DES 加密字符串
	 * @return
	 */
	public static String getEncodingAesKey(WebApplicationContext context){
		return RedisDictProperties.getInstance(context).getdictValueByCode("ENCODING_AES_KEY",context);
	}
}
