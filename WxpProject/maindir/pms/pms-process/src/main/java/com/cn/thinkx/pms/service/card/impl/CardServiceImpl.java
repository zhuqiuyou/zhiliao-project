package com.cn.thinkx.pms.service.card.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.LockTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.baseDao.BaseDaoI;
import com.cn.thinkx.pms.encdec.EncDec72_DataIN;
import com.cn.thinkx.pms.encdec.EncDec72_DataOUT;
import com.cn.thinkx.pms.model.card.TbAccountInf;
import com.cn.thinkx.pms.model.card.TbCardInf;
import com.cn.thinkx.pms.model.card.TbRelCardAccount;
import com.cn.thinkx.pms.model.product.TbProduct;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.card.Card;
import com.cn.thinkx.pms.service.card.CardService;
import com.cn.thinkx.pms.util.GlobalSocketConst;
import com.cn.thinkx.pms.util.PmsSession;
import com.cn.thinkx.pms.util.PmsSessionApp;
import com.cn.thinkx.pms.utils.OutputSecResult;
import com.cn.thinkx.pms.utils.SecException;
import com.cn.thinkx.pms.utils.StringUtil;

@Service
public class CardServiceImpl implements CardService {
	private static Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
	@Autowired
	private BaseDaoI<TbCardInf> cardDao;
	@Autowired
	private BaseDaoI<TbAccountInf> accountDao;
	@Autowired
	private BaseDaoI<TbProduct> productDao;

	//@Value("${orderDownloadPath}")
	//private String orderDownPath;

	@Override
	public Card search(String cardNo) {
		Card card = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardNo", cardNo);
		String hql = " select t from TbCardInf t  where t.cardNo= :cardNo ";
		List<TbCardInf> cardList = cardDao.find(hql, params);
		if (cardList != null && cardList.size() > 0) {
			card = new Card();
			TbCardInf tf = cardList.get(0);
			BeanUtils.copyProperties(tf, card);
			Set<TbRelCardAccount> ras = tf.getCardAccountSet();
			if (ras != null && ras.size() > 0) {
				TbRelCardAccount ra = (TbRelCardAccount) ras.toArray()[0];
				Map<String, Object> aparams = new HashMap<String, Object>();
				aparams.put("relCardAccount", ra);
				aparams.put("dataStat", "0");// 启用
				String aihql = " select t from TbAccountInf t left join t.cardAccountSet ca left join ca.card cc where ca=:relCardAccount  and ca.dataStat=:dataStat  ";
				List<TbAccountInf> accountList = accountDao.find(aihql, aparams);
				if (accountList != null && accountList.size() > 0) {
					BeanUtils.copyProperties(accountList.get(0), card.getAccount());
					card.getAccount().setAccBal(StringUtil.fromCentToYuan(card.getAccount().getAccBal()));
				}
			}
		}
		return card;
	}

	public String getCardCode(String code18) {
		if (code18.length() != 18) {
			return null;
		}
		char[] cs = code18.toCharArray();
		int last = 0;
		int sec = 0;
		for (int i = cs.length; i > 0; i--) {
			char b = cs[i - 1];
			if (i % 2 == 0) {
				last = last + Character.getNumericValue(b) * 2 / 10 + Character.getNumericValue(b) * 2 % 10;
			} else {
				sec = sec + Character.getNumericValue(b);
			}
		}
		int sub = last + sec;
		int b = (10 - sub % 10) % 10;
		return code18 + b;
	}

	@Override
	public List<Card> dataCardGrid(Map<String, Object> params, PageFilter ph) {
		String hql = " select t from TbCardInf t ";
		List<TbCardInf> cardInf = cardDao.find(hql + whereHql(params) + orderHql(ph), params, ph.getPage(),
				ph.getRows());
		List<Card> cardList = new ArrayList<Card>();
		for (TbCardInf t : cardInf) {
			Card u = new Card();
			BeanUtils.copyProperties(t, u);
			cardList.add(u);
		}
		return cardList;
	}

