package com.cn.thinkx.ecom.cash.wxtrans.domain;

/**
 * 交易返回对象，用于接收交易返回时转换json字符串
 * 
 * @author xiaomei
 *
 */
public class TxnResp {

	private String code;
	private String info;
	private String txnFlowNo;
	private String swtFlowNo;
	private String transAmt; //手续费

	/**返回接口层流水主键**/
	private String interfacePrimaryKey;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTxnFlowNo() {
		return txnFlowNo;
	}

	public void setTxnFlowNo(String txnFlowNo) {
		this.txnFlowNo = txnFlowNo;
	}

	public String getSwtFlowNo() {
		return swtFlowNo;
	}

	public void setSwtFlowNo(String swtFlowNo) {
		this.swtFlowNo = swtFlowNo;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getInterfacePrimaryKey() {
		return interfacePrimaryKey;
	}

	public void setInterfacePrimaryKey(String interfacePrimaryKey) {
		this.interfacePrimaryKey = interfacePrimaryKey;
	}

	@Override
	public String toString() {
		return "TxnResp [code=" + code + ", info=" + info + ", txnFlowNo=" + txnFlowNo + ", swtFlowNo=" + swtFlowNo
				+ ", transAmt=" + transAmt + ", interfacePrimaryKey=" + interfacePrimaryKey + "]";
	}
	
}
