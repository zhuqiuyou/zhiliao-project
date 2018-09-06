package com.cn.thinkx.pms.service.key;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.key.KeyIndex;

public interface KeyIndexService {

	public List<KeyIndex> dataGrid(KeyIndex member, PageFilter ph);

	public Long count(KeyIndex member, PageFilter ph);

	public void add(List<KeyIndex> inds);

	public void delete(String id);

	public void edit(KeyIndex KeyIndex);

	public KeyIndex get(String id);

	public KeyIndex getKeyIndexByCode(String versionCode);

	public List<KeyIndex> findKeyIndexListByVersionId(String versionId);


}
