package com.cn.thinkx.pms.utils;


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
	public static String getEncodingAesKey(){
		return ReadPropertiesFile.getInstance().getProperty("ENCODING_AES_KEY", null);
	}
}
