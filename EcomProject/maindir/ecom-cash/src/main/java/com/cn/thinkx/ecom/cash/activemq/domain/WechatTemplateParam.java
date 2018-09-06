package com.cn.thinkx.ecom.cash.activemq.domain;

import java.util.TreeMap;

/**
 * 微信模板消息参数封装类
 */
public class WechatTemplateParam {

	private String acountName;// 微信号

	private String touser;// 接收者openid

	private String template_id;// 模板id

	private String url;// 模板跳转链接

	// "miniprogram":{ 未加入
	// "appid":"xiaochengxuappid12345",
	// "pagepath":"index?foo=bar"
	// },

	private TreeMap<String, TreeMap<String, String>> data;// 模板数据

	public String getAcountName() {
		return acountName;
	}

	public void setAcountName(String acountName) {
		this.acountName = acountName;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TreeMap<String, TreeMap<String, String>> getData() {
		return data;
	}

	public void setData(TreeMap<String, TreeMap<String, String>> data) {
		this.data = data;
	}

}
