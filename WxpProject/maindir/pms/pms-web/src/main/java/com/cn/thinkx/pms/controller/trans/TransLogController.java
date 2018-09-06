package com.cn.thinkx.pms.controller.trans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Grid;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.trans.TransInf;
import com.cn.thinkx.pms.pageModel.trans.TransLog;
import com.cn.thinkx.pms.service.trans.TransInfoService;
import com.cn.thinkx.pms.service.trans.TransLogService;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.GlobalConstant;
import com.cn.thinkx.pms.utils.StringUtil;

@Controller
@RequestMapping("/translog")
public class TransLogController extends BaseController {

	Logger log = Logger.getLogger(TransLogController.class.getName());
	@Autowired
	private TransInfoService transInfoService;
	@Autowired
	private TransLogService transLogService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		List<TransInf> transInfList = transInfoService.findTransInfos(null);
		request.setAttribute("tranTypeList", JSON.toJSONString(transInfList));
//		request.setAttribute("transStatusList",
//				JSON.toJSONString(dictionaryService.comboxCodeAndTextByTypeCode("transStatusList")));
		request.setAttribute("curOrHisList", GlobalConstant.convertJson(GlobalConstant.recordDatelist));

		request.setAttribute("tranTypeListObj", transInfList);
		request.setAttribute("curOrHisListObj", GlobalConstant.recordDatelist);
		return "/trans/translog";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TransLog log, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(transLogService.dataGrid(log, ph));
		grid.setTotal(transLogService.count(log, ph));
		return grid;
	}

	@RequestMapping("/exportLog")
	public void exportLog(HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream toClient = null;
		String filename = null;
		try {
			
			filename = "detail_"+System.currentTimeMillis()+".xls";
			TransLog transLog = new TransLog();
			transLog.setMchntCode(request.getParameter("mchntCode"));
			transLog.setShopCode(request.getParameter("shopCode"));
			transLog.setTermCode(request.getParameter("termCode"));
			transLog.setCardNo(request.getParameter("cardNo"));
			transLog.setTransId(request.getParameter("transId"));
			transLog.setIsCurrent(request.getParameter("isCurrent"));
			if (StringUtil.isNullOrEmpty(request.getParameter("settleTimeStart"))) {
				// 查询当天
				transLog.setSettleTimeStart(DateUtil.getStringFromDate(DateUtil.getCurrentDate(), DateUtil.FORMAT_YYYYMMDD));
			} else {
				// 查询历史
				transLog.setSettleTimeStart(request.getParameter("settleTimeStart"));
			}
			if (StringUtil.isNullOrEmpty(request.getParameter("settleTimeEnd"))) {
				// 查询当天
				transLog.setSettleTimeEnd(DateUtil.getStringFromDate(DateUtil.getCurrentDate(), DateUtil.FORMAT_YYYYMMDD));
			} else {
				// 查询历史
				transLog.setSettleTimeEnd(request.getParameter("settleTimeEnd"));
			}
			PageFilter ph = new PageFilter();
			createExcel(filename, transLog, ph);
			fis = new BufferedInputStream(new FileInputStream(filename));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + new File(filename).length());
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
			if (filename != null) {
				
				File f = new File(filename);
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}
	
	public void createExcel(String filename, TransLog log, PageFilter ph) throws WriteException,IOException{
        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("交易明细",0);
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        createFirstLine(sheet);
        ph.setOrder("asc");
        ph.setSort("CREATE_TIME");
        List<TransLog> logList = transLogService.queryTransLog(log, ph);
        if (logList != null && !logList.isEmpty()) {
        	//List<Dictionary> transStatusList = dictionaryService.comboxCodeAndTextByTypeCode("transStatusList");
        	List<TransInf> transInfList = transInfoService.findTransInfos(null);
        	for (int i=0; i<logList.size(); i++) {
        		TransLog l = logList.get(i);
        		int line = i+1;
        		Label l0 = new Label(0,line,l.getTxnPrimaryKey());
        		sheet.addCell(l0);
        		Label l1 = new Label(1,line,l.getDmsRelatedKey());
        		sheet.addCell(l1);
        		Label l2 = new Label(2,line,l.getSettleDate());
        		sheet.addCell(l2);
        		Label l3 = new Label(3,line,l.getCardNo());
        		sheet.addCell(l3);
        		Label l4 = new Label(4,line,l.getPriAcctNo());
        		sheet.addCell(l4);
        		Label l5 = new Label(5,line,l.getMchntName());
        		sheet.addCell(l5);
        		Label l6 = new Label(6,line,l.getShopName());
        		sheet.addCell(l6);
        		Label l7 = new Label(7,line,l.getTermCode());
        		sheet.addCell(l7);
        		Label l8 = new Label(8,line,getTransNameById(transInfList, l.getTransId()));
        		sheet.addCell(l8);
        		Label l9 = new Label(9,line,l.getTransAmt());
        		sheet.addCell(l9);
        		Label l10 = new Label(10,line,getTextByCode(l.getRespCode()));
        		sheet.addCell(l10);
        		Label l11 = new Label(11,line,""+l.getCreateTime());
        		sheet.addCell(l11);
        	}
        }
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        //os.close();
    }
	
	private String getTransNameById(List<TransInf> list, String id) {
		
		if (list != null && !list.isEmpty()) {
			for (TransInf t : list) {
				if (t.getTransId().equals(id)) {
					return t.getTransName();
				}
			}
		}
		return "未知类型";
	}
	
	private String getTextByCode(String code) {
		
		if(code != null && code.equals(GlobalConstant.RESPONSE_SUCCESS_CODE)) {
			return "交易成功";
		}
		return "交易失败";
	}

	private static void createFirstLine(WritableSheet sheet) throws RowsExceededException, WriteException {
		Label l0 = new Label(0,0,"交易流水号");
        sheet.addCell(l0);
        Label l1 = new Label(1,0,"交易参考号");
        sheet.addCell(l1);
        Label l2 = new Label(2,0,"清算日期");
        sheet.addCell(l2);
        Label l3 = new Label(3,0,"卡号");
        sheet.addCell(l3);
        Label l4 = new Label(4,0,"账户号");
        sheet.addCell(l4);
        Label l5 = new Label(5,0,"商户名称");
        sheet.addCell(l5);
        Label l6 = new Label(6,0,"门店名称");
        sheet.addCell(l6);
        Label l7 = new Label(7,0,"终端号");
        sheet.addCell(l7);
        Label l8 = new Label(8,0,"交易类型");
        sheet.addCell(l8);
        Label l9 = new Label(9,0,"交易金额");
        sheet.addCell(l9);
        Label l10 = new Label(10,0,"交易状态");
        sheet.addCell(l10);
        Label l11 = new Label(11,0,"交易时间");
        sheet.addCell(l11);
        
	}

}
