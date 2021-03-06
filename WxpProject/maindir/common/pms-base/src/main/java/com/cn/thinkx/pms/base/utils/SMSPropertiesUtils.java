package com.cn.thinkx.pms.base.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;


/**
 * Proprties 短信发送工具类
 * PropertiesUtils
 */
public class SMSPropertiesUtils {

	private static Properties currProp;
	

	public final static String SYS_CONFIG_FILE = "smsConfig.properties";

	private  Logger logger = LoggerFactory.getLogger(SMSPropertiesUtils.class);

	private SMSPropertiesUtils(){
	}
 
	/**
	 * 功能：解析配置文件
	 * @param filePath
	 * @return Properties
	 */
	public static Properties getProperties(String filePath) {
		Properties prop = new Properties();
		try {
			ClassPathResource cr = new ClassPathResource(filePath);
			prop.load(cr.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}
	/**
	 * 功能：得到的配置文件
	 * @return Properties
	 */
	public static Properties getSysProperties(){
		if(currProp == null){
			currProp = getProperties(SYS_CONFIG_FILE);
		}
		return currProp;
	}
	public static String getProperty(String key){
		return getSysProperties().getProperty(key);
	}

	public static Properties getCurrProperties(){
	    return getProperties(SYS_CONFIG_FILE);
	}
   public static String getCurrProperty(String key){
        return getProperties(SYS_CONFIG_FILE).getProperty(key);
   }
}