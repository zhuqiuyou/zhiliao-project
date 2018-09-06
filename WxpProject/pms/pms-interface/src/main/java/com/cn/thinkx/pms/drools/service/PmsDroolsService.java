package com.cn.thinkx.pms.drools.service;

import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.cn.thinkx.pms.drools.fact.BaseFact;

@Service
/**
 * 规则引擎调用service
 * 
 * @author pucke
 *
 */
public interface PmsDroolsService {

	/**
	 * 根据名称得到KieSession
	 * 
	 * @param kSessionName
	 * @return
	 */
	public KieSession getKieSession(String kSessionName);

	/**
	 * 执行规则方法
	 * 
	 * @param kSession
	 *            KieSession
	 * @param fact
	 *            规则模型类
	 * @param expires
	 *            规则的过期时间
	 * @param effective
	 *            规则的生效时间
	 * @param duration
	 *            规则定时(单位：毫秒)
	 * @param salience
	 *            优先级
	 * @throws Exception
	 */
	public void excute(KieSession kSession, BaseFact fact, String expires, String effective, String duration,
			String salience) throws Exception;

}