	private String whereHql(Map<String, Object> map) {
		String hql = " where 1=1 ";
		if (map.containsKey("startCardNum")) {
			hql += " and t.cardNo >=:startCardNum";
		}
		if (map.containsKey("endCardNum")) {
			hql += " and t.cardNo <= :endCardNum";
		}
		if (map.containsKey("productCode")) {
			hql += " and t.productCode = :productCode";
		}

		if (map.containsKey("stockStat")) {
			hql += " and t.stockStat in (:stockStat)";
		}
		if (map.containsKey("cardStat")) {
			hql += " and t.cardStat = :cardStat";
		}
		if (map.containsKey("cancelStat")) {
			hql += " and t.cancelStat = :cancelStat";
		}
		
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if (!StringUtil.isNullOrEmpty(ph.getSort()) && !StringUtil.isNullOrEmpty(ph.getOrder())) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public Long countCard(Map<String, Object> map, PageFilter ph) {
		String hql = " select count(t.id)  from TbCardInf t ";
		return cardDao.count(hql + whereHql(map), map);
	}

	@Override
	public Card updateCardStatus(Card card) throws Exception {
		try {

			TbCardInf tcf = cardDao.get("select t from TbCardInf t where t.cardNo='" + card.getCardNo() + "'");
			if (tcf.getLockVersion() != card.getLockVersion()) {
				throw new LockTimeoutException("version had changed!!");
			} else {
				tcf.setCardStat(card.getCardStat());// 卡状态
				tcf.setLockStat(card.getLockStat());
				tcf.setLostStat(card.getLostStat());
				tcf.setCancelStat(card.getCancelStat());
				tcf.setFreezeStat(card.getFreezeStat());
				if (StringUtil.isNotEmpty(card.getLockDate())) {
					tcf.setLockDate(card.getLockDate());
				}
				cardDao.saveOrUpdate(tcf);
			}
			BeanUtils.copyProperties(tcf, card);
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			throw e;
		}
		return card;
	}

	@Override
	public Boolean updateCardPassword(String cardNo, String oldPsw, String newPsw) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardNo", cardNo);
		params.put("dataStat", "0");// 启用
		String hql = " select tp from TbCardInf t , TbProduct tp where tp.productCode=t.productCode and t.cardNo= :cardNo and tp.dataStat=:dataStat  ";
		TbProduct product = productDao.get(hql, params);

		if (product == null) {
			logger.error("the card(" + cardNo + ") is not exist！");
			return false;
		}
		int nRet = 0;
		OutputSecResult outRslt = null;
		PmsSession session = null;
		EncDec72_DataIN encdec72in = null;
		EncDec72_DataOUT encdec72out = null;
		try {
			session = new PmsSession();
			PmsSessionApp hApp = new PmsSessionApp();
			nRet = session.GetLastError();
			if (nRet != 0) {
				logger.error(SecException.ERR_SEC_CODE_8002 + SecException.ERR_SEC_MSG_8002);
				throw new SecException(SecException.ERR_SEC_CODE_8002 + SecException.ERR_SEC_MSG_8002);
			}
			byte[] bts = encodePasswordAndCardNo(newPsw, cardNo);
			if (bts != null && bts.length == 8) {
				String keyIndex = product.getCvvKeyIndex();
				if (StringUtil.isEmpty(keyIndex)) {
					keyIndex = "016";
				}
				encdec72in = new EncDec72_DataIN(keyIndex.getBytes(), "00000000".getBytes(), "1".getBytes(),
						"0".getBytes(), "8".getBytes(), bts);

				encdec72out = new EncDec72_DataOUT();
				nRet = hApp.EncDec72_Data(session, encdec72in, encdec72out);
			} else {
				throw new Exception("password and cardNo transform  is failure");
			}

		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			throw new SecException(SecException.ERR_SEC_CODE_8002 + SecException.ERR_SEC_MSG_8002 + ":"
					+ new String(encdec72out.reply_code));
		} finally {
			if (session != null) {
				session.HsmSessionEnd();
			}
		}
		if (nRet != GlobalSocketConst.SUCCESS) {
			throw new SecException(SecException.ERR_SEC_CODE_8002 + SecException.ERR_SEC_MSG_8002 + ":"
					+ new String(encdec72out.reply_code));
		} else {
			outRslt = new OutputSecResult();
			outRslt.setData(encdec72out.data);
			outRslt.setDataLen(encdec72out.data_len);
			logger.info("EncData replyCode:" + PmsSessionApp.byte2hex(encdec72out.reply_code));
			logger.info("EncData length:" + PmsSessionApp.byte2hex(encdec72out.data_len));
			logger.info("EncData data:" + PmsSessionApp.byte2hex(encdec72out.data));
			
			TbCardInf tbcard = cardDao.get("select t from TbCardInf t where t.cardNo='" + cardNo + "'");
			if (!StringUtil.isNullOrEmpty(encdec72out.data)) {
				tbcard.setTxnPin(PmsSessionApp.byte2hex(encdec72out.data).replace(" ", ""));
			}
			cardDao.update(tbcard);
			return true;
		}

	}

