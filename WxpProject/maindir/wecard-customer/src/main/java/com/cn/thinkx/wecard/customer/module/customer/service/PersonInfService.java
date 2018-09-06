package com.cn.thinkx.wecard.customer.module.customer.service;

import com.cn.thinkx.common.wecard.domain.person.PersonInf;
import com.cn.thinkx.common.wecard.domain.user.UserInf;

public interface PersonInfService {

	/**
	 * insert用户信息
	 * @return
	 */
	public int insertPersonInf(PersonInf personInf);
	
	/**
	 * 根据Id类型查询
	 * @param personId
	 * @return
	 */
	public PersonInf findPersonInfById(String personId);
	
	/**
	 * 客户会员注册
	 * @param openid
	 * @param phoneNumber
	 * @param password
	 * @return String userId
	 */
	public String addPersonInfRegister(String openid,String phoneNumber);
		
	/**
	 * 修改用户信息
	 * @param userInf
	 * @return
	 */
	public int updateUserInf(UserInf userInf);
	
	
	/**
	 * 获取用户个人信息
	 * @param userId
	 * @return
	 */
	public PersonInf getPersonInfByUserId(String userId);
	
	/**
	 * 获取用户的手机号码
	 * @param openid
	 * @return
	 */
	public String getPhoneNumberByOpenId(String openid);
	
	public PersonInf getPersonInfByAccountNo(String accountNo);
	
	/**
	 * 根据手机号查找用户
	 * @param phoneNo
	 * @param channel
	 * @return
	 */
	public PersonInf getPersonInfByPhoneAndChnl(String phoneNo,String channel);
	
	/**
	 * 根据openid查询用户（渠道号40001010）
	 * @param openid
	 * @return
	 */
	PersonInf getPersonInfByOpenId(String openid);
	
}
