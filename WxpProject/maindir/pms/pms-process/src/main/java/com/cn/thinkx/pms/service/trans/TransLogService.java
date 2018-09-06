package com.cn.thinkx.pms.service.trans;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.trans.TransLog;

;

public interface TransLogService {

	public List<TransLog> dataGrid(TransLog transLog, PageFilter ph);

	public Long count(TransLog transLog, PageFilter ph);
	
	public List<TransLog> queryTransLog(TransLog transLog, PageFilter ph);

}
