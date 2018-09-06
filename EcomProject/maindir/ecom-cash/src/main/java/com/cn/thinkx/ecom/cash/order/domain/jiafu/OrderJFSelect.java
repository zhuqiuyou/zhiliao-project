package com.cn.thinkx.ecom.cash.order.domain.jiafu;

public class OrderJFSelect {

	private String trade_no;	//平台交易号
	private String timestamp;	//时间戳
	private String sign_type;	//时间戳
	private String sign;		//时间戳
	
	private String out_trade_no;	//时间戳
	private String refund_flag;		//时间戳
	private String total_amount;	//时间戳
	private String refund_amount;	//时间戳
	private String code;			//时间戳
	private String errmsg;			//时间戳
	
	public String getTrade_no() {
		return trade_no;
	}
	
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getSign_type() {
		return sign_type;
	}
	
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	public String getSign() {
		return sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getOut_trade_no() {
		return out_trade_no;
	}
	
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
	public String getRefund_flag() {
		return refund_flag;
	}
	
	public void setRefund_flag(String refund_flag) {
		this.refund_flag = refund_flag;
	}
	
	public String getTotal_amount() {
		return total_amount;
	}
	
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	
	public String getRefund_amount() {
		return refund_amount;
	}
	
	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getErrmsg() {
		return errmsg;
	}
	
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String toString() {
		return "OrderJFSelect [trade_no=" + trade_no + ", timestamp=" + timestamp + ", sign_type=" + sign_type
				+ ", sign=" + sign + ", out_trade_no=" + out_trade_no + ", refund_flag=" + refund_flag
				+ ", total_amount=" + total_amount + ", refund_amount=" + refund_amount + ", code=" + code + ", errmsg="
				+ errmsg + "]";
	}
	
}
