package com.cn.thinkx.pms.controller.card;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.card.Card;
import com.cn.thinkx.pms.service.base.ServiceException;
import com.cn.thinkx.pms.service.card.CardService;
import com.cn.thinkx.pms.service.sys.DictionaryServiceI;
import com.cn.thinkx.pms.utils.DateUtil;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/card")
public class CardController extends BaseController {

	@Autowired
	private CardService cardService;
	@Autowired
	private DictionaryServiceI dictionaryService;

	@RequestMapping("/manager")
	public String manager(HttpServletRequest request) {
		request.setAttribute("stockStatList", dictionaryService.comboxCodeAndTextByTypeCode("stockStatList"));
		request.setAttribute("accountStatList", dictionaryService.comboxCodeAndTextByTypeCode("accountStatList"));
		return "/order/cardmanger";
	}

	@RequestMapping("/search")
	@ResponseBody
	public Json search(String card) {
		Json j = new Json();
		Card ol = cardService.search(card);
		if (ol == null) {
			j.setSuccess(false);
			j.setMsg("该卡不存在!");
		} else {
			ol.setCardStatCN(getCardStateCNByCard(ol));
			j.setSuccess(true);
			j.setMsg(ol.getCardNo());
			j.setObj(ol);
		}
		return j;
	}

	private String getCardStateCNByCard(Card card) {

		StringBuffer cardStatus = new StringBuffer();
		cardStatus.append(GlobalConstant.cardStatList.get(card.getCardStat()));
		String reStr = cardStatus.toString();
		if ("10".equals(card.getCardStat())) {
			cardStatus.append("   ");
			if ("0".equals(card.getLostStat())) {
				cardStatus.append("挂失");
				cardStatus.append("/");
			}
			if ("0".equals(card.getLockStat())) {
				cardStatus.append("锁定");
				cardStatus.append("/");
			}
			if ("0".equals(card.getFreezeStat())) {
				cardStatus.append("冻结");
				cardStatus.append("/");
			}
			if ("0".equals(card.getCancelStat())) {
				cardStatus.append("注销");
				cardStatus.append("/");
			}
			reStr = cardStatus.toString();
			if (reStr.endsWith("/")) {
				reStr = reStr.substring(0, reStr.length() - 1);
			} else {
				cardStatus.append("正常");
				reStr = cardStatus.toString();
			}
		}
		return reStr;
	}

	@RequestMapping("/lost")
	@ResponseBody
	public Json lost(String cardNo) {
		Json j = new Json();
		try {
			Card card = cardService.search(cardNo);
			if (card == null) {
				j.setSuccess(false);
				j.setMsg("该卡不存在!");
			} else {
				if ("0".equals(card.getLostStat())) {
					card.setLostStat("1");
				} else {
					card.setLostStat("0");
				}
				Card ol = cardService.updateCardStatus(card);
				j.setSuccess(true);
				j.setMsg(ol.getCardNo());
				j.setObj(ol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping("/lock")
	@ResponseBody
	public Json lock(String cardNo) {
		Json j = new Json();
		try {
			Card card = cardService.search(cardNo);
			if (card == null) {
				j.setSuccess(false);
				j.setMsg("该卡不存在!");
			} else {
				if ("0".equals(card.getLockStat())) {
					card.setLockStat("1");
				} else {
					card.setLockStat("0");
					card.setLockDate(DateUtil.getStringFromDate(DateUtil.getCurrentDate(), DateUtil.FORMAT_YYYYMMDD));
				}
				Card ol = cardService.updateCardStatus(card);
				j.setSuccess(true);
				j.setMsg(ol.getCardNo());
				j.setObj(ol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping("/freeze")
	@ResponseBody
	public Json freeze(String cardNo) {
		Json j = new Json();
		try {
			Card card = cardService.search(cardNo);
			if (card == null) {
				j.setSuccess(false);
				j.setMsg("该卡不存在!");
			} else {
				if ("0".equals(card.getFreezeStat())) {
					card.setFreezeStat("1");
				} else {
					card.setFreezeStat("0");
				}
				Card ol = cardService.updateCardStatus(card);
				j.setSuccess(true);
				j.setMsg(ol.getCardNo());
				j.setObj(ol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping("/active")
	@ResponseBody
	public Json active(String cardNo) {
		Json j = new Json();
		try {
			Card card = cardService.search(cardNo);
			if (card == null) {
				j.setSuccess(false);
				j.setMsg("该卡不存在!");
			} else {
				card.setCardStat("10");
				Card ol = cardService.updateCardStatus(card);
				j.setSuccess(true);
				j.setMsg(ol.getCardNo());
				j.setObj(ol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping("/cancel")
	@ResponseBody
	public Json cancel(String cardNo) {
		Json j = new Json();
		try {
			Card card = cardService.search(cardNo);
			if (card == null) {
				j.setSuccess(false);
				j.setMsg("该卡不存在!");
			} else {
				if ("0".equals(card.getCancelStat())) {
					card.setCancelStat("1");
				} else {
					card.setCancelStat("0");
				}
				Card ol = cardService.updateCardStatus(card);
				j.setSuccess(true);
				j.setMsg(ol.getCardNo());
				j.setObj(ol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping("/resetPsw")
	public String resetPsw(HttpServletRequest request, String cardNo) {
		request.setAttribute("cardNo", cardNo);
		return "/order/cardResetPsw";
	}

	@RequestMapping("/submitPsw")
	@ResponseBody
	public Json edit(String password, String newPassword, String cardNo) {
		Json j = new Json();
		try {
			Card card = cardService.search(cardNo);
			if (card == null) {
				j.setSuccess(false);
				j.setMsg("该卡不存在!");
			} else {
				boolean flag = cardService.updateCardPassword(cardNo, null, newPassword);
				if (flag) {
					j.setMsg("修改成功！");
					j.setSuccess(true);
				} else {
					j.setMsg("修改失败！");
					j.setSuccess(false);
				}
			}
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/setPsw")
	public String setPsw(HttpServletRequest request, String cardNo) {
		request.setAttribute("cardNo", cardNo);
		return "/order/cardSetPsw";
	}
}
