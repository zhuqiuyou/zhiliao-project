package com.cn.thinkx.pms.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface PMSWebService {
	@WebMethod
	public void discount(@WebParam(name = "money") int money);

	public void setUser(String username);
}