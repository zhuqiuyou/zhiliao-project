package com.cn.thinkx.pms.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.sys.Dictionarytype;
import com.cn.thinkx.pms.pageModel.sys.SysParameter;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.service.sys.SysParamService;

@Controller
@RequestMapping("/systemparam")
public class SysParamController extends BaseController {

	@Autowired
	private SysParamService sysparamService;

	@Autowired
	private DictionaryServiceI dictionaryService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		List<Dictionarytype> dicList = dictionaryService.getDictionarytypeByParentCode("link_params");
		request.setAttribute("linkdownparams", JSON.toJSONString(dicList));
		request.setAttribute("downParams", dicList);
		return "/admin/posSystemParam";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(SysParameter param, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(sysparamService.dataGrid(param, ph));
		grid.setTotal(sysparamService.count(param, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, String type) {
		request.setAttribute("prmTypes", dictionaryService.getDictionarytypeByParentCode("link_params"));
		if ("10001".equals(type)) {
			
			request.setAttribute("prms", JSONArray.toJSONString(sysparamService.getTbSysParameterList(null,10001, 0)));
			return "/admin/posSystemParamAdd";
		} else {
			return "/admin/systemParamAdd";
		}
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(SysParameter param) {
		Json j = new Json();
		List<SysParameter> params = sysparamService.getSysParameterList(param);
		if (params != null && !params.isEmpty()) {
			j.setMsg("版本信息已存在!");
		} else {
			try {
				sysparamService.add(param);
				j.setSuccess(true);
				j.setMsg("添加成功！");
			} catch (Exception e) {
				j.setMsg(e.getMessage());
			}
		}
		return j;
	}

	@RequestMapping(value = "/addLink", method = RequestMethod.POST)
	@ResponseBody
	public Json addLink(HttpServletRequest request) {
		Json j = new Json();
		String prmVersion = request.getParameter("prmVersion");
		String prmType = request.getParameter("prmType");
		Integer version = Integer.parseInt(prmVersion);
		Integer type = Integer.parseInt(prmType);
		SysParameter sysparameter = sysparamService.findSysParameter(11, type, version);
		if (sysparameter != null) {
			j.setSuccess(false);
			j.setMsg("该版本下已经存在此数据！");
		} else {
			try {
				String[] prmName = request.getParameterValues("prmName");
				String[] prmVal = request.getParameterValues("prmVal");
				String[] prmLen = request.getParameterValues("prmLen");
				String[] prmDesc = request.getParameterValues("prmDesc");
				List<SysParameter> sysParamList = new ArrayList<SysParameter>();
				for (int i = 0; i < prmDesc.length; i++) {
					SysParameter sys = new SysParameter();
					sys.setId(Long.valueOf(i + 11));
					sys.setPrmType(type);
					sys.setPrmVersion(version);
					sys.setPrmName(prmName[i]);
					sys.setPrmVal(prmVal[i]);
					sys.setPrmLen(Integer.valueOf(prmLen[i]));
					sys.setPrmDesc(prmDesc[i]);
					sysParamList.add(sys);
				}
				sysparamService.addSysParameterList(sysParamList);
				j.setSuccess(true);
				j.setMsg("添加成功！");
			} catch (Exception e) {
				j.setMsg(e.getMessage());
			}
		}
		return j;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(Long id, Integer prmType, Integer prmVersion) {
		Json j = new Json();
		try {
			sysparamService.delete(id, prmType, prmVersion);
			j.setMsg("停用成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, Long id, Integer prmType, Integer prmVersion) {
		SysParameter u = sysparamService.get(id, prmType, prmVersion);
		request.setAttribute("systemParam", u);
		return "/admin/systemParamEdit";
	}
	@RequestMapping("/viewPage")
	public String viewPage(HttpServletRequest request, Long id, Integer prmType, Integer prmVersion) {
		SysParameter u = sysparamService.get(id, prmType, prmVersion);
		request.setAttribute("systemParam", u);
		return "/admin/systemParamView";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Json edit(SysParameter param) {
		Json j = new Json();
		/*SysParameter sysparameter = sysparamService.findSysParameter(param.getId().intValue(), param.getPrmType(),
				param.getPrmVersion());*/
		SysParameter condition = new SysParameter();
		condition.setPrmType(param.getPrmType());
		condition.setPrmVal(param.getPrmVal());
		condition.setPrmVersion(param.getPrmVersion());
		List<SysParameter> params = sysparamService.getSysParameterList(condition);
		if (!params.isEmpty() 
				&& params.get(0).getId().longValue() != param.getId().longValue()) {
			j.setSuccess(false);
			j.setMsg("该版本下已经存在此数据！");
		} else {
			try {
				sysparamService.edit(param);
				j.setSuccess(true);
				j.setMsg("编辑成功！");
			} catch (Exception e) {
				j.setMsg(e.getMessage());
			}
		}
		return j;
	}

}
