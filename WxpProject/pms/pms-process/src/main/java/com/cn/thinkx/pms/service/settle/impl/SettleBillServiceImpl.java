package com.cn.thinkx.pms.service.settle.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.settle.TbSettleBill;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.settle.SettleBill;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.settle.SettleBillService;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class SettleBillServiceImpl implements SettleBillService {
	@Autowired
	private BaseDaoI<TbSettleBill> settleBillDao;
	@Autowired
	private CommonService commonService;
	
	@Override
	public List<SettleBill> dataGrid(SettleBill settleBill, PageFilter ph) {
		List<SettleBill> list = new ArrayList<SettleBill>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbSettleBill t  ";
		List<TbSettleBill> l = settleBillDao.find(hql + whereHql(settleBill, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbSettleBill t : l) {
			SettleBill sb = new SettleBill();
			sb.setMchntCode(t.getTbMerchantInf().getMchntCode());
			sb.setMchntName(t.getTbMerchantInf().getMchntName());
			BeanUtils.copyProperties(t, sb);
			sb.setTransAtSum(StringUtil.fromCentToYuan(sb.getTransAtSum()));
			sb.setSettleAtSum(StringUtil.fromCentToYuan(sb.getSettleAtSum()));
			sb.setSettleFee(StringUtil.fromCentToYuan(sb.getSettleFee()));
			list.add(sb);
		}
		return list;
	}

	private String whereHql(SettleBill settleBill, Map<String, Object> params) {
		String hql = "";
		if (settleBill != null) {
			hql += " where 1=1 ";
			if (!StringUtil.isNullOrEmpty(settleBill.getMchntCode())) {
				hql += " and t.tbMerchantInf.mchntCode = :mchntCode";
				params.put("mchntCode", settleBill.getMchntCode());
			}
			if (!StringUtil.isNullOrEmpty(settleBill.getBillStat())) {
				hql += " and t.billStat = :billStat";
				params.put("billStat", settleBill.getBillStat());
			}
			if (!StringUtil.isNullOrEmpty(settleBill.getSettleDateStart())) {
				hql += " and t.settleDateStart >= :settleDateStart";
				params.put("settleDateStart", settleBill.getSettleDateStart());
			}
			if (!StringUtil.isNullOrEmpty(settleBill.getSettleDateEnd())) {
				hql += " and t.settleDateEnd <= :settleDateEnd";
				params.put("settleDateEnd", settleBill.getSettleDateEnd());
			}
		}
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if (!StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public Long count(SettleBill settleBill, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select count(*)  from TbSettleBill t ";
		return settleBillDao.count( hql + whereHql(settleBill, params), params);
	}

	@Override
	public void add(SettleBill settleBill) {
		TbSettleBill entity = new TbSettleBill();
		BeanUtils.copyProperties(settleBill, entity);
		settleBill.setSettleId(commonService.initSeqId(TbSettleBill.class));
		settleBillDao.save(entity);
	}

	@Override
	public void delete(String id) {
		TbSettleBill sb = settleBillDao.get("from TbSettleBill t where t.settleId='" + id + "'");
		settleBillDao.delete(sb);
	}

	@Override
	public void edit(SettleBill settleBill) {
		TbSettleBill tsb = settleBillDao.get("from TbSettleBill t where t.settleId='" + settleBill.getSettleId() + "'");
		if (tsb.getLockVersion() != settleBill.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			settleBill.setCreateTime(tsb.getCreateTime());
			settleBill.setCreateUser(tsb.getCreateUser());
			String fee = StringUtil.fromYuanToCent(settleBill.getSettleFee());
			settleBill.setSettleFee(fee);
			String feeBefore = tsb.getSettleFee();
			// 手续费-修改后（分）
			long feeLong = Long.parseLong(fee);
			// 手续费-修改前（分）
			long feeLongBefore = Long.parseLong(feeBefore);
			// 结算金额 （分）
			long settleAtSum = Long.parseLong(tsb.getTransAtSum()) - feeLong;
			// 偏移量计算 （分）
			long feeOffset = feeLong - feeLongBefore + Long.parseLong(tsb.getFeeOffset());
			settleBill.setSettleAtSum(Long.toString(settleAtSum));
			settleBill.setFeeOffset(Long.toString(feeOffset));
			settleBill.setTransAtSum(StringUtil.fromYuanToCent(settleBill.getTransAtSum()));
			BeanUtils.copyProperties(settleBill, tsb);
			settleBillDao.update(tsb);
		}
	}

	@Override
	public SettleBill get(String settleId) {
		TbSettleBill tsb = settleBillDao.get(" from TbSettleBill t  where t.settleId='" + settleId + "'");
		SettleBill sb = new SettleBill();
		BeanUtils.copyProperties(tsb, sb);
		sb.setTransAtSum(StringUtil.fromCentToYuan(sb.getTransAtSum()));
		sb.setSettleAtSum(StringUtil.fromCentToYuan(sb.getSettleAtSum()));
		sb.setSettleFee(StringUtil.fromCentToYuan(sb.getSettleFee()));
		sb.setFeeOffset(StringUtil.fromCentToYuan(sb.getFeeOffset()));
		sb.setMchntName(tsb.getTbMerchantInf().getMchntName());
		sb.setMchntCode(tsb.getTbMerchantInf().getMchntCode());
		return sb;
	}
}