	/**
	 * 将密码和卡号转化为高低位的编码
	 * 
	 * @param psw
	 *            6位 密码
	 * @param card
	 *            卡号
	 * @return
	 */
	private byte[] encodePasswordAndCardNo(String psw, String cardNo) {
		/* 密码长度 + 密码 + 8个零（补足十六位） */
		String sec = "06" + psw + "FFFFFFFF";
		/* FFFF（前补F）补足十六位， + 除去最后一位的校验位 向前去12为卡号 */
		// String sec1 = "FFFF"+"678900000004";
		String sec1 = "0000" + cardNo.substring(cardNo.length() - 13, cardNo.length() - 1);
		if (!(sec.matches("[0-9,A-F]{16}") && sec1.matches("[0-9,A-F]{16}"))) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {

			/* 16进制 转化 为10进制 */
			String c = sec.substring(i, i + 1);
			int k = Integer.parseInt(c, 16);

			String d = sec1.substring(i, i + 1);
			int kk = Integer.parseInt(d, 16);
			/* 异或 */
			sb.append(Integer.toHexString(k ^ kk));
		}
		String s = sb.toString();
		byte[] bts = new byte[8];
		for (int i = 0; i < 16; i = i + 2) {
			String c = s.substring(i, i + 1);
			int k = Integer.parseInt(c, 16);
			// 高位
			int t1 = k << 4;
			String c2 = s.substring(i + 1, i + 2);
			int k2 = Integer.parseInt(c2, 16);
			// 低位
			int t2 = k2;
			bts[i / 2] = (byte) (t1 | t2);
		}
		return bts;
	}
	
	public static void main(String[] args) {
		String psw="123456";
		String cardNo = "8888123456000000014";
		/* 密码长度 + 密码 + 8个零（补足十六位） */
		String sec = "06" + psw + "FFFFFFFF";
		/* FFFF（前补F）补足十六位， + 除去最后一位的校验位 向前去12为卡号 */
		// String sec1 = "FFFF"+"678900000004";
		String sec1 = "0000" + cardNo.substring(cardNo.length() - 13, cardNo.length() - 1);
		System.out.println(sec1);
		if (!(sec.matches("[0-9,A-F]{16}") && sec1.matches("[0-9,A-F]{16}"))) {
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {

			/* 16进制 转化 为10进制 */
			String c = sec.substring(i, i + 1);
			int k = Integer.parseInt(c, 16);

			String d = sec1.substring(i, i + 1);
			int kk = Integer.parseInt(d, 16);
			/* 异或 */
			sb.append(Integer.toHexString(k ^ kk));
		}
		String s = sb.toString();
		System.out.println(s);
		byte[] bts = new byte[8];
		for (int i = 0; i < 16; i = i + 2) {
			String c = s.substring(i, i + 1);
			int k = Integer.parseInt(c, 16);
			// 高位
			int t1 = k << 4;
			String c2 = s.substring(i + 1, i + 2);
			System.out.println(c+"====="+c2);
			int k2 = Integer.parseInt(c2, 16);
			// 低位
			int t2 = k2;
			bts[i / 2] = (byte) (t1 | t2);
		}
		System.out.println(bts.length);
	}
}
