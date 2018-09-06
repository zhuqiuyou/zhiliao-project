package com.cn.thinkx.pms.controller.shop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.key.KeyIndex;
import com.cn.thinkx.pms.pageModel.key.KeyVersion;
import com.cn.thinkx.pms.pageModel.shop.MerchantInf;
import com.cn.thinkx.pms.pageModel.shop.PosInf;
import com.cn.thinkx.pms.pageModel.shop.ShopInf;
import com.cn.thinkx.pms.pageModel.sys.SysParameter;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.key.KeyIndexService;
import com.cn.thinkx.pms.service.key.KeyVersionService;
import com.cn.thinkx.pms.service.shop.MerchantService;
import com.cn.thinkx.pms.service.shop.PosInfService;
import com.cn.thinkx.pms.service.shop.ShopInfService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.service.sys.SysParamService;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Controller
@RequestMapping("/pos")
public class PosController extends BaseController {

	@Autowired
	private PosInfService posInfService;
	@Autowired
	private ShopInfService shopInfService;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private DictionaryServiceI dictionaryService;
	@Autowired
	private KeyVersionService keyVersionService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private KeyIndexService keyIndexService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("termSigninStatlist", GlobalConstant.convertJson(GlobalConstant.termSigninStatlist));
		request.setAttribute("pos_effectlist", GlobalConstant.convertJson(GlobalConstant.pos_effectlist));
		List<ShopInf> shopInfList = shopInfService.findShopList(null);
		request.setAttribute("shopInfList", shopInfList);
		return "/shop/pos";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(PosInf pos, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(posInfService.dataGrid(pos, ph));
		grid.setTotal(posInfService.count(pos, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, String id) {
		ShopInf shopInf = new ShopInf();
		if (!"-1".equals(id)) {

		}
		List<ShopInf> shopInfList = shopInfService.findShopList(shopInf);
		request.setAttribute("shopInfList", shopInfList);
		Map<String,Object> versionMap = new HashMap<String, Object>();
		versionMap.put("versionType", GlobalConstant.KEY_TYPE_POS);
		List<KeyVersion> keyVersionList = keyVersionService.findKeyVersionList(versionMap);
		request.setAttribute("keyVersionList", keyVersionList);
		// 设置默认选项
		for (KeyVersion tmp : keyVersionList) {
			if (tmp.getDftStat().equals(GlobalConstant.DEFAULT.toString())) {
				String versionId = tmp.getVersionId();
				request.setAttribute("defaultVersionId", versionId);
			}
		}
		request.setAttribute("allowlist", GlobalConstant.allowlist);
		request.setAttribute("termSigninStatlist", GlobalConstant.termSigninStatlist);
		request.setAttribute("pos_effectlist",GlobalConstant.pos_effectlist);
		request.setAttribute("pos_testList",GlobalConstant.pos_testList);
		request.setAttribute("pos_IC_download_flag",GlobalConstant.pos_IC_download_flag);
		
		List<SysParameter> prmVersion1List = sysParamService.findVersionByType(10001);
		List<SysParameter> prmVersion2List = sysParamService.findVersionByType(10002);
		
		request.setAttribute("prmVersion1List",prmVersion1List);
		request.setAttribute("prmVersion2List",prmVersion2List);
		return "/shop/posAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(PosInf pos, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		PosInf kv = posInfService.getPosInfByTermId(pos.getTermId());
		if (kv != null) {
			j.setMsg("该终端号信息已存在!");
		} else {
			try {
				pos.setCreateUser(o.getLoginname());
				pos.setCreateTime(new Date());
				pos.setUpdateTime(new Date());
				posInfService.add(pos);
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
	public PosInf get(String id) {
		return posInfService.get(id);
	}

	@RequestMapping("/initRef")
	@ResponseBody
	public Json initRef(String shopId, String versionId) {
		Json j = new Json();
		JSONObject json = new JSONObject();
		if (StringUtil.isNotBlank(shopId)) {
			ShopInf shop = shopInfService.get(shopId);
			MerchantInf mch = merchantService.get(shop.getMchntId());
			json.put("ShopInf", shop);
			json.put("MerchantInf", mch);
		}
		if (StringUtil.isNotBlank(versionId)) {
			KeyVersion keyVersion = keyVersionService.get(versionId);
			List<KeyIndex> indexList = keyIndexService.findKeyIndexListByVersionId(keyVersion.getVersionId());
			json.put("KeyVersion", keyVersion);
			json.put("KeyIndexList", indexList);
		}
		j.setSuccess(true);
		j.setObj(json);
		return j;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			posInfService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		PosInf u = posInfService.get(id);
		request.setAttribute("pos", u);
		List<MerchantInf> merchantInfList = merchantService.findMerchantList(null);
		request.setAttribute("merchantInfJson", merchantInfList);
		
		ShopInf shopInf = new ShopInf();
		List<ShopInf> shopInfList = shopInfService.findShopList(shopInf);
		request.setAttribute("shopInfList", shopInfList);
		Map<String,Object> versionMap = new HashMap<String, Object>();
		versionMap.put("versionType", GlobalConstant.KEY_TYPE_POS);
		List<KeyVersion> keyVersionList = keyVersionService.findKeyVersionList(versionMap);
		request.setAttribute("keyVersionList", keyVersionList);
		request.setAttribute("allowlist", GlobalConstant.allowlist);
		request.setAttribute("termSigninStatlist", GlobalConstant.termSigninStatlist);
		request.setAttribute("pos_effectlist",GlobalConstant.pos_effectlist);
		request.setAttribute("pos_testList",GlobalConstant.pos_testList);
		request.setAttribute("pos_IC_download_flag",GlobalConstant.pos_IC_download_flag);
		request.setAttribute("pos_TMK_download_flag",GlobalConstant.pos_TMK_download_flag);
		
		List<SysParameter> prmVersion1List = sysParamService.findVersionByType(10001);
		List<SysParameter> prmVersion2List = sysParamService.findVersionByType(10002);
		
		request.setAttribute("prmVersion1List",prmVersion1List);
		request.setAttribute("prmVersion2List",prmVersion2List);
		return "/shop/posEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(PosInf ki, HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
			ki.setUpdateUser(o.getLoginname());
			ki.setUpdateTime(new Date());
			posInfService.edit(ki);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
