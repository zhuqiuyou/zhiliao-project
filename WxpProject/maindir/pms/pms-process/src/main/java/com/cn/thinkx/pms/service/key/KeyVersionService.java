package com.cn.thinkx.pms.service.key;

import java.util.List;
import java.util.Map;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.key.KeyVersion;

public interface KeyVersionService {

	public List<KeyVersion> findKeyVersionList(Map<String ,Object> params);
	
	public List<KeyVersion> dataGrid(KeyVersion keyVersion, PageFilter ph);

	public Long count(KeyVersion keyVersion, PageFilter ph);

	public void add(KeyVersion keyVersion);

	public void delete(String id);

	public void edit(KeyVersion keyVersion);

	public KeyVersion get(String id);

	public void grant(KeyVersion keyVersion);

	public List<Tree> tree();

	public KeyVersion getKeyVersionByCode(String versionCode);

	public List<Tree> treeWithoutIndex();
	
	public List<KeyVersion> getAll();
}
