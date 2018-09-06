package com.cn.thinkx.pms.controller.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cn.thinkx.pms.pageModel.key.KeyVersion;
import com.cn.thinkx.pms.pageModel.product.Product;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.key.KeyVersionService;
import com.cn.thinkx.pms.service.product.ProductService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private DictionaryServiceI dictionaryService;
	@Autowired
	private KeyVersionService keyVersionService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("versiontypeJson", JSON.toJSONString(keyVersionService.findKeyVersionList(null)));
		
		request.setAttribute("statelistList",GlobalConstant.convertJson(GlobalConstant.statelist));
		request.setAttribute("onymousStatList",GlobalConstant.convertJson(GlobalConstant.onymousStatList));
		request.setAttribute("businessTypeList", GlobalConstant.convertJson(GlobalConstant.businessTypeList));
		request.setAttribute("productTypeList", GlobalConstant.convertJson(GlobalConstant.productTypeList));
		return "/product/product";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(Product project, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(productService.dataGrid(project, ph));
		grid.setTotal(productService.count(project, ph));
		return grid;
	}

	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		Map<String,Object> versionMap = new HashMap<String, Object>();
		versionMap.put("versionType", "01");
		List<KeyVersion> versionList = keyVersionService.findKeyVersionList(versionMap);
		request.setAttribute("versiontypeJson", versionList);
		for (KeyVersion tmp : versionList) {
			if (tmp.getDftStat().equals(GlobalConstant.DEFAULT.toString())) {
				request.setAttribute("defaultCode", tmp.getVersionCode());
			}
		}
		request.setAttribute("onymousStatList", GlobalConstant.onymousStatList);
		request.setAttribute("businessTypeList", GlobalConstant.businessTypeList);
		request.setAttribute("productTypeList", GlobalConstant.productTypeList);
		return "/product/productAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(Product product, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		Product kv = productService.getProductByName(product.getProductName());
		if (kv != null) {
			j.setMsg("该产品已存在!");
			return j;
		}
		kv = productService.getProductByCardBin(product.getCardBin());
		if (kv != null) {
			j.setMsg("CardBin已被占用!");
			return j;
		}
		try {
			product.setCreateUser(o.getLoginname());
			product.setCreateTime(new Date());
			product.setMaxBalance(StringUtil.fromYuanToCent(product.getMaxBalance()));
			productService.add(product);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/get")
	@ResponseBody
	public Product get(String id) {
		return productService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			productService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		Product u = productService.get(id);
		u.setMaxBalance(StringUtil.fromCentToYuan(u.getMaxBalance()));
		request.setAttribute("product", u);
		Map<String,Object> versionMap = new HashMap<String, Object>();
		versionMap.put("versionType", "01");
		List<KeyVersion> versionList = keyVersionService.findKeyVersionList(versionMap);
		request.setAttribute("versiontypeJson", versionList);

		request.setAttribute("onymousStatList", GlobalConstant.onymousStatList);
		request.setAttribute("businessTypeList", GlobalConstant.businessTypeList);
		request.setAttribute("productTypeList", GlobalConstant.productTypeList);
		return "/product/productEdit";
	}
	@RequestMapping("/viewPage")
	public String viewPage(HttpServletRequest request, String id) {
		Product u = productService.get(id);
		u.setMaxBalance(StringUtil.fromCentToYuan(u.getMaxBalance()));
		request.setAttribute("product", u);
		
		List<KeyVersion> versionList = keyVersionService.findKeyVersionList(null);
		request.setAttribute("versiontypeJson", versionList);

		request.setAttribute("onymousStatList", GlobalConstant.onymousStatList);
		request.setAttribute("businessTypeList", GlobalConstant.businessTypeList);
		request.setAttribute("productTypeList", GlobalConstant.productTypeList);
		return "/product/productView";
	}
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Product product, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		Product kv = productService.getProductByName(product.getProductName());
		if (kv != null && !product.getProductCode().equals(kv.getProductCode())) {
			j.setMsg("该产品已存在!");
			return j;
		}
		kv = productService.getProductByCardBin(product.getCardBin());
		if (kv != null && !product.getProductCode().equals(kv.getProductCode())) {
			j.setMsg("CardBin已被占用!");
			return j;
		}
		try {
			product.setUpdateUser(o.getLoginname());
			product.setUpdateTime(new Date());
			product.setMaxBalance(StringUtil.fromYuanToCent(product.getMaxBalance()));
			productService.edit(product);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public void setDictionaryService(DictionaryServiceI dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
}
