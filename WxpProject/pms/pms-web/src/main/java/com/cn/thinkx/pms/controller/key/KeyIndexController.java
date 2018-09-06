package com.cn.thinkx.pms.controller.key;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.key.KeyIndex;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.key.KeyIndexService;
import com.cn.thinkx.pms.service.key.KeyVersionService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/keyindex")
public class KeyIndexController extends BaseController {

	@Autowired
	private KeyIndexService keyIndexService;
	@Autowired
	private KeyVersionService keyVersionService;
	@Autowired
	private DictionaryServiceI dictionaryService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("keyindextypeJson",
				JSON.toJSONString(dictionaryService.comboxCodeAndTextByParentTypeCode("secretversiontype")));
		request.setAttribute("versiontypeJson", JSON.toJSONString(keyVersionService.getAll()));
		return "/key/keyindex";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(KeyIndex keyIndex, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(keyIndexService.dataGrid(keyIndex, ph));
		grid.setTotal(keyIndexService.count(keyIndex, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		request.setAttribute("accountTypeJson",
				dictionaryService.comboxCodeAndTextByTypeCode("account_secretversiontype"));
		request.setAttribute("posTypeJson", dictionaryService.comboxCodeAndTextByTypeCode("pos_secretversiontype"));
		return "/key/keyindexAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(KeyIndex keyIndex, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		String keyType = keyIndex.getKeyIndex();
		String keyName = keyIndex.getKeyName();
		String remark = keyIndex.getRemarks();
		List<KeyIndex> tl = keyIndexService.findKeyIndexListByVersionId(keyIndex.getVersionId());
		if (tl != null) {
			j.setSuccess(false);
			j.setMsg("该密钥版本已经创建了密钥索引！");
		}
		if (!keyType.contains(",")) {
			j.setSuccess(false);
			j.setMsg("请填写索引明细");
		}
		try {
			List<KeyIndex> inds = new ArrayList<KeyIndex>();
			String[] keyTypes = keyType.split(",");
			String[] keyNames = keyName.split(",");
			String[] remarks = remark.split(",", -1);
			for (int i = 0; i < keyTypes.length; i++) {
				KeyIndex ind = new KeyIndex();
				ind.setKeyIndex(keyNames[i]);
				ind.setKeyName(keyTypes[i]);
				ind.setRemarks(remarks[i]);
				ind.setCreateUser(o.getLoginname());
				ind.setCreateTime(new Date());
				ind.setVersionId(keyIndex.getVersionId());
				inds.add(ind);
			}
			keyIndexService.add(inds);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		return j;
	}

	@RequestMapping("/get")
	@ResponseBody
	public KeyIndex get(String id) {
		return keyIndexService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			keyIndexService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		KeyIndex u = keyIndexService.get(id);
		request.setAttribute("keyindex", u);
		request.setAttribute("keyindextypeJson",
				dictionaryService.comboxCodeAndTextByParentTypeCode("secretversiontype"));
		return "/key/keyindexEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(KeyIndex ki, HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
			ki.setUpdateUser(o.getLoginname());
			keyIndexService.edit(ki);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
