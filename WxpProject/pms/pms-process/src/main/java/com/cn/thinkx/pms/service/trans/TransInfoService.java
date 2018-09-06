package com.cn.thinkx.pms.service.trans;

import java.util.List;

import com.cn.thinkx.pms.pageModel.trans.TransInf;

public interface TransInfoService {

	public List<TransInf> findTransInfos(TransInf transInf);
}
