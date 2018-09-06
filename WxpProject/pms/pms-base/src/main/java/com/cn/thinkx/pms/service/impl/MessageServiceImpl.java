package com.cn.thinkx.pms.service.impl;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.pms.http.HttpClient;
import com.cn.thinkx.pms.http.HttpRequest;
import com.cn.thinkx.pms.http.HttpResponse;
import com.cn.thinkx.pms.service.MessageService;
import com.cn.thinkx.pms.utils.ReadPropertiesFile;
import com.cn.thinkx.pms.utils.StringUtil;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	private static Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);
	
	private final static String SEND_URL = ReadPropertiesFile.getInstance().getProperty("SEND_URL", null);
	private final static String ACCOUNT = ReadPropertiesFile.getInstance().getProperty("ACCOUNT", null);
	private final static String PASSWORD = ReadPropertiesFile.getInstance().getProperty("PASSWORD", null);

	@Override
	public boolean sendMessage(String target, String content) {
		String flag= ReadPropertiesFile.getInstance().getProperty("SEND_MSG_FLAG", null);
		if("true".equals(flag)){
			try {
				StringBuffer strBuf = new StringBuffer(SEND_URL);
				strBuf.append("?un=").append(ACCOUNT);
				strBuf.append("&pw=").append(PASSWORD);
				strBuf.append("&da=").append(target);
				strBuf.append("&sm=").append(URLEncoder.encode(content, "UTF-8"));
				strBuf.append("&dc=15&rd=1&rf=2&tf=3");
	
				LOG.info("手机号：" + target + "--->" + "短信开始发送");
				HttpResponse response = HttpClient.get(new HttpRequest(strBuf.toString().replace("+","%20")));
				String result = null;
				if (response != null && StringUtil.isNotEmpty(result = response.getStringResult())) {
					JSONObject obj = JSON.parseObject(result);
					if (obj != null && obj.getBooleanValue("success")) {
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("手机号：" + target + "--->" + "短信发送失败", e);
			}
		}
		return false;
	}

	/*public static void main(String[] args) {
		MessageService pro = new MessageServiceImpl();
		System.out.println(pro.sendMessage("13162666043", "【知了企服】亲爱的会员，你尾号为0265的会员卡于2016-10-09 13:09:17充值100.00元，余额234.72元[上海宸树]"));
	}*/
}
