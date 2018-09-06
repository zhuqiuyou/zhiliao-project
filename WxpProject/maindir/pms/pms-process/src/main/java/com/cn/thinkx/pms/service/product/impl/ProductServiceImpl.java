package com.cn.thinkx.pms.service.product.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.key.TbKeyIndex;
import com.cn.thinkx.pms.model.key.TbKeyVersion;
import com.cn.thinkx.pms.model.product.TbProduct;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.product.Product;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.product.ProductService;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private BaseDaoI<TbProduct> productDao;
	@Autowired
	private BaseDaoI<TbKeyIndex> KeyIndexDao;
	@Autowired
	private CommonService commonService;

	@Override
	public List<Product> dataGrid(Product product, PageFilter ph) {
		List<Product> kl = new ArrayList<Product>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbProduct t ";
		List<TbProduct> l = productDao.find(hql + whereHql(product, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbProduct t : l) {
			Product u = new Product();
			BeanUtils.copyProperties(t, u);
			u.setMaxBalance(StringUtil.fromCentToYuan(t.getMaxBalance()));
			u.setVersionId(t.getTbKeyVersion().getVersionId());
			kl.add(u);
		}
		return kl;
	}

	@Override
	public List<Product> findProductList(Map<String, Object> params) {
		String hql = " from TbProduct t where 1=1 ";
		List<TbProduct> l = null;
		if (params != null) {
			for (String key : params.keySet()) {
				hql += " and t." + key + " = :" + key + "";
			}
			l = productDao.find(hql, params);
		} else {
			l = productDao.find(hql);
		}
		List<Product> kl = new ArrayList<Product>();
		for (TbProduct t : l) {
			Product u = new Product();
			BeanUtils.copyProperties(t, u);
			kl.add(u);
		}
		return kl;
	}

	private String whereHql(Product kv, Map<String, Object> params) {
		String hql = "";
		if (kv != null) {
			hql += " where 1=1 ";
			if (!StringUtil.isNullOrEmpty(kv.getProductName())) {
				hql += " and t.productName like :productName";
				params.put("productName", "%%" + kv.getProductName() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(kv.getVersionCode())) {
				hql += " and t.tbKeyVersion.versionCode = :versionCode";
				params.put("versionCode", kv.getVersionCode());
			}
			if (kv.getCreateTimeStart() != null) {
				hql += " and t.createTime >= :createTimeStart";
				params.put("createTimeStart", kv.getCreateTimeStart());
			}
			if (kv.getCreateTimeEnd() != null) {
				hql += " and t.createTime <= :createTimeEnd";
				params.put("createTimeEnd", kv.getCreateTimeEnd());
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
	public Long count(Product kv, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbProduct t ";
		return productDao.count("select count(*) " + hql + whereHql(kv, params), params);
	}

	@Override
	public void add(Product p) {
		TbProduct product = new TbProduct();
		BeanUtils.copyProperties(p, product);
		product.setDataStat(GlobalConstant.START_STOP_STATUS.START.getValue());
		product.setLastCardNum(0);
		product.setProductCode(commonService.initProductCode());

		List<TbKeyIndex> kv = KeyIndexDao
				.find("select t from TbKeyIndex t  left join  t.tbKeyVersion v where v.versionId='" + p.getVersionId()
						+ "'");
		for (TbKeyIndex ki : kv) {
			TbKeyVersion t = ki.getTbKeyVersion();
			if ("CVV".equals(ki.getKeyName())) {
				product.setCvvKeyIndex(ki.getKeyIndex());
			} else if ("PWD".equals(ki.getKeyName())) {
				product.setPwdKeyIndex(ki.getKeyIndex());
			} else if ("BAL".equals(ki.getKeyName())) {
				product.setBalKeyIndex(ki.getKeyIndex());
			}
			product.setTbKeyVersion(t);
		}
		productDao.save(product);
	}

	@Override
	public void delete(String id) {
		TbProduct kv = productDao.get(" from TbProduct p where p.productCode='" + id + "'");
		kv.setDataStat(GlobalConstant.START_STOP_STATUS.STOP.getValue());
		productDao.saveOrUpdate(kv);
	}

	@Override
	public void edit(Product p) {
		TbProduct tproduct = productDao.get("from TbProduct t where t.productCode='" + p.getProductCode() + "'");
		if (tproduct.getLockVersion() != p.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			p.setCreateTime(tproduct.getCreateTime());
			p.setCreateUser(tproduct.getCreateUser());
			String balKeyIndex = tproduct.getBalKeyIndex();
			String cvvKeyIndex = tproduct.getCvvKeyIndex();
			String pwdKeyIndex = tproduct.getPwdKeyIndex();
			if (!p.getVersionId().equals(tproduct.getTbKeyVersion().getVersionId())) {
				List<TbKeyIndex> kv = KeyIndexDao
						.find("select t from TbKeyIndex t left join fetch t.tbKeyVersion v where v.versionId='"
								+ p.getVersionId() + "'");
				for (TbKeyIndex ki : kv) {
					TbKeyVersion t = ki.getTbKeyVersion();
					if ("CVV".equals(ki.getKeyName())) {
						cvvKeyIndex = ki.getKeyIndex();
					} else if ("PWD".equals(ki.getKeyName())) {
						pwdKeyIndex = ki.getKeyIndex();
					} else if ("BAL".equals(ki.getKeyName())) {
						balKeyIndex = ki.getKeyIndex();
					}
					tproduct.setTbKeyVersion(t);
				}
			}
			BeanUtils.copyProperties(p, tproduct);
			tproduct.setBalKeyIndex(balKeyIndex);
			tproduct.setCvvKeyIndex(cvvKeyIndex);
			tproduct.setPwdKeyIndex(pwdKeyIndex);
			productDao.update(tproduct);
		}
	}

	@Override
	public Product get(String id) {
		TbProduct tkv = productDao.get("from TbProduct t where t.productCode='" + id + "'");
		Product kv = new Product();
		BeanUtils.copyProperties(tkv, kv);
		kv.setVersionId(tkv.getTbKeyVersion().getVersionId());
		return kv;
	}

	@Override
	public Product getProductByName(String productName) {
		TbProduct tkv = productDao.get("from TbProduct t where t.productName='" + productName + "'");
		if (tkv != null) {
			Product kv = new Product();
			BeanUtils.copyProperties(tkv, kv);
			kv.setVersionId(tkv.getTbKeyVersion().getVersionId());
			return kv;
		} else {
			return null;
		}
	}

	@Override
	public Product getProductByCardBin(String cardBin) {
		TbProduct tkv = productDao.get("from TbProduct t where t.cardBin='" + cardBin + "'");
		if (tkv != null) {
			Product kv = new Product();
			BeanUtils.copyProperties(tkv, kv);
			return kv;
		} else {
			return null;
		}
	}

	@Override
	public TbProduct get(String hql, Map<String, Object> params) {
		return productDao.get(hql, params);
	}

	public void setProductDao(BaseDaoI<TbProduct> productDao) {
		this.productDao = productDao;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

}
