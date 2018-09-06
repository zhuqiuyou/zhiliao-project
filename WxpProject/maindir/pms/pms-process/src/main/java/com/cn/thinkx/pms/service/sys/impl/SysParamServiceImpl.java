package com.cn.thinkx.pms.service.sys.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.model.sys.TbSysParameter;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.sys.SysParameter;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.sys.SysParamService;

@Service
public class SysParamServiceImpl implements SysParamService {

	@Autowired
	private BaseDaoI<TbSysParameter> sysParamDao;
	@Autowired
	private CommonService commonService;

	@Override
	public void add(SysParameter u) {
		TbSysParameter t = new TbSysParameter();
		BeanUtils.copyProperties(u, t);
		t.setId(commonService.initSysParamId());
		t.setPrmStat(false);
		sysParamDao.save(t);
	}

	@Override
	public void delete(Long id, Integer prmType, Integer prmVersion) {
		TbSysParameter t = getTbSysParameterList(id.intValue(), prmType, prmVersion).get(0);
		t.setPrmStat(true);
		sysParamDao.update(t);
	}

	@Override
	public void edit(SysParameter u) {
		TbSysParameter t = getTbSysParameterList(u.getId().intValue(), u.getPrmType(), u.getPrmVersion()).get(0);
		t.setPrmDesc(u.getPrmDesc());
		t.setPrmLen(u.getPrmLen());
		t.setPrmName(u.getPrmName());
		t.setPrmType(u.getPrmType());
		t.setPrmVal(u.getPrmVal());
		sysParamDao.update(t);
	}

	@Override
	public SysParameter get(Long id, Integer prmType, Integer prmVersion) {
		return findSysParameter(id.intValue(), prmType, prmVersion);
	}

	@Override
	public List<SysParameter> dataGrid(SysParameter param, PageFilter ph) {
		List<SysParameter> ul = new ArrayList<SysParameter>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbSysParameter t ";
		List<TbSysParameter> l = sysParamDao.find(hql + whereHql(param, params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		for (TbSysParameter t : l) {
			SysParameter u = new SysParameter();
			BeanUtils.copyProperties(t, u);
			ul.add(u);
		}
		return ul;
	}

	@Override
	public Long count(SysParameter param, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TbSysParameter t ";
		return sysParamDao.count("select count(*) " + hql + whereHql(param, params), params);
	}

	private String whereHql(SysParameter param, Map<String, Object> params) {
		String hql = "";
		if (param != null) {
			hql += " where 1=1 ";
			if (param.getPrmName() != null) {
				hql += " and t.prmName like :name";
				params.put("name", "%%" + param.getPrmName() + "%%");
			}
			if (param.getPrmVersion() != null) {
				hql += " and t.prmVersion = :prmVersion";
				params.put("prmVersion", param.getPrmVersion());
			}

			if (param.getPrmType() != null) {
				hql += " and t.prmType = :prmType";
				params.put("prmType", param.getPrmType());
			}
		}
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if ((ph.getSort() != null) && (ph.getOrder() != null)) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public SysParameter getByName(SysParameter param) {
		TbSysParameter t = sysParamDao.get("from TbSysParameter t  where t.prmName = '" + param.getPrmName() + "'");
		SysParameter u = new SysParameter();
		if (t != null) {
			BeanUtils.copyProperties(t, u);
		} else {
			return null;
		}
		return u;
	}

	@Override
	public void addSysParameterList(List<SysParameter> sysParamList) {
		for (SysParameter sysParameter : sysParamList) {
			TbSysParameter t = new TbSysParameter();
			BeanUtils.copyProperties(sysParameter, t);
			t.setPrmStat(false);
			sysParamDao.save(t);
		}
	}

	@Override
	public SysParameter findSysParameter(Integer id, Integer prmType, Integer prmVersion) {
		List<TbSysParameter> tl = getTbSysParameterList(id, prmType, prmVersion);
		if (tl != null) {
			for (TbSysParameter t : tl) {
				SysParameter u = new SysParameter();
				if (t != null) {
					BeanUtils.copyProperties(t, u);
					return u;
				}
			}
		}
		return null;
	}

	public List<TbSysParameter> getTbSysParameterList(Integer id, Integer prmType, Integer prmVersion) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t from TbSysParameter t  where 1=1 ");
		if (id != null) {
			sb.append(" and t.id=" + id);
		}
		if (prmType != null) {
			sb.append(" and t.prmType=" + prmType);
		}
		if (prmVersion != null) {
			sb.append(" and t.prmVersion=" + prmVersion);
		}
		sb.append(" order by id ");
		List<TbSysParameter> tl = sysParamDao.find(sb.toString());
		return tl;
	}
	
	public List<SysParameter> getSysParameterList(SysParameter param) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("select t from TbSysParameter t  where 1=1 ");
		if (param.getId() != null) {
			sb.append(" and t.id=" + param.getId());
		}
		if (param.getPrmType() != null) {
			sb.append(" and t.prmType=" + param.getPrmType());
		}
		if (param.getPrmVersion() != null) {
			sb.append(" and t.prmVersion=" + param.getPrmVersion());
		}
		
		if (param.getPrmVal() != null) {
			sb.append(" and t.prmVal=" + param.getPrmVal());
		}
		
		sb.append(" order by id ");
		List<TbSysParameter> tl = sysParamDao.find(sb.toString());
		List<SysParameter> params = new ArrayList<SysParameter>();
		for (TbSysParameter tmp : tl) {
			SysParameter paramTmp = new SysParameter();
			BeanUtils.copyProperties(tmp, paramTmp);
			params.add(paramTmp);
		}
		
		return params;
	}

	@Override
	public List<SysParameter> findVersionByType(int type) {
		String hql = "select t from TbSysParameter t  where 1=1 and t.prmType=" + type + " order by t.prmVersion";
		List<TbSysParameter> tl = sysParamDao.find(hql);
		List<SysParameter> sysList = new ArrayList<SysParameter>();
		if (tl != null) {
			Set<Integer> versionSet = new HashSet<Integer>();
			for (TbSysParameter t : tl) {
				Integer version = t.getPrmVersion();
				if (versionSet.contains(version)) {
					continue;
				} else {
					versionSet.add(version);
					SysParameter u = new SysParameter();
					BeanUtils.copyProperties(t, u);
					sysList.add(u);
				}
			}
		}
		if (sysList.size() == 0) {
			sysList.add(new SysParameter());
		}
		return sysList;
	}
}
