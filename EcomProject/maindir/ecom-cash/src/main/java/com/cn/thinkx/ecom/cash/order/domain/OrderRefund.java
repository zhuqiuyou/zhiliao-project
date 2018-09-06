package com.cn.thinkx.ecom.cash.order.domain;

/**
 * 退款参数
 * 
 * @author xiaomei
 *
 */
public class OrderRefund {
	
	private String oriOrderId;		//外部原交易订单号
	private String refundOrderId;	//外部退款订单号（唯一）
	private String refundAmount;	//退款金额
	private String channel;			//渠道号	
	private String refundFlag;		//退款标志（1：代表系统自动发起退款；2：代表用户端发起退款）
	private long timestamp;			//时间戳
	private String sign;			//签名
	
	private String code;		//返回码（00：代表退款成功）
	private String msg;			//返回信息
	private String txnFlowNo;	//知了企服退款流水
	
	private String channelName;		//渠道名称
	
	public String getOriOrderId() {
		return oriOrderId;
	}
	public void setOriOrderId(String oriOrderId) {
		this.oriOrderId = oriOrderId;
	}
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	public String getTxnFlowNo() {
		return txnFlowNo;
	}
	public void setTxnFlowNo(String txnFlowNo) {
		this.txnFlowNo = txnFlowNo;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRefundFlag() {
		return refundFlag;
	}
	public void setRefundFlag(String refundFlag) {
		this.refundFlag = refundFlag;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	@Override
	public String toString() {
		return "OrderRefund [oriOrderId=" + oriOrderId + ", refundOrderId=" + refundOrderId + ", refundAmount="
				+ refundAmount + ", channel=" + channel + ", refundFlag=" + refundFlag + ", timestamp=" + timestamp
				+ ", sign=" + sign + ", code=" + code + ", msg=" + msg + ", txnFlowNo=" + txnFlowNo + ", channelName="
				+ channelName + "]";
	}
}
