
package com.cn.thinkx.pms.entity;

public class RequestHeard {

    protected String seq;
    protected String requestDate;
    protected String status;
    
/*    public RequestHeard(String seq,String requestDate,String status) {
    	this.seq=seq;
    	this.requestDate=seq;
    	this.status=status;
	}*/
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

    
}
