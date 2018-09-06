package com.cn.thinkx.pms.service.sys;

import java.util.List;

import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.sys.Resource;

public interface ResourceServiceI {

	public List<Resource> treeGrid();

	public void add(Resource resource);

	public void delete(Long id);

	public void edit(Resource resource);

	public Resource get(Long id);

	public List<Tree> tree(SessionInfo sessionInfo);

	public List<Tree> allTree(boolean flag);

	public List<String> resourceAllList();

}
