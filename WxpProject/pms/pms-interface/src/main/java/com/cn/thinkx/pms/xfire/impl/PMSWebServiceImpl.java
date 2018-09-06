package com.cn.thinkx.pms.xfire.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;

import com.cn.thinkx.pms.entity.MessageBody;
import com.cn.thinkx.pms.entity.RequestMessage;
import com.cn.thinkx.pms.entity.ResponseHeard;
import com.cn.thinkx.pms.entity.ResponseMessage;
import com.cn.thinkx.pms.entity.TxnPackageDTO;
import com.cn.thinkx.pms.service.Java2TXNBusinessService;
import com.cn.thinkx.pms.utils.ConnectConstant;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.ReadPropertiesFile;
import com.cn.thinkx.pms.xfire.PMSWebService;

public class PMSWebServiceImpl implements PMSWebService {

	private Logger LOG = Logger.getLogger(getClass().getName());

	private Java2TXNBusinessService businessService;

	private ReadPropertiesFile properties = new ReadPropertiesFile();

	@Override
	public ResponseMessage search(RequestMessage input) {
		LOG.info("Executing operation search");
		ResponseMessage response = new ResponseMessage();
		TxnPackageDTO txnPackageDTO = new TxnPackageDTO();

		BeanUtils.copyProperties(response.getResponseBody(), txnPackageDTO);
		Map<String, String> params = new HashMap<String, String>();

		params.put(ConnectConstant.TXN_TYPE, properties.getProperty("PAY_GATE_SEARCH_TXN_TYPE", null));
		params.put(ConnectConstant.TXN_CHANNEL, properties.getProperty("PAY_GATE_SEARCH_TXN_CHNL_ID", null));

		ResponseHeard responseHeard = new ResponseHeard();
		responseHeard.setResponseDate(DateUtil.convertCurrentDateTimeToString());
		responseHeard.setSeq(input.getRequestHeard().getSeq());
		MessageBody body = new MessageBody();
		try {
			TxnPackageDTO txn = businessService.queryTransation(txnPackageDTO, params);
			if ("00".equals(txn.getRspCode())) {
				responseHeard.setStatus("SUCCESS");
				BeanUtils.copyProperties(txn, body);
			} else {
				responseHeard.setStatus("FAILUER");
			}
		} catch (Exception e) {
			responseHeard.setStatus("FAILUER");
		}
		response.setResponseHeard(responseHeard);
		response.setResponseBody(body);
		return response;
	}

	public void setBusinessService(Java2TXNBusinessService businessService) {
		this.businessService = businessService;
	}

}
