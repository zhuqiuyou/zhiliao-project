package com.cn.thinkx.pms.controller.settlebill;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.settle.SettleBill;
import com.cn.thinkx.pms.pageModel.sys.Dictionary;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.settle.SettleBillService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/settlebill")
public class SettleBillController extends BaseController {

	Logger log = Logger.getLogger(SettleBillController.class.getName());
	
	@Autowired
	private DictionaryServiceI dictionaryService;
	
	@Autowired
	private SettleBillService settleBillService;
	
	@Value("${settleBillPath}")
	private String settleBillPath;
	
	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		List<Dictionary> billStatList = dictionaryService.comboxCodeAndTextByTypeCode("settleStat");
		request.setAttribute("billStatListJson",JSON.toJSONString(billStatList));
		request.setAttribute("billStatList",billStatList);
		String resultPage = "/settlebill/settlebill";
		return resultPage;
	}
	
	@RequestMapping("/managerCreate")
	public String managerCreate(HttpServletRequest request) {
		List<Dictionary> billStatList = dictionaryService.comboxCodeAndTextByTypeCode("settleStat");
		request.setAttribute("billStatListJson",JSON.toJSONString(billStatList));
		request.setAttribute("billStatList",billStatList);
		String resultPage = "/settlebill/settlebillCreate";
		return resultPage;
	}
	
	@RequestMapping("/managerPay")
	public String managerPay(HttpServletRequest request) {
		List<Dictionary> billStatList = dictionaryService.comboxCodeAndTextByTypeCode("settleStat");
		request.setAttribute("billStatListJson",JSON.toJSONString(billStatList));
		request.setAttribute("billStatList",billStatList);
		String resultPage = "/settlebill/settlebillPay";
		return resultPage;
	}

	@RequestMapping("/dataGrid/{billStat}")
	@ResponseBody
	public Grid dataGrid(SettleBill settleBill, PageFilter ph, @PathVariable("billStat") String billStat) {
		Grid grid = new Grid();
		if (!"all".equals(billStat)) {
			settleBill.setBillStat(billStat);
		}
		grid.setRows(settleBillService.dataGrid(settleBill, ph));
		grid.setTotal(settleBillService.count(settleBill, ph));
		return grid;
	}

	@RequestMapping("/get")
	@ResponseBody
	public SettleBill get(String id) {
		return settleBillService.get(id);
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			String settleId = request.getParameter("settleId");
			String filename = settleId + "detail.csv";
			String path = settleBillPath + File.separator + filename;
			File file = new File(path);
			if (!file.exists()) {
				return;
			}
			fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} catch (Exception e) {
			log.log(Level.FINEST, e.getMessage(), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (toClient != null) {
				try {
					toClient.flush();
					toClient.close();
				} catch (IOException e) {
				}
			}
		}
	}

	
	@RequestMapping("/updateStat")
	public void updateStat(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("settleId");
		String stat = request.getParameter("stat");
		String settleIdArr[] = ids.split(",");
		for (String settleId : settleIdArr) {
			SettleBill settleBill = settleBillService.get(settleId);
			if (settleBill != null) {
				settleBill.setBillStat(stat);
			}
			settleBillService.edit(settleBill);
		}
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, String settleId) {
		SettleBill u = settleBillService.get(settleId);
		List<Dictionary> billStatList = dictionaryService.comboxCodeAndTextByTypeCode("settleStat");
		request.setAttribute("billStatList",billStatList);
		request.setAttribute("settlebill", u);
		return "/settlebill/settlebillEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(SettleBill settleBill, HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo o = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
			settleBill.setUpdateUser(o.getLoginname());
			settleBill.setUpdateTime(new Date());
			settleBillService.edit(settleBill);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/viewPage")
	public String viewPage(HttpServletRequest request, String settleId) {
		SettleBill u = settleBillService.get(settleId);
		request.setAttribute("settlebill", u);
		List<Dictionary> billStatList = dictionaryService.comboxCodeAndTextByTypeCode("settleStat");
		request.setAttribute("billStatList",billStatList);
		return "/settlebill/settlebillView";
	}

	public String getSettleBillPath() {
		return settleBillPath;
	}

	public void setSettleBillPath(String settleBillPath) {
		this.settleBillPath = settleBillPath;
	}

}
