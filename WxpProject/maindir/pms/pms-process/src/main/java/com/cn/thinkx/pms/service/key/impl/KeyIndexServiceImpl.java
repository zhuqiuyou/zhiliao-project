package com.cn.thinkx.pms.service.key.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.key.KeyIndex;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.key.KeyIndexService;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class KeyIndexServiceImpl implements KeyIndexService {
	@Autowired
	private BaseDaoI<TbKeyIndex> KeyIndexDao;
	@Autowired
	private BaseDaoI<TbKeyVersion> TbKeyVersionDao;
	@Autowired
	private CommonService commonService;

	@Override
	public List<KeyIndex> dataGrid(KeyIndex kv, PageFilter ph) {
		List<KeyIndex> kl = new ArrayList<KeyIndex>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbKeyIndex t ";
		List<TbKeyIndex> l = KeyIndexDao.find(hql + whereHql(kv, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbKeyIndex t : l) {
			KeyIndex u = new KeyIndex();
			BeanUtils.copyProperties(t, u);
			u.setVersionId(t.getTbKeyVersion().getVersionId());
			kl.add(u);
		}
		return kl;
	}

	private String whereHql(KeyIndex kv, Map<String, Object> params) {
		String hql = "";
		if (kv != null) {
			hql += " where 1=1 ";
			if (StringUtil.isNotBlank(kv.getVersionId())) {
				hql += " and t.tbKeyVersion.versionId = :versionId";
				params.put("versionId", kv.getVersionId());
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
		if (!StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public Long count(KeyIndex kv, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbKeyIndex t ";
		return KeyIndexDao.count("select count(*) " + hql + whereHql(kv, params), params);
	}

	@Override
	public void add(List<KeyIndex> keyIndexs) {
		if (keyIndexs != null && keyIndexs.size() > 0) {
			for (KeyIndex keyIndex : keyIndexs) {
				TbKeyIndex kv = new TbKeyIndex();
				BeanUtils.copyProperties(keyIndex, kv);
				if (StringUtil.isNotEmpty(keyIndex.getVersionId())) {
					kv.setTbKeyVersion(TbKeyVersionDao.get(TbKeyVersion.class, keyIndex.getVersionId()));
				}
				kv.setKeyId(commonService.initSeqId(TbKeyIndex.class));
				KeyIndexDao.save(kv);
			}
		}
	}

	@Override
	public void delete(String id) {
		TbKeyIndex kv = KeyIndexDao.get("from TbKeyIndex t where t.keyId='" + id + "'");
		KeyIndexDao.delete(kv);
	}

	@Override
	public void edit(KeyIndex keyIndex) {
		TbKeyIndex tkv = KeyIndexDao.get("from TbKeyIndex t where t.keyId='" + keyIndex.getKeyId() + "'");
		if (tkv.getLockVersion() != keyIndex.getLockVersion()) {
			throw new LockTimeoutException("version had changed!!");
		} else {
			tkv.setUpdateTime(new Date());
			tkv.setUpdateUser(keyIndex.getUpdateUser());
			tkv.setKeyIndex(keyIndex.getKeyIndex());
			tkv.setRemarks(keyIndex.getRemarks());
			KeyIndexDao.update(tkv);
		}
	}

	@Override
	public KeyIndex get(String id) {
		TbKeyIndex tkv = KeyIndexDao.get("from TbKeyIndex t where t.keyId='" + id + "'");
		KeyIndex kv = new KeyIndex();
		BeanUtils.copyProperties(tkv, kv);
		kv.setVersionId(tkv.getTbKeyVersion().getVersionId());
		return kv;
	}

	@Override
	public KeyIndex getKeyIndexByCode(String keyIndex) {
		TbKeyIndex tkv = KeyIndexDao.get("from TbKeyIndex t where t.keyIndex='" + keyIndex + "'");
		if (tkv != null) {
			KeyIndex kv = new KeyIndex();
			BeanUtils.copyProperties(tkv, kv);
			return kv;
		} else {
			return null;
		}
	}

	@Override
	public List<KeyIndex> findKeyIndexListByVersionId(String versionId) {
		List<TbKeyIndex> l = KeyIndexDao
				.find("select t from TbKeyIndex t left join t.tbKeyVersion k where k.versionId='" + versionId + "'");
		List<KeyIndex> kl = new ArrayList<KeyIndex>();
		for (TbKeyIndex t : l) {
			KeyIndex u = new KeyIndex();
			BeanUtils.copyProperties(t, u);
			u.setVersionId(t.getTbKeyVersion().getVersionId());
			kl.add(u);
		}
		return kl;
	}
}
