package com.cn.thinkx.pms.service.sys;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.sys.Organization;

public interface OrganizationServiceI {

	public List<Organization> treeGrid();

	public void add(Organization organization);

	public void delete(Long id);

	public void edit(Organization organization);

	public Organization get(Long id);

	public List<Tree> tree();

}
