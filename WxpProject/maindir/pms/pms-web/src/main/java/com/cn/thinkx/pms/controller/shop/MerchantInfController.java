package com.cn.thinkx.pms.controller.shop;

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
import com.cn.thinkx.pms.pageModel.shop.MerchantInf;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.shop.MerchantService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/merchant")
public class MerchantInfController extends BaseController {

	@Autowired
	private MerchantService merchantService;
	@Autowired
	private DictionaryServiceI dictionaryService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("statelistList", GlobalConstant.convertJson(GlobalConstant.statelist));
		return "/shop/merchant";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(MerchantInf merchant, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(merchantService.dataGrid(merchant, ph));
		grid.setTotal(merchantService.count(merchant, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		return "/shop/merchantAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(MerchantInf merchantInf, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		MerchantInf kv = merchantService.getMerchantInfByCode(merchantInf.getMchntCode());
		if (kv != null) {
			j.setMsg("该商户编号已存在!");
			return j;
		}
		try {
			merchantInf.setCreateUser(o.getLoginname());
			merchantInf.setCreateTime(new Date());
			merchantService.add(merchantInf);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/get")
	@ResponseBody
	public MerchantInf get(String id) {
		return merchantService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			merchantService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		MerchantInf u = merchantService.get(id);
		request.setAttribute("merchant", u);
		return "/shop/merchantEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(MerchantInf pageMnt, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		MerchantInf kv = merchantService.getMerchantInfByCode(pageMnt.getMchntCode());
		if (kv != null &&!kv.getMchntId().equals(pageMnt.getMchntId())) {
			j.setMsg("该商户编号已存在!");
			return j;
		}
		try {
			pageMnt.setUpdateUser(o.getLoginname());
			pageMnt.setUpdateTime(new Date());
			merchantService.edit(pageMnt);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpSession session) {
		return merchantService.tree();
	}
	public void setMerchantService(MerchantService MerchantService) {
		this.merchantService = MerchantService;
	}

	public void setDictionaryService(DictionaryServiceI dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
}
