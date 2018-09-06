package com.cn.thinkx.ecom.you163.api.utils;

/**
 * 严选字典数据
 * @author zhuqiuyou
 *
 */
public class YXConstUtils {

	
	/**
	 * 订单状态
	 * 
	 * @author zhuqiuyou
	 *
	 */
	public enum OrderStatus {

		PAYED("PAYED", "已付款"),
		KF_CANCEL("KF_CANCEL", "客服取消"),
		CANCELLING("CANCELLING", "取消待审核");

		private String code;
		private String name;

		OrderStatus(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}
		
	}
	
	
	/**
	 * 包裹状态
	 * 
	 * @author zhuqiuyou
	 *
	 */
	public enum PackageStatus {

		WAITING_DELIVERY("PAYED", "等待发货"),
		START_DELIVERY("KF_CANCEL", "已发货(等待收货)"),
		WAITING_COMMENT("CANCELLING", "已收货(等待评论)");

		private String code;
		private String name;

		PackageStatus(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}
		
	}
}
