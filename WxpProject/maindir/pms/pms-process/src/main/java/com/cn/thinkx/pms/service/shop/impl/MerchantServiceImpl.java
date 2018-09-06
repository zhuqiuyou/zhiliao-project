package com.cn.thinkx.pms.service.shop.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.shop.TbMerchantInf;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.shop.MerchantInf;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.shop.MerchantService;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class MerchantServiceImpl implements MerchantService {
	@Autowired
	private BaseDaoI<TbMerchantInf> merchantInfDao;
	@Autowired
	private CommonService commonService;

	@Override
	public List<MerchantInf> findMerchantList(MerchantInf merchantInf) {
		List<MerchantInf> kl = new ArrayList<MerchantInf>();
		String hql = " from TbMerchantInf t where t. dataStat='" + GlobalConstant.START_STOP_STATUS.START.getValue()
				+ "'";
		List<TbMerchantInf> l = merchantInfDao.find(hql);
		for (TbMerchantInf t : l) {
			MerchantInf u = new MerchantInf();
			BeanUtils.copyProperties(t, u);
			kl.add(u);
		}
		return kl;
	}

	@Override
	public List<MerchantInf> dataGrid(MerchantInf MerchantInf, PageFilter ph) {
		List<MerchantInf> kl = new ArrayList<MerchantInf>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbMerchantInf t ";
		List<TbMerchantInf> l = merchantInfDao.find(hql + whereHql(MerchantInf, params) + orderHql(ph), params,
				ph.getPage(), ph.getRows());
		for (TbMerchantInf t : l) {
			MerchantInf u = new MerchantInf();
			BeanUtils.copyProperties(t, u);
			kl.add(u);
		}
		return kl;
	}

	private String whereHql(MerchantInf mi, Map<String, Object> params) {
		String hql = "";
		if (mi != null) {
			hql += " where 1=1 ";
			if (!StringUtil.isNullOrEmpty(mi.getMchntName())) {
				hql += " and t.mchntName like :mchntName";
				params.put("mchntName", "%%" + mi.getMchntName() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(mi.getMchntCode())) {
				hql += " and t.mchntCode like :mchntCode";
				params.put("mchntCode", "%%" + mi.getMchntCode() + "%%");
			}
			if (mi.getCreateTimeStart() != null) {
				hql += " and t.createTime >= :createTimeStart";
				params.put("createTimeStart", mi.getCreateTimeStart());
			}
			if (mi.getCreateTimeEnd() != null) {
				hql += " and t.createTime <= :createTimeEnd";
				params.put("createTimeEnd", mi.getCreateTimeEnd());
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
	public Long count(MerchantInf kv, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbMerchantInf t ";
		return merchantInfDao.count("select count(*) " + hql + whereHql(kv, params), params);
	}

	@Override
	public void add(MerchantInf mnt) {
		TbMerchantInf tbMnt = new TbMerchantInf();
		BeanUtils.copyProperties(mnt, tbMnt);
		tbMnt.setDataStat(GlobalConstant.START_STOP_STATUS.START.getValue());
		tbMnt.setMchntId(commonService.initSeqId(TbMerchantInf.class));
		merchantInfDao.save(tbMnt);
	}

	@Override
	public void delete(String id) {
		TbMerchantInf kv = merchantInfDao.get(" from TbMerchantInf p where p.mchntId='" + id + "'");
		kv.setDataStat(GlobalConstant.START_STOP_STATUS.STOP.getValue());
		merchantInfDao.saveOrUpdate(kv);
	}

	@Override
	public void edit(MerchantInf p) {
		TbMerchantInf tbMnt = merchantInfDao.get("from TbMerchantInf t where t.mchntId='" + p.getMchntId() + "'");
		if (tbMnt.getLockVersion() != p.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			p.setCreateTime(tbMnt.getCreateTime());
			p.setCreateUser(tbMnt.getCreateUser());
			BeanUtils.copyProperties(p, tbMnt);
			merchantInfDao.update(tbMnt);
		}
	}

	@Override
	public MerchantInf get(String id) {
		TbMerchantInf tkv = merchantInfDao.get("from TbMerchantInf t where t.mchntId='" + id + "'");
		MerchantInf kv = new MerchantInf();
		BeanUtils.copyProperties(tkv, kv);
		return kv;
	}

	@Override
	public MerchantInf getMerchantInfByCode(String mchntCode) {
		TbMerchantInf tkv = merchantInfDao.get("from TbMerchantInf t where t.mchntCode='" + mchntCode + "'");
		if (tkv != null) {
			MerchantInf kv = new MerchantInf();
			BeanUtils.copyProperties(tkv, kv);
			return kv;
		} else {
			return null;
		}
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	public List<Tree> tree() {
		List<Tree> lt = new ArrayList<Tree>();
		List<TbMerchantInf> mctList = merchantInfDao.find("select distinct t from TbMerchantInf t  where t.dataStat='"
				+ GlobalConstant.START_STOP_STATUS.START.getValue() + "' order by t.mchntCode");

		if ((mctList != null) && (mctList.size() > 0)) {
			for (TbMerchantInf r : mctList) {
				Tree tree = new Tree();
				tree.setId(r.getMchntId());
				tree.setText(r.getMchntName());
				lt.add(tree);
			}
		}
		return lt;
	}

}
