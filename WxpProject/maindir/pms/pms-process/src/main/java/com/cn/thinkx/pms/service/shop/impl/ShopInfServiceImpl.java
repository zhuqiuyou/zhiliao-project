package com.cn.thinkx.pms.service.shop.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.shop.TbMerchantInf;
import com.cn.thinkx.pms.model.shop.TbShopInf;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.shop.ShopInf;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.shop.ShopInfService;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class ShopInfServiceImpl implements ShopInfService {
	@Autowired
	private BaseDaoI<TbShopInf> ShopInfDao;
	@Autowired
	private BaseDaoI<TbMerchantInf> merchantInfDao;
	@Autowired
	private CommonService commonService;

	@Override
	public List<ShopInf> findShopList(ShopInf shopInfo) {
		List<ShopInf> shopList = new ArrayList<ShopInf>();
		String hql = " select t from TbShopInf t left join t.merchantInf f where t.dataStat=:shopStatus and f.dataStat=:merchantStatus";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shopStatus", GlobalConstant.START_STOP_STATUS.START.getValue());
		params.put("merchantStatus", GlobalConstant.START_STOP_STATUS.START.getValue());
		List<TbShopInf> l = ShopInfDao.find(hql, params);
		for (TbShopInf t : l) {
			ShopInf u = new ShopInf();
			BeanUtils.copyProperties(t, u);
			TbMerchantInf mf = t.getMerchantInf();
			u.setMchntId(mf.getMchntId());
			u.setMchntCode(mf.getMchntCode());
			u.setMchntName(mf.getMchntName());
			shopList.add(u);
		}
		return shopList;
	}

	@Override
	public List<ShopInf> dataGrid(ShopInf shop, PageFilter ph) {
		List<ShopInf> shopList = new ArrayList<ShopInf>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select t from TbShopInf t left join t.merchantInf f";
		List<TbShopInf> l = ShopInfDao.find(hql + whereHql(shop, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbShopInf t : l) {
			ShopInf u = new ShopInf();

			BeanUtils.copyProperties(t, u);
			u.setMchntId(t.getMerchantInf().getMchntId());
			shopList.add(u);
		}
		return shopList;
	}

	private String whereHql(ShopInf shop, Map<String, Object> params) {
		String hql = "";
		if (shop != null) {
			hql += " where 1=1 ";
			if (StringUtil.isNotBlank(shop.getMchntId())) {
				hql += " and f.mchntId = :mchntId";
				params.put("mchntId", shop.getMchntId());
			}
			if (!StringUtil.isNullOrEmpty(shop.getShopCode())) {
				hql += " and t.shopCode like :shopCode";
				params.put("shopCode", "%%" + shop.getShopCode() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(shop.getShopName())) {
				hql += " and t.shopName like :shopName";
				params.put("shopName", "%%" + shop.getShopName() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(shop.getMchntName())) {
				hql += " and f.mchntName like :mchntName";
				params.put("mchntName", "%%" + shop.getMchntName() + "%%");
			}
			if (shop.getCreateTimeStart() != null) {
				hql += " and t.createTime >= :createTimeStart";
				params.put("createTimeStart", shop.getCreateTimeStart());
			}
			if (shop.getCreateTimeEnd() != null) {
				hql += " and t.createTime <= :createTimeEnd";
				params.put("createTimeEnd", shop.getCreateTimeEnd());
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
	public Long count(ShopInf shop, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbShopInf t left join t.merchantInf f";
		return ShopInfDao.count("select count(*) " + hql + whereHql(shop, params), params);
	}

	@Override
	public void add(ShopInf ShopInf) {
		TbShopInf kv = new TbShopInf();
		BeanUtils.copyProperties(ShopInf, kv);
		if (StringUtil.isNotEmpty(ShopInf.getMchntId())) {
			kv.setMerchantInf(merchantInfDao.get(TbMerchantInf.class, ShopInf.getMchntId()));
		}
		kv.setShopId(commonService.initSeqId(TbShopInf.class));
		ShopInfDao.save(kv);
	}

	@Override
	public void delete(String id) {
		TbShopInf kv = ShopInfDao.get("from TbShopInf t where t.shopId='" + id + "'");
		ShopInfDao.delete(kv);
	}

	@Override
	public void edit(ShopInf ShopInf) {
		TbShopInf tkv = ShopInfDao.get("from TbShopInf t where t.shopId='" + ShopInf.getShopId() + "'");
		if (tkv.getLockVersion() != ShopInf.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			ShopInf.setCreateTime(tkv.getCreateTime());
			ShopInf.setCreateUser(tkv.getCreateUser());
			BeanUtils.copyProperties(ShopInf, tkv);
			if (StringUtil.isNotEmpty(ShopInf.getMchntId())) {
				tkv.setMerchantInf(merchantInfDao.get(TbMerchantInf.class, ShopInf.getMchntId()));
			}
			ShopInfDao.update(tkv);
		}
	}

	@Override
	public ShopInf get(String id) {
		TbShopInf tkv = ShopInfDao.get("from TbShopInf t where t.shopId='" + id + "'");
		ShopInf kv = new ShopInf();
		BeanUtils.copyProperties(tkv, kv);
		kv.setMchntId(tkv.getMerchantInf().getMchntId());
		return kv;
	}

	@Override
	public ShopInf getShopInfByCode(String code) {
		TbShopInf tShop = ShopInfDao.get("from TbShopInf t where t.shopCode='" + code + "'");
		if (tShop != null) {
			ShopInf shop = new ShopInf();
			BeanUtils.copyProperties(tShop, shop);
			return shop;
		} else {
			return null;
		}
	}

	@Override
	public ShopInf getShopInfByMchntCode(String mchntCode) {
		String hql = " select t from TbShopInf t left join t.merchantInf f where f.mchntCode ='" + mchntCode + "'";
		TbShopInf tShop = ShopInfDao.get(hql);
		if (tShop != null) {
			ShopInf shop = new ShopInf();
			BeanUtils.copyProperties(tShop, shop);
			shop.setMchntName(tShop.getMerchantInf().getMchntName());
			return shop;
		} else {
			return null;
		}
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
				Set<TbShopInf> shopSet = r.getShopInfSet();
				if (shopSet != null) {
					List<Tree> children = new ArrayList<Tree>();
					for (TbShopInf shop : shopSet) {
						Tree t = new Tree();
						t.setId(shop.getShopId());
						t.setText(shop.getShopName());
						t.setPid(r.getMchntId());
						children.add(t);
					}
					tree.setChildren(children);
				}
				lt.add(tree);
			}
		}
		return lt;
	}
}
