package com.cn.thinkx.pms.controller.key;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.key.KeyIndex;
import com.cn.thinkx.pms.pageModel.key.KeyVersion;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.key.KeyIndexService;
import com.cn.thinkx.pms.service.key.KeyVersionService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/keyversion")
public class KeyVersionController extends BaseController {

	@Autowired
	private KeyVersionService keyVersionService;

	@Autowired
	private DictionaryServiceI dictionaryService;
	
	@Autowired
	private KeyIndexService keyIndexService;
	
	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("keyTypelist", GlobalConstant.keyTypelist);
		return "/key/keyversion";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(KeyVersion keyversion, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(keyVersionService.dataGrid(keyversion, ph));
		grid.setTotal(keyVersionService.count(keyversion, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		request.setAttribute("keyTypelist", GlobalConstant.keyTypelist);
		request.setAttribute("commonDefaultlist", GlobalConstant.commonDefaultlist);
		return "/key/keyversionAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(KeyVersion keyVersion, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		KeyVersion kv = keyVersionService.getKeyVersionByCode(keyVersion.getVersionCode());
		if (kv != null) {
			j.setSuccess(false);
			j.setMsg("版本号已存在!");
			return j;
		}
		if (GlobalConstant.DEFAULT.toString().equals(keyVersion.getDftStat())) {
			KeyVersion tmp = new KeyVersion();
			tmp.setVersionType(keyVersion.getVersionType());
			tmp.setDftStat(keyVersion.getDftStat());
			long count = keyVersionService.count(tmp, null);
			if (count > 0L) {
				j.setSuccess(false);
				j.setMsg("当前默认已存在，请设置为非默认!");
				return j;
			}
		}
		
		try {
			keyVersion.setCreateUser(o.getLoginname());
			keyVersion.setCreateTime(new Date());
			keyVersionService.add(keyVersion);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpSession session) {
		return keyVersionService.tree();
	}
	@RequestMapping("/treeWithoutIndex")
	@ResponseBody
	public List<Tree> treeWithoutIndex(HttpSession session) {
		return keyVersionService.treeWithoutIndex();
	}
	@RequestMapping("/get")
	@ResponseBody
	public KeyVersion get(String id) {
		return keyVersionService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		
		List<KeyIndex> list = keyIndexService.findKeyIndexListByVersionId(id);
		if (list != null && !list.isEmpty()) {
			j.setMsg("当前版本已添加过密钥索引！");
			j.setSuccess(false);
			return j;
		}
		try {
			keyVersionService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		KeyVersion u = keyVersionService.get(id);
		request.setAttribute("keyversion", u);
		request.setAttribute("keyTypelist", GlobalConstant.keyTypelist);
		request.setAttribute("commonDefaultlist", GlobalConstant.commonDefaultlist);
		return "/key/keyversionEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(KeyVersion keyVersion, HttpServletRequest request) {
		Json j = new Json();
		KeyVersion tkv = keyVersionService.get(keyVersion.getVersionId());
		if (!tkv.getDftStat().equals(keyVersion.getDftStat())
				&& keyVersion.getDftStat().equals(""+GlobalConstant.DEFAULT)) {
			KeyVersion tmp = new KeyVersion();
			tmp.setVersionType(keyVersion.getVersionType());
			tmp.setDftStat(keyVersion.getDftStat());
			long count = keyVersionService.count(tmp, null);
			if (count > 0L) {
				j.setSuccess(false);
				j.setMsg("当前默认已存在，请设置为非默认!");
				return j;
			}
		}
		try {
			SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
			keyVersion.setUpdateUser(o.getLoginname());
			keyVersion.setUpdateTime(new Date());
			keyVersionService.edit(keyVersion);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
