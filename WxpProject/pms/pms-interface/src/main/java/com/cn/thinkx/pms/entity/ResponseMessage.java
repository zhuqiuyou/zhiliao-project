package com.cn.thinkx.pms.entity;

public class ResponseMessage {

	protected ResponseHeard responseHeard;
	protected MessageBody responseBody;

	public ResponseHeard getResponseHeard() {
		return responseHeard;
	}

	public void setResponseHeard(ResponseHeard responseHeard) {
		this.responseHeard = responseHeard;
	}

	public MessageBody getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(MessageBody responseBody) {
		this.responseBody = responseBody;
	}
}
