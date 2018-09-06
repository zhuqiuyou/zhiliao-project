package com.cn.thinkx.pms.controller.shop;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.cn.thinkx.pms.pageModel.base.Tree;
import com.cn.thinkx.pms.pageModel.shop.MerchantInf;
import com.cn.thinkx.pms.pageModel.shop.ShopInf;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.shop.MerchantService;
import com.cn.thinkx.pms.service.shop.ShopInfService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController {

	@Autowired
	private ShopInfService shopInfService;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private DictionaryServiceI dictionaryService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		List<MerchantInf> merchantInfList = merchantService.findMerchantList( null);
		request.setAttribute("merchantInfJson",JSON.toJSONString(merchantInfList));
		request.setAttribute("statelistList",GlobalConstant.convertJson(GlobalConstant.statelist));
		return "/shop/shop";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(ShopInf keyIndex, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(shopInfService.dataGrid(keyIndex, ph));
		grid.setTotal(shopInfService.count(keyIndex, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, String mchntId) {
		List<MerchantInf> merchantInfList = merchantService.findMerchantList( null);
		if (mchntId != null) {
			request.setAttribute("dftMchant", mchntId);
		}
		request.setAttribute("merchantInfJson",merchantInfList);
		return "/shop/shopAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(ShopInf shop, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		ShopInf kv = shopInfService.getShopInfByCode(shop.getShopCode());
		if (kv != null) {
			j.setMsg("该版本中索引类型已存在!");
		} else {
			try {
				shop.setDataStat(GlobalConstant.START_STOP_STATUS.START.getValue());
				shop.setCreateUser(o.getLoginname());
				shop.setCreateTime(new Date());
				shopInfService.add(shop);
				j.setSuccess(true);
				j.setMsg("添加成功！");
			} catch (Exception e) {
				j.setMsg(e.getMessage());
			}

		}
		return j;
	}

	@RequestMapping("/get")
	@ResponseBody
	public ShopInf get(String id) {
		return shopInfService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			shopInfService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		ShopInf u = shopInfService.get(id);
		request.setAttribute("shop", u);
		List<MerchantInf> merchantInfList = merchantService.findMerchantList( null);
		request.setAttribute("merchantInfJson",merchantInfList);
		return "/shop/shopEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(ShopInf ki, HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
			ki.setUpdateUser(o.getLoginname());
			ki.setUpdateTime(new Date());
			shopInfService.edit(ki);
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
		return shopInfService.tree();
	}
}
