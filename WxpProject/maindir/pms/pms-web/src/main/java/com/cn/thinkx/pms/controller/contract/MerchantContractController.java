package com.cn.thinkx.pms.controller.contract;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.contract.MerchantContract;
import com.cn.thinkx.pms.pageModel.contract.MerchantContractList;
import com.cn.thinkx.pms.pageModel.shop.MerchantInf;
import com.cn.thinkx.pms.service.base.CommonService;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.contract.MerchantContractService;
import com.cn.thinkx.pms.service.product.ProductService;
import com.cn.thinkx.pms.service.shop.MerchantService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Component
@Controller
@RequestMapping("/contract")
public class MerchantContractController extends BaseController {

	Logger log = Logger.getLogger("MerchantContractController");
	@Autowired
	private MerchantContractService merchantContractService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ProductService productService;
	@Autowired
	private DictionaryServiceI dictionaryService;
	@Autowired
	private MerchantService merchantService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("statelistList", GlobalConstant.convertJson(GlobalConstant.statelist));
		request.setAttribute("contractSettleCycleList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractSettleCycle")));
		request.setAttribute("contractSettleTypeList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractSettleType")));

		return "/contract/contract";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(MerchantContract contract, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(merchantContractService.dataGrid(contract, ph));
		grid.setTotal(merchantContractService.count(contract, ph));
		return grid;
	}
	
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request) {
		request.setAttribute("statelistList", GlobalConstant.convertJson(GlobalConstant.statelist));
		request.setAttribute("contractListTypeList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractListType")));
		request.setAttribute("productList", JSON.toJSONString(productService.findProductList(null)));

		List<MerchantInf> merchantInfList = merchantService.findMerchantList(null);
		request.setAttribute("merchantInfJson", merchantInfList);

		request.setAttribute("id", merchantContractService.initContractCode());

		request.setAttribute("contractSettleCycleList",
				dictionaryService.comboxCodeAndTextByTypeCode("contractSettleCycle"));
		request.setAttribute("contractSettleTypeList",
				dictionaryService.comboxCodeAndTextByTypeCode("contractSettleType"));

		return "/contract/contractAdd";
	}

	@RequestMapping("/addListPage")
	public String addListPage(HttpServletRequest request) {
		request.setAttribute("contractListTypeList", dictionaryService.comboxCodeAndTextByTypeCode("contractListType"));
		request.setAttribute("productList", productService.findProductList(null));
		request.setAttribute("contractSettleCycleList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractSettleCycle")));
		request.setAttribute("contractSettleTypeList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractSettleType")));
		return "/contract/contractListAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(MerchantContract contract, HttpServletRequest request) {
		SessionInfo session = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		List<MerchantContract> list = merchantContractService.queryByMerchantId(contract.getMerchantInfId());
		if (list != null) {
			for (MerchantContract l : list) {
				if (l.getContractEndDate().compareTo(contract.getContractStartDate())>=0) {
					j.setSuccess(false);
					j.setMsg("新合同的开始时间应大于上一合同结束时间");
					return j;
				}
			}
		}
		
		try {
			contract.setCreateUser(session.getLoginname());
			contract.setCreateTime(new Date());
			if (StringUtil.isNotBlank(contract.getMerchantContractListStr())) {
				JSONArray contractList = (JSONArray) JSONArray.parse(contract.getMerchantContractListStr().replaceAll(
						"&quot;", "\""));
				for (Object o : contractList) {
					JSON json = (JSON) o;
					MerchantContractList mc = json.parseObject(json.toJSONString(), MerchantContractList.class);
					mc.setDataStat("0");
					mc.setCreateTime(new Date());
					mc.setCreateUser(session.getLoginname());
					mc.setUpdateTime(new Date());
					mc.setUpdateUser(session.getLoginname());

					contract.getMerchantContractListSet().add(mc);
				}
			}
			merchantContractService.add(contract);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/get")
	@ResponseBody
	public MerchantContract get(String id) {
		return merchantContractService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		try {
			merchantContractService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		request.setAttribute("statelistList", GlobalConstant.convertJson(GlobalConstant.statelist));
		request.setAttribute("contractListTypeList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractListType")));
		request.setAttribute("productList", JSON.toJSONString(productService.findProductList(null)));

		List<MerchantInf> merchantInfList = merchantService.findMerchantList(null);
		request.setAttribute("merchantInfJson", merchantInfList);

		request.setAttribute("contractSettleCycleList",
				dictionaryService.comboxCodeAndTextByTypeCode("contractSettleCycle"));
		request.setAttribute("contractSettleTypeList",
				dictionaryService.comboxCodeAndTextByTypeCode("contractSettleType"));

		MerchantContract u = merchantContractService.get(id);
		List<MerchantContractList> mcl = u.getMerchantContractListSet();
		if (mcl != null && mcl.size() > 0) {
			String json = JSONArray.toJSONString(mcl);
			u.setMerchantContractListStr(json);
		}else{
			u.setMerchantContractListStr("[]");
		}
		request.setAttribute("contract", u);
		return "/contract/contractEdit";
	}

	
	@RequestMapping("/viewPage")
	public String viewPage(HttpServletRequest request, String id) {
		request.setAttribute("statelistList", GlobalConstant.convertJson(GlobalConstant.statelist));
		request.setAttribute("contractListTypeList",
				GlobalConstant.convertJson(dictionaryService.comboxMap("contractListType")));
		request.setAttribute("productList", JSON.toJSONString(productService.findProductList(null)));

		List<MerchantInf> merchantInfList = merchantService.findMerchantList(null);
		request.setAttribute("merchantInfJson", merchantInfList);

		request.setAttribute("contractSettleCycleList",
				dictionaryService.comboxCodeAndTextByTypeCode("contractSettleCycle"));
		request.setAttribute("contractSettleTypeList",
				dictionaryService.comboxCodeAndTextByTypeCode("contractSettleType"));

		MerchantContract u = merchantContractService.get(id);
		List<MerchantContractList> mcl = u.getMerchantContractListSet();
		if (mcl != null && mcl.size() > 0) {
			String json = JSONArray.toJSONString(mcl);
			u.setMerchantContractListStr(json);
		}else{
			u.setMerchantContractListStr("[]");
		}
		request.setAttribute("contract", u);
		return "/contract/contractView";
	}
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(MerchantContract contract, HttpServletRequest request) {
		SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		List<MerchantContract> list = merchantContractService.queryByMerchantId(contract.getMerchantInfId());
		if (list != null) {
			for (MerchantContract l : list) {
				if (	!l.getId().equals(contract.getId())
						&& l.getDataStat().equals(GlobalConstant.ENABLE.toString())
						&& contract.getContractStartDate().compareTo(l.getContractStartDate()) <= 0
						/*&& contract.getContractEndDate().compareTo(l.getContractStartDate())>=0*/) {
					j.setSuccess(false);
					j.setMsg("商户存在多个合同，如果要修改当前合同，请先删除最新合同。");
					return j;
				}
			}
		}
		try {
			contract.setUpdateUser(o.getLoginname());
			contract.setUpdateTime(new Date());
			if (StringUtil.isNotBlank(contract.getMerchantContractListStr())) {
				JSONArray contractList = (JSONArray) JSONArray.parse(contract.getMerchantContractListStr().replaceAll(
						"&quot;", "\""));
				for (Object cl : contractList) {
					JSON json = (JSON) cl;
					MerchantContractList mc = json.parseObject(json.toJSONString(), MerchantContractList.class);
					contract.getMerchantContractListSet().add(mc);
				}
			}
			merchantContractService.edit(contract,o.getLoginname());
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/deleteDetail")
	@ResponseBody
	public Json deleteDetail(String id) {
		Json j = new Json();
		try {
			merchantContractService.deleteDetail(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
