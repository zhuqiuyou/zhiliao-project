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
import com.cn.thinkx.pms.model.key.TbKeyVersion;
import com.cn.thinkx.pms.model.shop.TbMerchantInf;
import com.cn.thinkx.pms.model.shop.TbPosInf;
import com.cn.thinkx.pms.model.shop.TbShopInf;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.shop.PosInf;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.shop.PosInfService;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class PosInfServiceImpl implements PosInfService {
	@Autowired
	private BaseDaoI<TbPosInf> PosInfDao;
	@Autowired
	private BaseDaoI<TbShopInf> shopInfDao;
	@Autowired
	private BaseDaoI<TbKeyVersion> keyVersionDao;
	@Autowired
	private CommonService commonService;

	@Override
	public List<PosInf> dataGrid(PosInf shop, PageFilter ph) {
		List<PosInf> posList = new ArrayList<PosInf>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select t from TbPosInf t  left join t.tbShopInf s left join t.tbKeyVersion v";
		List<TbPosInf> l = PosInfDao.find(hql + whereHql(shop, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbPosInf t : l) {
			PosInf u = new PosInf();
			BeanUtils.copyProperties(t, u);
			u.setShopId(t.getTbShopInf().getShopId());
			posList.add(u);
		}
		return posList;
	}

	private String whereHql(PosInf pos, Map<String, Object> params) {
		String hql = "";
		if (pos != null) {
			hql += " where 1=1 ";
			if (StringUtil.isNotBlank(pos.getShopId())) {
				hql += " and s.shopId = :shopId";
				params.put("shopId", pos.getShopId());
			}
			
			if (StringUtil.isNotBlank(pos.getShopCode())) {
				hql += " and t.shopCode like :shopCode";
				params.put("shopCode", "%%" +pos.getShopCode() +"%%");
			}
			
			if (StringUtil.isNotBlank(pos.getTermId())) {
				hql += " and t.termId like :termId";
				params.put("termId", "%%" +pos.getTermId() +"%%");
			}
			if (pos.getCreateTimeStart() != null) {
				hql += " and t.createTime >= :createTimeStart";
				params.put("createTimeStart", pos.getCreateTimeStart());
			}
			if (pos.getCreateTimeEnd() != null) {
				hql += " and t.createTime <= :createTimeEnd";
				params.put("createTimeEnd", pos.getCreateTimeEnd());
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
	public Long count(PosInf shop, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select count(t.id)  from TbPosInf t  left join t.tbShopInf s left join t.tbKeyVersion v";
		return PosInfDao.count( hql + whereHql(shop, params), params);
	}

	@Override
	public void add(PosInf posInf) {
		TbPosInf pos = new TbPosInf();
		BeanUtils.copyProperties(posInf, pos);
		if (StringUtil.isNotEmpty(posInf.getShopId())) {
			TbShopInf ts = shopInfDao.get(TbShopInf.class, posInf.getShopId());
			pos.setTbShopInf(ts);
			pos.setShopCode(ts.getShopCode());
			TbMerchantInf mf =ts.getMerchantInf();
			pos.setMchntCode(mf.getMchntCode());
			pos.setMchntName(mf.getMchntName());
		}
		if (StringUtil.isNotEmpty(posInf.getVersionId())) {
			pos.setTbKeyVersion(keyVersionDao.get(TbKeyVersion.class, posInf.getVersionId()));
		}
		pos.setId(commonService.initSeqId(TbPosInf.class));
		PosInfDao.save(pos);
	}

	@Override
	public void delete(String id) {
		TbPosInf kv = PosInfDao.get("from TbPosInf t where t.id='" + id + "'");
		PosInfDao.delete(kv);
	}

	@Override
	public void edit(PosInf pos) {
		TbPosInf tkv = PosInfDao.get("from TbPosInf t where t.id='" + pos.getId() + "'");
		if (tkv.getLockVersion() != pos.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			pos.setCreateTime(tkv.getCreateTime());
			pos.setCreateUser(tkv.getCreateUser());
			BeanUtils.copyProperties(pos, tkv);
			if (StringUtil.isNotEmpty(pos.getShopId())) {
				tkv.setTbShopInf(shopInfDao.get(TbShopInf.class, pos.getShopId()));
			}
			if (StringUtil.isNotEmpty(pos.getVersionId())) {
				tkv.setTbKeyVersion(keyVersionDao.get(TbKeyVersion.class, pos.getVersionId()));
			}
			PosInfDao.update(tkv);
		}
	}

	@Override
	public PosInf get(String id) {
		TbPosInf tkv = PosInfDao.get(" select t from TbPosInf t  left join t.tbShopInf s left join t.tbKeyVersion v where t.id='" + id + "'");
		PosInf kv = new PosInf();
		BeanUtils.copyProperties(tkv, kv);
		kv.setShopId(tkv.getTbShopInf().getShopId());
		kv.setVersionId(tkv.getTbKeyVersion().getVersionId());
//		kv.setMchntId(tkv.getMerchantInf().getMchntId());
		return kv;
	}

	@Override
	public PosInf getPosInfByTermId(String code) {
		TbPosInf tShop = PosInfDao.get("from TbPosInf t where t.termId='" + code + "'");
		if (tShop != null) {
			PosInf shop = new PosInf();
			BeanUtils.copyProperties(tShop, shop);
			return shop;
		} else {
			return null;
		}
	}
}
