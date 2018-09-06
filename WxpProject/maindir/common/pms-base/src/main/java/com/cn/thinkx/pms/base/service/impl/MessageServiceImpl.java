package com.cn.thinkx.pms.base.service.impl;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.pms.base.http.HttpClient;
import com.cn.thinkx.pms.base.http.HttpRequest;
import com.cn.thinkx.pms.base.http.HttpResponse;
import com.cn.thinkx.pms.base.service.MessageService;
import com.cn.thinkx.pms.base.utils.SMSPropertiesUtils;
import com.cn.thinkx.pms.base.utils.StringUtil;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
	private static Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Override
	public boolean sendMessage(String target, String content) {
		String flag = SMSPropertiesUtils.getProperty("SEND_MSG_FLAG");
		if ("true".equals(flag)) {
			try {
				StringBuffer strBuf = new StringBuffer(SMSPropertiesUtils.getProperty("SEND_URL"));
				strBuf.append("?un=").append(SMSPropertiesUtils.getProperty("ACCOUNT"));
				strBuf.append("&pw=").append(SMSPropertiesUtils.getProperty("PASSWORD"));
				strBuf.append("&da=").append(target);
				strBuf.append("&sm=").append(URLEncoder.encode(content, "UTF-8"));
				strBuf.append("&dc=15&rd=1&rf=2&tf=3");

				LOG.info("手机号[{}]开始发送短信", target);
				HttpResponse response = HttpClient.get(new HttpRequest(strBuf.toString().replace("+", "%20")));
				String result = null;
				if (response != null && StringUtil.isNotEmpty(result = response.getStringResult())) {
					JSONObject obj = JSON.parseObject(result);
					if (obj != null && obj.getBooleanValue("success")) {
						return true;
					}
				}
			} catch (Exception e) {
				LOG.error("## 手机号[{}]短信发送失败", target, e);
			}
		}
		return false;
	}

	/*
	 * public static void main(String[] args) { MessageService pro = new
	 * MessageServiceImpl(); System.out.println(pro.sendMessage("13162666043",
	 * "【知了企服】亲爱的会员，你尾号为0265的会员卡于2016-10-09 13:09:17充值100.00元，余额234.72元[上海宸树]"));
	 * }
	 */
}
