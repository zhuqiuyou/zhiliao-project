package com.cn.thinkx.pms.service.trans.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.trans.TbTransLog;
import com.cn.thinkx.pms.model.trans.TbTransLog1;
import com.cn.thinkx.pms.model.trans.TbTransLog2;
import com.cn.thinkx.pms.model.trans.TbTransLogHis;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.shop.ShopInf;
import com.cn.thinkx.pms.pageModel.trans.TransLog;
import com.cn.thinkx.pms.service.shop.ShopInfService;
import com.cn.thinkx.pms.service.trans.TransLogService;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class TransLogServiceImpl implements TransLogService {
	@Autowired
	private BaseDaoI<TbTransLog> logDao;
	@Autowired
	private BaseDaoI<TbTransLog1> log1Dao;
	@Autowired
	private BaseDaoI<TbTransLog2> log2Dao;
	@Autowired
	private BaseDaoI<TbTransLogHis> logHisDao;
	
	@Autowired
	private ShopInfService shopInfService;

	@Override
	public List<TransLog> dataGrid(TransLog log, PageFilter ph) {
		String wholeSql = "select t.*,p.MCHNT_NAME from ${tableName} t left join TB_POS_INF p on t.TERM_CODE = p.TERM_ID and p.MCHNT_CODE = t.MCHNT_CODE where 1=1";
		Map<String, Object> params = new HashMap<String, Object>();
		List<TransLog> list = new ArrayList<TransLog>();
		if (!StringUtil.isNullOrEmpty(log.getIsCurrent()) && "0".equals(log.getIsCurrent())) {
			wholeSql = "select * from ( "+wholeSql.replace("${tableName}", "TB_TRANS_LOG1") + whereHql(log, params, ph) + ") union all (select * from ("
					+ wholeSql.replace("${tableName}", "TB_TRANS_LOG2") + whereHql(log, params, ph) + "))";
			List<TbTransLog1> listT = log1Dao.findListBySql(wholeSql, params, TbTransLog1.class,ph.getPage(),
					ph.getRows());
			for (TbTransLog1 t : listT) {
				TransLog sb = new TransLog();
				BeanUtils.copyProperties(t, sb);
				list.add(sb);
			}
		} else {
			wholeSql = wholeSql.replace("${tableName}", "TB_TRANS_LOG_HIS") + whereHql(log, params, ph);
			List<TbTransLogHis> listT = logHisDao.findListBySql(wholeSql, params, TbTransLogHis.class,ph.getPage(),
					ph.getRows());
			for (TbTransLogHis t : listT) {
				TransLog sb = new TransLog();
				BeanUtils.copyProperties(t, sb);
				sb.setTransAmt(StringUtil.fromCentToYuan(sb.getTransAmt()));
				// TODO 临时方案，需要改进
				if (!StringUtil.isNullOrEmpty(sb.getMchntCode())) {
					if (sb.getMchntCode().equals("999999999999999")) {
						sb.setMchntName("管理商户");
						sb.setShopName("管理门店");
					} else {
						ShopInf s = shopInfService.getShopInfByMchntCode(sb.getMchntCode());
						if (s != null) {
							sb.setMchntName(s.getMchntName());
							sb.setShopName(s.getShopName());
						}
					}
				}
				
				list.add(sb);
			}
		}
		return list;
	}

	private String whereHql(TransLog log, Map<String, Object> params, PageFilter ph) {
		String whereSQL = " AND TRANS_ST not in(4,5) ";
		if (log != null) {
			if (!StringUtil.isNullOrEmpty(log.getMchntCode())) {
				whereSQL += " and t.MCHNT_CODE = :mchntCode";
				params.put("mchntCode", log.getMchntCode());
			}
			if (!StringUtil.isNullOrEmpty(log.getShopCode())) {
				whereSQL += " and p.SHOP_CODE = :shopCode";
				params.put("shopCode", log.getShopCode());
			}
			if (!StringUtil.isNullOrEmpty(log.getTermCode())) {
				whereSQL += " and t.TERM_CODE = :termCode";
				params.put("termCode", log.getTermCode());
			}
			if (!StringUtil.isNullOrEmpty(log.getCardNo())) {
				whereSQL += " and t.CARD_NO = :cardNo";
				params.put("cardNo", log.getCardNo());
			}
			if (!StringUtil.isNullOrEmpty(log.getTransId())) {
				whereSQL += " and  t.TRANS_ID = :transId";
				params.put("transId", log.getTransId());
			}
			String  settleTimeStart = null;
			String settleTimeEnd = null;
			if (!StringUtil.isNullOrEmpty(log.getIsCurrent()) && "0".equals(log.getIsCurrent())) {
				settleTimeStart = DateUtil.getStringFromDate(DateUtil.getCurrentDate(), DateUtil.FORMAT_YYYYMMDD);
				settleTimeEnd = settleTimeStart;
			} else {
				settleTimeStart = log.getSettleTimeStart();
				settleTimeEnd = log.getSettleTimeEnd();
			}
			
			whereSQL += " and t.settle_date >= :settleTimeStart";
			params.put("settleTimeStart", settleTimeStart);
			whereSQL += " and t.settle_date <= :settleTimeEnd";
			params.put("settleTimeEnd", settleTimeEnd);

			if (!StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
				whereSQL += " order by t." + ph.getSort() + " " + ph.getOrder();
			}
		}
		return whereSQL;
	}

	@Override
	public Long count(TransLog log, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		ph.setSort(null);
		String wholeSql = "select count(t.TXN_PRIMARY_KEY) from ${tableName} t left join TB_POS_INF p on t.TERM_CODE = p.TERM_ID and p.MCHNT_CODE = t.MCHNT_CODE where 1=1";
		if (!StringUtil.isNullOrEmpty(log.getIsCurrent()) && "0".equals(log.getIsCurrent())) {
			BigInteger bin1 = log1Dao.countBySql(wholeSql.replace("${tableName}", "TB_TRANS_LOG1") + whereHql(log, params, ph), params);
			BigInteger bin2 = log2Dao.countBySql(wholeSql.replace("${tableName}", "TB_TRANS_LOG2") + whereHql(log, params, ph), params);
			return bin1.add(bin2).longValue();
		} else {
			wholeSql = wholeSql.replace("${tableName}", "TB_TRANS_LOG_HIS") + whereHql(log, params, ph);
			BigInteger bin = logHisDao.countBySql(wholeSql, params);
			return bin.longValue();
		}
	}
	
	public List<TransLog> queryTransLog(TransLog log, PageFilter ph) {
		String wholeSql = "select t.* from ${tableName} t left join TB_POS_INF p on t.TERM_CODE = p.TERM_ID and p.MCHNT_CODE = t.MCHNT_CODE where 1=1";
		Map<String, Object> params = new HashMap<String, Object>();
		List<TransLog> list = new ArrayList<TransLog>();
		if (!StringUtil.isNullOrEmpty(log.getIsCurrent()) && "0".equals(log.getIsCurrent())) {
			wholeSql = "select * from ( "+wholeSql.replace("${tableName}", "TB_TRANS_LOG1") + whereHql(log, params, ph) + ") union all (select * from ("
					+ wholeSql.replace("${tableName}", "TB_TRANS_LOG2") + whereHql(log, params, ph) + "))";
			List<TbTransLog1> listT = log1Dao.findListBySql(wholeSql, params, TbTransLog1.class);
			for (TbTransLog1 t : listT) {
				TransLog sb = new TransLog();
				BeanUtils.copyProperties(t, sb);
				list.add(sb);
			}
		} else {
			wholeSql = wholeSql.replace("${tableName}", "TB_TRANS_LOG_HIS") + whereHql(log, params, ph);
			List<TbTransLogHis> listT = logHisDao.findListBySql(wholeSql, params, TbTransLogHis.class);
			for (TbTransLogHis t : listT) {
				TransLog sb = new TransLog();
				BeanUtils.copyProperties(t, sb);
				sb.setTransAmt(StringUtil.fromCentToYuan(sb.getTransAmt()));
				// TODO 临时方案，需要改进
				if (!StringUtil.isNullOrEmpty(sb.getMchntCode())) {
					if (sb.getMchntCode().equals("999999999999999")) {
						sb.setMchntName("管理商户");
						sb.setShopName("管理门店");
					} else {
						ShopInf s = shopInfService.getShopInfByMchntCode(sb.getMchntCode());
						if (s != null) {
							sb.setMchntName(s.getMchntName());
							sb.setShopName(s.getShopName());
						}
					}
				}
				list.add(sb);
			}
		}
		return list;
	}

}
