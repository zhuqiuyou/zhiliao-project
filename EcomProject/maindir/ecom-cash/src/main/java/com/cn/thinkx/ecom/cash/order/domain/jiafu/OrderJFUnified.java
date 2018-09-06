package com.cn.thinkx.ecom.cash.order.domain.jiafu;

public class OrderJFUnified {

	private String e_eid;
	private String e_uid;
	private String trade_no;
	private String total_amount;
	private String extand_params;
	private String timestamp;
	private String sign_type;
	private String sign;
	
	private String out_trade_no;
	private String code;
	private String errmsg;
	
	public String getE_eid() {
		return e_eid;
	}

	public void setE_eid(String e_eid) {
		this.e_eid = e_eid;
	}

	public String getE_uid() {
		return e_uid;
	}

	public void setE_uid(String e_uid) {
		this.e_uid = e_uid;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getExtand_params() {
		return extand_params;
	}

	public void setExtand_params(String extand_params) {
		this.extand_params = extand_params;
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
		return "OrderJFUnified [e_eid=" + e_eid + ", e_uid=" + e_uid + ", trade_no=" + trade_no + ", total_amount="
				+ total_amount + ", extand_params=" + extand_params + ", timestamp=" + timestamp + ", sign_type="
				+ sign_type + ", sign=" + sign + ", out_trade_no=" + out_trade_no + ", code=" + code + ", errmsg="
				+ errmsg + "]";
	}

}
