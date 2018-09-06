package com.cn.thinkx.pms.xfire;

import com.cn.thinkx.pms.entity.RequestMessage;
import com.cn.thinkx.pms.entity.ResponseMessage;

public interface PMSWebService {

	public ResponseMessage search(RequestMessage requestMessage);

}