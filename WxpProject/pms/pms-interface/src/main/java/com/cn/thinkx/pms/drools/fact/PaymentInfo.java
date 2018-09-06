package com.cn.thinkx.pms.drools.fact;

public class PaymentInfo extends BaseFact {

	private static final long serialVersionUID = 1L;

	private int inMoney;

	private int outMoney;

	public PaymentInfo() {

	}

	public int getInMoney() {
		return inMoney;
	}

	public void setInMoney(int inMoney) {
		this.inMoney = inMoney;
	}

	public int getOutMoney() {
		return outMoney;
	}

	public void setOutMoney(int outMoney) {
		this.outMoney = outMoney;
	}

}
