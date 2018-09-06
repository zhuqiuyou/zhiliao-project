package com.cn.thinkx.pms.service.key.impl;

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
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.key.KeyVersion;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.key.KeyVersionService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class KeyVersionServiceImpl implements KeyVersionService {
	@Autowired
	private BaseDaoI<TbKeyVersion> keyVersionDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private DictionaryServiceI dictionaryService;

	@Override
	public List<KeyVersion> dataGrid(KeyVersion kv, PageFilter ph) {
		List<KeyVersion> kl = new ArrayList<KeyVersion>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbKeyVersion t ";
		List<TbKeyVersion> l = keyVersionDao.find(hql + whereHql(kv, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbKeyVersion t : l) {
			KeyVersion u = new KeyVersion();
			BeanUtils.copyProperties(t, u);
			kl.add(u);
		}
		return kl;
	}

	private String whereHql(KeyVersion kv, Map<String, Object> params) {
		String hql = "";
		if (kv != null) {
			hql += " where 1=1 ";
			if (!StringUtil.isNullOrEmpty(kv.getVersionCode())) {
				hql += " and t.versionCode like :code";
				params.put("code", "%%" + kv.getVersionCode() + "%%");
			}
			if (!StringUtil.isNullOrEmpty(kv.getDftStat())) {
				hql += " and t.dftStat = :dftStat";
				params.put("dftStat", kv.getDftStat());
			}
			if (!StringUtil.isNullOrEmpty(kv.getVersionType())) {
				hql += " and t.versionType = :versionType";
				params.put("versionType", kv.getVersionType());
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
	public Long count(KeyVersion kv, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbKeyVersion t ";
		return keyVersionDao.count("select count(*) " + hql + whereHql(kv, params), params);
	}

	@Override
	public void add(KeyVersion keyVersion) {
		TbKeyVersion kv = new TbKeyVersion();
		BeanUtils.copyProperties(keyVersion, kv);
		kv.setVersionId(commonService.initSeqId(TbKeyVersion.class));
		keyVersionDao.save(kv);
	}

	@Override
	public void delete(String id) {
		TbKeyVersion kv = keyVersionDao.get("from TbKeyVersion t where t.versionId='" + id + "'");
		keyVersionDao.delete(kv);
	}

	@Override
	public void edit(KeyVersion keyVersion) {
		TbKeyVersion tkv = keyVersionDao.get("from TbKeyVersion t where t.versionId='" + keyVersion.getVersionId()
				+ "'");
		if (tkv.getLockVersion() != keyVersion.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			tkv.setDftStat(keyVersion.getDftStat());
			tkv.setVersionCode(keyVersion.getVersionCode());
			tkv.setVersionType(keyVersion.getVersionType());

			tkv.setUpdateTime(keyVersion.getUpdateTime());
			tkv.setUpdateUser(keyVersion.getUpdateUser());
			keyVersionDao.update(tkv);
		}
	}

	@Override
	public KeyVersion get(String id) {
		TbKeyVersion tkv = keyVersionDao.get("from TbKeyVersion t where t.versionId='" + id + "'");
		KeyVersion kv = new KeyVersion();
		BeanUtils.copyProperties(tkv, kv);
		return kv;
	}

	@Override
	public KeyVersion getKeyVersionByCode(String versionCode) {
		TbKeyVersion tkv = keyVersionDao.get("from TbKeyVersion t where t.versionCode='" + versionCode + "'");
		if (tkv != null) {
			KeyVersion kv = new KeyVersion();
			BeanUtils.copyProperties(tkv, kv);
			return kv;
		} else {
			return null;
		}
	}

	@Override
	public void grant(KeyVersion keyVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tree> tree() {
		List<Tree> lt = new ArrayList<Tree>();
		List<TbKeyVersion> kvList = keyVersionDao.find("select distinct t from TbKeyVersion t order by t.versionId");
		if ((kvList != null) && (kvList.size() > 0)) {
			for (TbKeyVersion r : kvList) {
				Tree tree = new Tree();
				tree.setId(r.getVersionId());
				tree.setText(r.getVersionCode());

				tree.setAttributes(new String[] { r.getVersionType(),
						GlobalConstant.keyTypelist.get(r.getVersionType()) });
				lt.add(tree);
			}
		}
		return lt;
	}

	public List<Tree> treeWithoutIndex() {
		List<Tree> lt = new ArrayList<Tree>();
		String hql = "select t from TbKeyVersion t left join t.keyIndexSet ks where 1=1 and ks is null order by t.createTime desc";
		List<TbKeyVersion> kvList = keyVersionDao.find(hql);
		if ((kvList != null) && (kvList.size() > 0)) {
			for (TbKeyVersion r : kvList) {
				Tree tree = new Tree();
				tree.setId(r.getVersionId());
				tree.setText(r.getVersionCode());

				tree.setAttributes(new String[] { r.getVersionType(),
						GlobalConstant.keyTypelist.get(r.getVersionType()) });
				lt.add(tree);
			}
		}
		return lt;
	}

	@Override
	public List<KeyVersion> findKeyVersionList(Map<String, Object> params) {
		String hql = "select  distinct t from TbKeyVersion t  join t.keyIndexSet kt where 1=1";
		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				if (key.equals("keyIndexExist")) {
					continue;
				}
				hql = hql + " and t." + key + "='" + params.get(key) + "'";
			}
			if (params.containsKey("keyIndexExist")) {
				hql = hql + " and kt is not null";
			}
		}
		List<TbKeyVersion> l = keyVersionDao.find(hql);
		List<KeyVersion> kl = new ArrayList<KeyVersion>();
		for (TbKeyVersion t : l) {
			KeyVersion u = new KeyVersion();
			BeanUtils.copyProperties(t, u);
			kl.add(u);
		}
		return kl;
	}

	@Override
	public List<KeyVersion> getAll() {
		List<KeyVersion> kl = new ArrayList<KeyVersion>();
		List<TbKeyVersion> l = keyVersionDao.find("from TbKeyVersion t ");
		for (TbKeyVersion t : l) {
			KeyVersion u = new KeyVersion();
			BeanUtils.copyProperties(t, u);
			kl.add(u);
		}
		return kl;
	}
}
