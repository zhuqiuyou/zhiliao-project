package com.cn.thinkx.pms.service.trans.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.trans.TbTransInf;
import com.cn.thinkx.pms.pageModel.trans.TransInf;
import com.cn.thinkx.pms.service.trans.TransInfoService;

@Service
public class TransInfoServiceImpl implements TransInfoService {
	@Autowired
	private BaseDaoI<TbTransInf> transInfDao;

	@Override
	public List<TransInf> findTransInfos(TransInf transInf) {
		List<TransInf> list = new ArrayList<TransInf>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbTransInf t  ";
		List<TbTransInf> l = transInfDao.find(hql + whereHql(transInf, params), params);
		for (TbTransInf t : l) {
			TransInf sb = new TransInf();
			BeanUtils.copyProperties(t, sb);
			list.add(sb);
		}
		return list;
	}

	private String whereHql(TransInf transInf, Map<String, Object> params) {
		String hql = "";
		if (transInf != null) {
			hql += " where 1=1 ";
		}
		return hql;
	}
}
