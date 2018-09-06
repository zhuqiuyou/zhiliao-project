package com.cn.thinkx.pms.service.sys;

import java.util.List;
import java.util.Map;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.sys.Dictionary;
import com.cn.thinkx.pms.pageModel.sys.Dictionarytype;

public interface DictionaryServiceI {

	public List<Dictionary> dataGrid(Dictionary dictionary, PageFilter ph);

	public Long count(Dictionary dictionary, PageFilter ph);

	public void add(Dictionary dictionary);

	public void delete(Long id);

	public void edit(Dictionary dictionary);

	public Dictionary get(Long id);

	public List<Dictionary> combox(String code);

	public List<Dictionary> comboxCodeAndTextByTypeCode(String code);
	

	public List<Dictionary> comboxCodeAndTextByParentTypeCode(String code);

	public Map<String, String> comboxMap(String code);

	public Dictionary checkUnique(Dictionary dictionary);
	
	public List<Dictionarytype> getDictionarytypeByParentCode(String code);
}
