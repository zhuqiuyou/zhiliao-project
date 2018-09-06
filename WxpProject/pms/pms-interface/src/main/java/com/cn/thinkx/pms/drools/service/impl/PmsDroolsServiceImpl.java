package com.cn.thinkx.pms.drools.service.impl;

import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.cn.thinkx.pms.drools.fact.BaseFact;
import com.cn.thinkx.pms.drools.service.PmsDroolsService;

public class PmsDroolsServiceImpl implements PmsDroolsService {

	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public KieSession getKieSession(String kSessionName) {
		try {
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession session = kContainer.newKieSession(kSessionName);
			return session;
		} catch (Exception e) {
			logger.error("New KieSession throws exception:" + e);
		}
		return null;
	}

	@Override
	public void excute(KieSession kSession, BaseFact fact, String expires, String effective, String duration,
			String salience) throws Exception {
		if (kSession == null) {
			throw new Exception("kSession can not be null !");
		} else if (fact == null) {
			throw new Exception("fact can not be null !");
		} else {
			try {
				fact.setExpires(expires);
				fact.setEffective(effective);
				fact.setDuration(duration);
				fact.setSalience(salience);
				
				kSession.insert(fact);
				kSession.fireAllRules();
				kSession.dispose();// Stateful rule session must always be disposed when finished
			} catch (Exception e) {
				logger.error("New KieSession throws exception:" + e);
			}
		}
	}

}
