package com.cn.thinkx.pms.service.card;

import java.util.List;
import java.util.Map;

import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.card.Card;

public interface CardService {

	public Card search(String cardNo);

	public List<Card> dataCardGrid(Map<String, Object> map, PageFilter ph);

	public Long countCard(Map<String, Object> map, PageFilter ph);

	/**
	 * 获取卡号 19位 第一步：拼接卡BIN及顺序流水号作为基础卡号 如：666888200000000001(10位卡bin：6668882000 +
	 * 8位流水 00000001) 第一步：从基础卡号右边起第一个数字（低序）开始每隔一位乘以2。 如：1*2=2, 0*2=0, 0*2=0,
	 * 0*2=0, 0*2=0, 0*2=0,8*2=16, 8*2=16, 6*2=12
	 * 第二步：把在第一步中获得的乘积的各位数字与原号码中未乘2的各位数字相加求和。
	 * 如：（2+0+0+0+0+0+1+6+1+6+1+2）+（6+6+8+
	 * 2+0+0+0+0+0）=41(第一个括号是第一步中得到的乘积的各位数，第二个括号是原基础卡号中未乘2的各位数)
	 * 第三步：第二步得到的和中取个位数，这个个位数的10的补数作为校验位。
	 * 如：41取个位数是1,1的10的补数是9，即该卡的校验位位9（0的10的补数是0，其他的10的补数是10减去该数，在此例中是10-1=9）。
	 * 第四部：完整卡号是6668882000000000019（10位卡bin：6668882000 + 8位流水 00000001+校验位9）
	 * 
	 * @param code18
	 * @return
	 */
	public String getCardCode(String code18);

	/**
	 * 更新card状态
	 * @param card
	 * @return
	 * @throws Exception
	 */
	public Card updateCardStatus(Card card)throws Exception;
	
	public Boolean updateCardPassword(String cardNo,String oldPsw,String newPsw)throws Exception;
}
