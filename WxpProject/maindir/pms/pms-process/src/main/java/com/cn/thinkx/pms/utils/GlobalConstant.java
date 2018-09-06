package com.cn.thinkx.pms.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GlobalConstant {

	public static final String SESSION_INFO = "sessionInfo";

	public static final Integer ENABLE = 0; // 启用
	public static final Integer DISABLE = 1; // 禁用
	
	
	public static final Integer DEFAULT = 0; // 默认
	public static final Integer NOT_DEFAULT = 1; // 非默认
	
	public static final String KEY_TYPE_POS = "02"; // 终端密钥版本
	
	public static final Map<String,String> sexlist = new HashMap(){{ put("0", "男");  put("1", "女");} };
	public static final Map<String,String> statelist = new HashMap(){{ put("0", "启用");  put("1", "停用");} };
	public static final Map<String,String> allowlist = new HashMap(){{ put("0", "不允许");  put("1", "允许");} };
	public static final Map<String,String> termSigninStatlist = new HashMap(){{ put("0", "未签到");  put("1", "已签到");} };
	public static final Map<String,String> recordDatelist = new LinkedHashMap(){{ put("0", "当天时间");  put("1", "历史记录");} };
	public enum START_STOP_STATUS{
		START("0"),STOP("1");
		private String value;
		private START_STOP_STATUS(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
	};
	
	public enum PUBLIC_ORDER_STATUS{
		CANCLE("00"),DRAFT("01"),FAILURE("02");
		private String value;
		private PUBLIC_ORDER_STATUS(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
	};
	public static final Map<String,String> keyTypelist = new HashMap(){{ put("01", "账户密钥版本");  put("02", "终端密钥版本");} };
	public static final Map<String,String> commonDefaultlist = new HashMap(){{ put("0", "默认");  put("1", "非默认");} };
	public static final Map<String,String> onymousStatList = new HashMap(){{ put("00", "非记名卡");  put("10", "记名卡");} };	
	public static final Map<String,String> productTypeList = new HashMap(){{ put("00", "充值卡");  put("10", "礼品卡");} };
	public static final Map<String,String> businessTypeList = new HashMap(){{ put("10", "线下支付业务"); /* put("20", "线上支付业务"); put("30", "app卡支付业务");*/} };
	
	public static final Map<String,String> cardStatList = new HashMap(){{ put("00", "未激活");  put("10", "激活");put("20", "赎回");} };
	public static final Map<String,String> pos_effectlist = new HashMap(){{ put("0", "有效");  put("1", "失效");} };
	public static final Map<String,String> pos_testList = new HashMap(){{ put("0", "不验证");  put("1", "验证");} };
	public static final Map<String,String> pos_IC_download_flag = new HashMap(){{ put("0", "不下载");  put("1", "下载");} };
	public static final Map<String,String> card_pin_status = new LinkedHashMap<String, String>(){{ put("00", "验密");  put("10", "不验密");  put("20", "有限额验密");} };
	
	public static final Map<String,String> order_activate_sts = new HashMap(){{ put("0", "激活");  put("1", "不激活");} };
	public static final Map<String,String> pos_TMK_download_flag = new HashMap(){{ put("1", "不允许下载");  put("0", "允许下载");} };
	public static String convertJson(Map<String,String> cardMap){
		JSONArray arr = new JSONArray();
		for (String k : cardMap.keySet()) {
			JSONObject json = new JSONObject();
			json.put("key", k);
			json.put("value", cardMap.get(k));
			arr.add(json);
		}
		return arr.toJSONString();
	}
	
	
	
	public static final String VTXN = "vTxn";
	
	//通信成功后返回值
	public static final String RESPONSE_SUCCESS_CODE= "00";
	
	//交易系统服务名称
		private final static String[] SVR_TXN = {"vChgKeyServiceTxn","vRechargeOrder",
												"vTxn","vCardTxnInq","vMchntTxnInq","vReloadShmProc","txnCon"};
		
		//账户系统服务名称
		private final static String[] SVR_ACC = {"vCardBatInq","vCardPinCheck",
												"vChgKeyServiceAcc","vMakecard","vCardQry","vRecharge","vRechrgOrder","vAct","Switch","vCardHolderBatInq","accCon"};
		
		//结算系统服务名称
		private final static String[] SVR_STL = {"vBillProc","stlCon"};
		
		/**
		 * 根据服务名称查找目标系统，如果未找到，则返回NULL
		 * @param svrNm 服务名称
		 * @return
		 */
		public static String  getSysBySvr(String svrNm){
			for(int i=0; i<SVR_TXN.length; i++)
				if(SVR_TXN[i].equals(svrNm))
					return SYSTEM_NAME.TXN.name();
			
			for(int i=0; i<SVR_ACC.length; i++)
				if(SVR_ACC[i].equals(svrNm))
					return SYSTEM_NAME.ACC.name();
			
			for(int i=0; i<SVR_STL.length; i++)
				if(SVR_STL[i].equals(svrNm))
					return SYSTEM_NAME.STL.name();
			
			return null;
		}
	/**
	 * CONNECTION_NM:链路名称
	 * REMOTE_IP:远端IP
	 * REMOTE_PORT:远端端口
	 */
	public final static String CONNECTION_NM = "CONNECTION_NM";
	public final static String REMOTE_IP = "REMOTE_IP";
	public final static String REMOTE_PORT = "REMOTE_PORT";
	public final static String LINK_UUID = "LINK_UUID";
	
	
	/**
	 * 搭建链路的最长允许时间（微秒）
	 */
	public final static long CONNECTION_CREATING_MAX_MILLIS = 9000;
	/**
	 * 链路状态
	 * 0:已关闭
	 * 1:建立中
	 * 2:已链接
	 */
	public enum CONNECTION_STATUS{
		CLOSED("0"),CREATING("1"),ESTABLISHED("2");
		private final  String value;
		CONNECTION_STATUS(String value){
			this.value = value;
		};
		@Override
		public String toString() {
			return this.value;
		}
	};
	/**
	 * 链路名称
	 */
	public enum CONNECTION_NAME{
		CONNECTION_TXN_1,CONNECTION_TXN_2
	};
	/**
	 * 系統名稱
	 * @author sunyue
	 *
	 */
	public enum SYSTEM_NAME{
		/*交易系統*/
		TXN,
		/*賬戶系統*/
		ACC,
		/*結算系統*/
		STL
	};
}
