package com.cn.thinkx.pms.service.contract.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.contract.TbMerchantContract;
import com.cn.thinkx.pms.model.contract.TbMerchantContractList;
import com.cn.thinkx.pms.model.shop.TbMerchantInf;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.contract.MerchantContract;
import com.cn.thinkx.pms.pageModel.contract.MerchantContractList;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.contract.MerchantContractService;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class MerchantContractServiceImpl implements MerchantContractService {

	@Autowired
	private BaseDaoI<TbMerchantContract> contractDao;
	@Autowired
	private BaseDaoI<TbMerchantContractList> contractListDao;
	@Autowired
	private CommonService commonService;

	@Autowired
	private BaseDaoI<TbMerchantInf> merchantInfDao;

	@Override
	public List<MerchantContract> dataGrid(MerchantContract contract, PageFilter ph) {
		List<MerchantContract> kl = new ArrayList<MerchantContract>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbMerchantContract t ";
		List<TbMerchantContract> l = contractDao.find(hql + whereHql(contract, params) + orderHql(ph), params,
				ph.getPage(), ph.getRows());
		for (TbMerchantContract t : l) {
			MerchantContract u = new MerchantContract();
			BeanUtils.copyProperties(t, u);
			u.setMchntName(t.getMerchantInf().getMchntName());
			kl.add(u);
		}
		return kl;
	}

	private String whereHql(MerchantContract contract, Map<String, Object> params) {
		String hql = "";
		if (contract != null) {
			hql += " where 1=1 ";
			if (!StringUtil.isNullOrEmpty(contract.getMchntCode())) {
				hql += " and t.mchntCode like :mchntCode";
				params.put("mchntCode", "%%" + contract.getMchntCode() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(contract.getMchntName())) {
				hql += " and t.merchantInf.mchntName like :mchntName";
				params.put("mchntName", "%%" + contract.getMchntName() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(contract.getId())) {
				hql += " and t.id like :id";
				params.put("id", "%%" + contract.getId() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(contract.getContractStartDate())) {
				hql += " and t.contractStartDate >= :contractStartDate";
				params.put("contractStartDate", contract.getContractStartDate());
			}
			if (!StringUtil.isNullOrEmpty(contract.getContractEndDate())) {
				hql += " and t.contractEndDate <= :contractEndDate";
				params.put("contractEndDate", contract.getContractEndDate());
			}
		}
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if (ph != null && !StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public Long count(MerchantContract contract, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbMerchantContract t ";
		return contractDao.count("select count(*) " + hql + whereHql(contract, params), params);
	}

	@Override
	public void add(MerchantContract contract) {
		TbMerchantContract tb = new TbMerchantContract();
		BeanUtils.copyProperties(contract, tb);
		tb.setDataStat(GlobalConstant.PUBLIC_ORDER_STATUS.DRAFT.getValue());
		TbMerchantInf merchantInf = merchantInfDao.get(TbMerchantInf.class, contract.getMerchantInfId());
		tb.setMerchantInf(merchantInf);
		tb.setDataStat("0");// 有效 1失效
		Date dateStart = null;
		try {
			dateStart = DateUtil.convertStringToDate("yyyyMMdd", contract.getContractStartDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dateStart != null) {
			String yestoday = DateUtil.getStringFromDate(DateUtil.getBeforeDate(dateStart, 1), "yyyyMMdd");
			tb.setPreSettleDate(yestoday);
		}
		tb.setMchntCode(merchantInf.getMchntCode());
		if (contract.getMerchantContractListSet() != null) {
			for (MerchantContractList mc : contract.getMerchantContractListSet()) {
				TbMerchantContractList tmcl = new TbMerchantContractList();
				BeanUtils.copyProperties(mc, tmcl);
				tmcl.setId(commonService.initSeqId(TbMerchantContractList.class));
				tmcl.setMerchantContract(tb);
				tb.getMerchantContractLists().add(tmcl);
			}
		}
		contractDao.save(tb);
	}

	@Override
	public void delete(String id) {
		String hql = " from TbMerchantContract t where t.id='" + id + "'";
		TbMerchantContract tb = contractDao.get(hql);
		tb.setDataStat("1");
		String today = DateUtil.getStringFromDate(new Date(), "yyyyMMdd");
		if (tb.getContractStartDate().compareTo(today) > 0) {
			tb.setContractStartDate(today);
		}
		if (tb.getContractEndDate().compareTo(today) > 0) {
			tb.setContractEndDate(today);
		}
		contractDao.update(tb);
		TbMerchantContract tmc = contractDao.get(TbMerchantContract.class, id);
		/* 更新明细 */
		List<TbMerchantContractList> list = tmc.getMerchantContractLists();
		for (TbMerchantContractList l : list) {
			deleteDetail(l.getId());
		}
	}

	@Override
	public void edit(MerchantContract m, String userName) {
		TbMerchantContract tmc = contractDao.get(TbMerchantContract.class, m.getId());
		if (tmc != null) {
			/* 结算日期为上一天 */
			String yestoday = DateUtil.getStringFromDate(DateUtil.getBeforeDate(new Date(), 1), "yyyyMMdd");
			m.setPreSettleDate(yestoday);
			m.setCreateTime(tmc.getCreateTime());
			m.setCreateUser(tmc.getCreateUser());
			m.setMchntCode(tmc.getMchntCode());
			m.setDataStat(tmc.getDataStat());
			BeanUtils.copyProperties(m, tmc);
			contractDao.update(tmc);
			/* 更新明细：保存新增条目 ；更新条目 */
			List<MerchantContractList> pageCtlListSet = m.getMerchantContractListSet();
			if (pageCtlListSet != null && pageCtlListSet.size() > 0) {
				for (MerchantContractList pageList : pageCtlListSet) {
					TbMerchantContractList tmcl = null;
					if (pageList.getId() != null) {
						/* 编辑页面条目 */
						for (TbMerchantContractList mcl : tmc.getMerchantContractLists()) {
							if (mcl.getId().equals(pageList.getId())) {
								tmcl = mcl;
								break;
							}
						}
						tmcl.setUpdateTime(new Date());
						tmcl.setUpdateUser(userName);
						tmcl.setContractEndDate(pageList.getContractEndDate());
						tmcl.setContractStartDate(pageList.getContractStartDate());
						tmcl.setDataStat(pageList.getDataStat());
						contractListDao.update(tmcl);
					} else {
						tmcl = new TbMerchantContractList();
						pageList.setCreateTime(new Date());
						pageList.setCreateUser(userName);
						if (StringUtil.isNullOrEmpty(pageList.getDataStat())) {
							pageList.setDataStat("0");
						}
						pageList.setId(commonService.initSeqId(TbMerchantContractList.class));
						tmc.getMerchantContractLists().add(tmcl);
						pageList.setUpdateTime(new Date());
						pageList.setUpdateUser(userName);

						BeanUtils.copyProperties(pageList, tmcl);
						tmcl.setMerchantContract(tmc);
						contractListDao.save(tmcl);
					}
				}
			}
		}
	}

	@Override
	public MerchantContract get(String id) {
		String hql = " from TbMerchantContract t where t.id='" + id + "'";
		TbMerchantContract tb = contractDao.get(hql);

		MerchantContract m = new MerchantContract();
		BeanUtils.copyProperties(tb, m);
		List<TbMerchantContractList> tcs = tb.getMerchantContractLists();
		List<MerchantContractList> mcs = new ArrayList<MerchantContractList>();
		for (TbMerchantContractList tc : tcs) {
			MerchantContractList mc = new MerchantContractList();
			BeanUtils.copyProperties(tc, mc);
			mcs.add(mc);
		}

		m.setMerchantInfId(tb.getMerchantInf().getMchntId());
		m.setMerchantContractListSet(mcs);
		return m;
	}

	@Override
	public String initContractCode() {
		return commonService.initContractCode();
	}

	@Override
	public void deleteDetail(String detailId) {
		String hql = " from TbMerchantContractList t where t.id='" + detailId + "'";
		TbMerchantContractList tb = contractListDao.get(hql);
		tb.setDataStat("1");
		String today = DateUtil.getStringFromDate(new Date(), "yyyyMMdd");
		if (tb.getContractStartDate().compareTo(today) > 0) {
			tb.setContractStartDate(today);
		}
		if (tb.getContractEndDate().compareTo(today) > 0) {
			tb.setContractEndDate(today);
		}
		tb.setContractEndDate(today);
		contractListDao.update(tb);
	}

	@Override
	public List<MerchantContract> queryByMerchantId(String merchantId) {
		String hql = " from TbMerchantContract t where t.merchantInf.mchntId='" + merchantId + "'";
		List<TbMerchantContract> l = contractDao.find(hql);
		List<MerchantContract> kl = new ArrayList<MerchantContract>();
		for (TbMerchantContract t : l) {
			MerchantContract u = new MerchantContract();
			BeanUtils.copyProperties(t, u);
			u.setMchntName(t.getMerchantInf().getMchntName());
			kl.add(u);
		}
		return kl;
	}

}
