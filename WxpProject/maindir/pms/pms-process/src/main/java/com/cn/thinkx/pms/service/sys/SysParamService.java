package com.cn.thinkx.pms.service.sys;

import java.util.List;

import com.cn.thinkx.pms.model.sys.TbSysParameter;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.sys.SysParameter;

public interface SysParamService {

	public List<SysParameter> dataGrid(SysParameter param, PageFilter ph);

	public Long count(SysParameter param, PageFilter ph);

	public void add(SysParameter param);

	public void delete(Long id, Integer prmType, Integer prmVersion);

	public void edit(SysParameter param);

	public SysParameter getByName(SysParameter user);

	public void addSysParameterList(List<SysParameter> sysParamList);

	public SysParameter findSysParameter(Integer id, Integer prmType, Integer prmVersion);

	public SysParameter get(Long id, Integer prmType, Integer prmVersion);
	
	public List<TbSysParameter> getTbSysParameterList(Integer id, Integer prmType, Integer prmVersion);

	public List<SysParameter> findVersionByType(int i);
	
	public List<SysParameter> getSysParameterList(SysParameter param);
}
