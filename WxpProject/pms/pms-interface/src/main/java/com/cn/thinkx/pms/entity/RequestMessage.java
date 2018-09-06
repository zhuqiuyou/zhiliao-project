package com.cn.thinkx.pms.entity;

public class RequestMessage {

	protected RequestHeard requestHeard;
	protected MessageBody requestBody;

	public RequestHeard getRequestHeard() {
		return requestHeard;
	}

	public void setRequestHeard(RequestHeard requestHeard) {
		this.requestHeard = requestHeard;
	}

	public MessageBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(MessageBody requestBody) {
		this.requestBody = requestBody;
	}

}
