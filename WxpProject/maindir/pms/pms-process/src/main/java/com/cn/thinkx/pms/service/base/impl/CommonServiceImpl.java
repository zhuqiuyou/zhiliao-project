package com.cn.thinkx.pms.service.base.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.base.TbSeqMngInf;
import com.cn.thinkx.pms.model.contract.TbMerchantContract;
import com.cn.thinkx.pms.model.sys.TsystemParameter;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	private BaseDaoI<TbSeqMngInf> seqMngInfDao;

	@Autowired
	private BaseDaoI<TsystemParameter> systemParameterDao;

	@Autowired
	private BaseDaoI<TbMerchantContract> contractDao;

	private int seqCoun = 100;
	private Map<String, LinkedList<String>> currentMap = new HashMap<String, LinkedList<String>>();

	public <T> String initSeqId(Class<T> object) {
		Table table = object.getAnnotation(Table.class);
		String tableName = table.name();
		LinkedList<String> seqList = currentMap.get(tableName);
		if (seqList == null || seqList.size() == 0) {
			String hql = "from TbSeqMngInf f where  f.tableName='" + tableName + "'";
			setSeqMap(hql);
			seqList = currentMap.get(tableName);
		}
		String dateStr = DateUtil.getStringFromDate(new Date(), "yyyyMMddHHmmss");
		return dateStr + seqList.removeLast();
	}

	private void setSeqMap(String hql) {
		List<TbSeqMngInf> currentSeqList = seqMngInfDao.find(hql);
		for (TbSeqMngInf seqMngInf : currentSeqList) {
			LinkedList<String> seqList = new LinkedList<String>();
			Long seq = Long.parseLong(seqMngInf.getCurValue());
			Long maxSeq = Long.parseLong(seqMngInf.getMaxValue());
			for (int i = 0; i < seqCoun; i++) {
				Long s = seq + i;
				if (s > maxSeq) {
					seq = Long.parseLong(seqMngInf.getMinValue());
					s = seq;
				}
				String cach = StringUtil.leftPad(String.valueOf(s), 8, "0");
				seqList.addFirst(cach);
			}
			seqMngInf.setCurValue(String.valueOf(seq + seqCoun));
			seqMngInfDao.saveOrUpdate(seqMngInf);
			currentMap.put(seqMngInf.getTableName(), seqList);
		}
	}

	@Override
	public String initProductCode() {
		TbSeqMngInf sp = seqMngInfDao.get("from TbSeqMngInf f where tableName='PRODUCT_CODE'");
		Long code = Long.parseLong(sp.getCurValue());
		Long max = Long.parseLong(sp.getMaxValue());
		Long min = Long.parseLong(sp.getMinValue());
		Long newCode = code + 1;
		if (newCode > max) {
			newCode = min;
			sp.setCurValue(sp.getMinValue());
		}
		sp.setCurValue(String.valueOf(newCode));
		seqMngInfDao.update(sp);
		return StringUtil.leftPad(code.toString(), 8, '0');
	}
	@Override
	public Long initSysParamId() {
		TbSeqMngInf sp = seqMngInfDao.get("from TbSeqMngInf f where tableName='TB_SYS_PARAMETER_SEQ'");
		Long code = Long.parseLong(sp.getCurValue());
		Long max = Long.parseLong(sp.getMaxValue());
		Long min = Long.parseLong(sp.getMinValue());
		Long newCode = code + 1;
		if (newCode > max) {
			newCode = min;
			sp.setCurValue(sp.getMinValue());
		}
		sp.setCurValue(String.valueOf(newCode));
		seqMngInfDao.update(sp);
		return code;
	}
	
	@Override
	public String initContractCode() {
		TbSeqMngInf sp = seqMngInfDao.get("from TbSeqMngInf f where tableName='CONTRACT_CODE'");
		Long code = Long.parseLong(sp.getCurValue());
		String contractCode = StringUtil.leftPad(String.valueOf(code - 1), 8, '0');
		TbMerchantContract ct = contractDao.get("from TbMerchantContract f where f.id='" + contractCode + "'");
		if (ct != null) {
			Long max = Long.parseLong(sp.getMaxValue());
			Long min = Long.parseLong(sp.getMinValue());
			Long newCode = code + 1;
			if (newCode > max) {
				newCode = min;
				sp.setCurValue(sp.getMinValue());
			} else {
				sp.setCurValue(String.valueOf(newCode));
			}
			seqMngInfDao.update(sp);
			return StringUtil.leftPad(String.valueOf(code), 8, '0');
		} else {
			return contractCode;
		}
	}
}
